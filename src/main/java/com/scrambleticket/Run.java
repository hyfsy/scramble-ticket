
package com.scrambleticket;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson2.JSON;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.DefaultClient;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.client.interceptor.*;
import com.scrambleticket.config.Switch;
import com.scrambleticket.config.SystemConfig;
import com.scrambleticket.config.TaskConfig;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowPipeline;
import com.scrambleticket.handler.context.CandidateContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.login.*;
import com.scrambleticket.handler.preload.PreLoader;
import com.scrambleticket.handler.scramble.CheckUserHandler;
import com.scrambleticket.handler.scramble.QueryXHandler;
import com.scrambleticket.handler.scramble.candidate.CandidateTicketDispatcher;
import com.scrambleticket.handler.scramble.common.ScrambleTicketDispatcher;
import com.scrambleticket.limit.LimitStrategy;
import com.scrambleticket.limit.SimpleLimitStrategy;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.model.SeatType;
import com.scrambleticket.model.TicketType;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.performance.StopWatch;
import com.scrambleticket.service.*;
import com.scrambleticket.util.ConfigUtil;
import com.scrambleticket.util.StringUtil;

public class Run {

    static AtomicInteger connectionIdGenerator = new AtomicInteger();

    Client client = new DefaultClient(Constants.HOST, Constants.PORT);
    UserSessionService userSessionService = new DefaultUserSessionService();
    LimitStrategy limitStrategy = new SimpleLimitStrategy();

    // mock login
    String cookieString = SystemConfig.getStr("cookieString");

    public static void main(String[] args) throws Exception {

        new Run().execute();

    }

    public void execute() throws Exception {

        StopWatch stopWatch = new StopWatch();

        // 一个用户一个context
        FlowContext context = createFlowContext();
        initUserSession(context);
        injectTaskConfig(context, "static/config/config_test.json");
        // injectTaskConfig(context, "static/config/config_1430_back.json");
        // injectTaskConfig(context, "static/config/config_1430_back_morning.json");
        // injectTaskConfig(context, "static/config/config_0830_go.json");

        try {

            login(context.copyFrom());

            stopWatch.record("login time");

            preload(context.copyFrom(true));

            stopWatch.record("preload time");

            // 限流重置，下面流程的6个接口保证仅限制一次
            Thread.sleep(1000L);

            stopWatch.record("limit wait time");

            Cost.getInstance().enable();

            executeTask(stopWatch, context.copyFrom());

            stopWatch.recordCurrent("stop task time");
            stopWatch.recordPass("task processed time");

        }
        finally {
            context.getClient().close();
        }
    }

    private FlowContext createFlowContext() {
        // 一个用户一个context
        FlowContext context = new FlowContext();
        CookieStorage cookieStorage = new CookieStorage().setDefaultCookie();
        Client realClient = createClient(context);;
        context.setClient(realClient);
        context.setConnectionId(connectionIdGenerator.incrementAndGet());
        context.setCookieStorage(cookieStorage);
        // 每个用户一个
        InteractionService interactionService = new MockInteractionService(userSessionService, context.getConnectionId());
        context.setInteractionService(interactionService);
        return context;
    }

    private Client createClient(FlowContext context) {
        Client interceptClient = Interceptors.intercept(client, //
                new CostInterceptor(), //
                new DefaultHeadersInterceptor(), //
                new RenewalInterceptor(context), //
                // new RetryInterceptor(), // TODO
                new SetCookieInterceptor(context), //
                new FlowLimitInterceptor(limitStrategy), //
                new StatisticsInterceptor(), //
                new RedirectErrorInterceptor(), //
                new HttpDebugInterceptor() //
        );
        return interceptClient;
    }

    private void injectTaskConfig(FlowContext context, String taskConfigPath) {

        String config = ConfigUtil.getFromClassPath(taskConfigPath);
        TaskConfig taskConfig = JSON.parseObject(config, TaskConfig.class);
        taskConfig.checkAndInit();

        ScrambleTask scrambleTask = new ScrambleTask();
        scrambleTask.setId(1L);
        scrambleTask.setScrambleRealUserName(null);
        scrambleTask.setActiveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskConfig.getActiveTime(), new ParsePosition(0)));
        scrambleTask.setFromStation(taskConfig.getFromStation());
        scrambleTask.setToStation(taskConfig.getToStation());
        scrambleTask.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd").parse(taskConfig.getDepartureTime(), new ParsePosition(0)));
        List<String> trainCodes = new ArrayList<>();
        trainCodes.addAll(taskConfig.getTrainCodes());
        scrambleTask.setTrainCodes(trainCodes);
        List<Passenger> passengers = new ArrayList<>();
        for (TaskConfig.Passenger passenger : taskConfig.getPassengers()) {
            Passenger p = new Passenger();
            p.setKey(passenger.getKey());
            p.setTicketType(TicketType.getByCode(passenger.getTicketType()));
            p.setSeatType(SeatType.getByCode(passenger.getSeatType()));
            passengers.add(p);
        }
        scrambleTask.setPassengers(passengers);

        ScrambleContext scrambleContext = ScrambleContext.putNew(context);
        scrambleContext.setTask(scrambleTask);

        CandidateContext candidateContext = CandidateContext.putNew(context);
        candidateContext.setTask(scrambleTask);

        TaskConfig.Candidate candidate = taskConfig.getCandidate();
        if (candidate != null) {
            Map<String, SeatType> map = new HashMap<>();
            for (TaskConfig.CandidatePlan candidatePlan : candidate.getCandidatePlans()) {
                map.put(candidatePlan.getTrainCode(), SeatType.getByCode(candidatePlan.getSeatType()));
            }
            candidateContext.setCandidatePlans(map);
            candidateContext.setCashingCutoffMinuteBefore(candidate.getCashingCutoffMinuteBefore());
            candidateContext.setAcceptNewTrain(candidate.getAcceptNewTrain());
            candidateContext.setNewTrainTimeStart(candidate.getNewTrainTimeStart());
            candidateContext.setNewTrainTimeEnd(candidate.getNewTrainTimeEnd());
            candidateContext.setAcceptStand(candidate.getAcceptStand());
        }
    }

    private void initUserSession(FlowContext context) {
        // 登录用
        UserSession userSession = new UserSession();
        userSession.setLoginUser(context.getInteractionService().getLoginUser());
        userSession.setContext(context);
        userSessionService.putUserSession(context.getConnectionId(), userSession);
    }

    private void login(FlowContext context) throws ExecutionException, InterruptedException {
        CompletableFuture<?> loginFuture = null;
        if (Switch.login_by_password) {
            loginFuture = loginByPassword(context);
        } else {
            loginFuture = loginByQr(context);
        }

        loginFuture.get();
    }

    private void preload(FlowContext context) throws ExecutionException, InterruptedException {
        CompletableFuture<?> preloadFuture = PreLoader.load(context);
        preloadFuture.get();
    }

    private void executeTask(StopWatch stopWatch, FlowContext context) throws ExecutionException, InterruptedException {
        FlowContext executeContext = context.copyFrom();
        CompletableFuture<?> executeFuture = executeTask(executeContext, stopWatch);
        executeFuture.get();
    }

    private CompletableFuture<?> loginByQr(FlowContext context) {
        return doLogin(context, true);
    }

    private CompletableFuture<?> loginByPassword(FlowContext context) {
        return doLogin(context, false);
    }

    private CompletableFuture<?> doLogin(FlowContext context, boolean qrcode) {

        FlowPipeline pipeline = new FlowPipeline();

        pipeline.addHandler(new ConfigHandler());
        if (StringUtil.isNotBlank(cookieString)) {
            pipeline.addHandler(new MockLoginHandler(cookieString));
        } else {
            // qrcode login
            if (qrcode) {
                pipeline.addHandler(new CreateQr64Handler());
            }
            // password login
            else {
                pipeline.addHandler(new UamtkStaticHandler());
                pipeline.addHandler(new CheckLoginVerifyHandler());
                pipeline.addHandler(new GetMessageCodeHandler());
                pipeline.addHandler(new LoginHandler());
            }

            // real login
            pipeline.addHandler(new UserLoginHandler());
            pipeline.addHandler(new UamtkHandler());
            pipeline.addHandler(new UamauthClientHandler());
            pipeline.addHandler(new CheckLoginStatusHandler());
        }
        pipeline.addHandler(new CookieStringPrintHandler());

        FlowEngine engine = new FlowEngine(pipeline);
        return engine.execute(context);
    }

    private CompletableFuture<?> executeTask(FlowContext context, StopWatch stopWatch) {

        FlowPipeline pipeline = new FlowPipeline();

        pipeline.addHandler(new QueryXHandler());
        // TODO 这边开始，接口都要判断用户登录情况
        pipeline.addHandler(new CheckUserHandler());
        // TODO limit start
        if (Switch.flow_type.isCommon()) {
            pipeline.addHandler(new ScrambleTicketDispatcher());
        }
        else if (Switch.flow_type.isCandidate()) {
            pipeline.addHandler(new CandidateTicketDispatcher());
        }
        // limit end

        FlowEngine engine = new FlowEngine(pipeline);

        Date activeTime = ScrambleContext.get(context).getTask().getActiveTime();
        waitUntilExecute(activeTime, stopWatch);

        stopWatch.recordCurrent("start task time");

        return engine.execute(context);
    }

    private void waitUntilExecute(Date activeTime, StopWatch stopWatch) {
        long waitTime = activeTime.getTime() - System.currentTimeMillis();
        while (waitTime > 0) {
            try {
                stopWatch.recordTime("task wait", waitTime);
                Thread.sleep(waitTime);
                waitTime = activeTime.getTime() - System.currentTimeMillis();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // throw new RuntimeException(e);
            }
        }
    }
}
