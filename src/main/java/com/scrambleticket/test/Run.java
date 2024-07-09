
package com.scrambleticket.test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import com.scrambleticket.Constants;
import com.scrambleticket.client.Client;
import com.scrambleticket.client.DefaultClient;
import com.scrambleticket.client.Interceptors;
import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.client.interceptor.*;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowPipeline;
import com.scrambleticket.handler.*;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.limit.LimitStrategy;
import com.scrambleticket.limit.SimpleLimitStrategy;
import com.scrambleticket.model.Passenger;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.performance.Cost;
import com.scrambleticket.service.*;
import com.scrambleticket.util.StringUtil;

public class Run {

    static AtomicInteger connectionIdGenerator = new AtomicInteger();

    Client client = new DefaultClient(Constants.HOST, Constants.PORT);
    UserSessionService userSessionService = new DefaultUserSessionService();
    LimitStrategy limitStrategy = new SimpleLimitStrategy();

    boolean login_by_password = Switch.login_by_password;

    String cookieString =
        //
            "Cookie: uamtk=oBhUVzONwViUdHoJbZ4zpqsVfW3M2u-Vijh1h0; _passport_session=90cd65364e9c4ae7bb861928f3299dd96070; tk=8yS_gZwdiMEVjZJ7-AfKysftD88v2eq3koh1h0; JSESSIONID=D1CAD303D8D6A2F5CFC3119158443ABF; highContrastMode=defaltMode; route=9036359bb8a8a461c164a04f8f50b252; BIGipServerotn=1708720394.24610.0000; BIGipServerpassport=870842634.50215.0000; guidesStatus=off; uKey=360a90b5b1ea4373bca56c02771559a62213df815732bd048c0dc004a9ec4867; cursorStatus=off\n"
    //
    ;

    String 今天 = 今天(); // "2024-05-20";
    String 提前15天的时间 = 提前15天的时间(); // "2024-06-03";

    String _1点30 = " 13:30:00";
    String _1点30车次 = "G7588"; // G7726 G7588
    String _2点30 = " 14:30:00";
    String _2点30车次 = "G7026";
    String _8点30 = " 8:30:00";
    String _8点30车次 = "G7267";
    String 现在 = 现在();
    String 今天13点30分 = 今天 + _1点30;
    String 今天14点30分 = 今天 + _2点30;
    String 今天8点30分 = 今天 + _8点30;
    String 今天现在 = 今天 + 现在;

    String 任务时间 = 今天13点30分;
    String 车次时间 = 提前15天的时间;
    String 车次 = 任务时间 == 今天13点30分 ? _1点30车次 : 任务时间 == 今天14点30分 ? _2点30车次 : 任务时间 == 今天8点30分 ? _8点30车次 : _2点30车次;
    boolean 上海到常州 = 车次 == _1点30车次 || 车次 == _2点30车次;

    public static void main(String[] args) throws Exception {

        new Run().execute();

    }

    private static String 今天() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private static String 现在() {
        return new SimpleDateFormat(" HH:mm:ss").format(new Date());
    }

    private static String 提前15天的时间() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 14);
        Date time = now.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    public void execute() throws Exception {

        StopWatch stopWatch = new StopWatch();

        int sessionId = connectionIdGenerator.incrementAndGet();

        // 一个用户一个context
        FlowContext context = new FlowContext();
        Client interceptClient = Interceptors.intercept(client, //
            new CostInterceptor(context), //
            new DefaultHeadersInterceptor(), //
            new RenewalInterceptor(context), //
            // new RetryInterceptor(), // TODO
            new SetCookieInterceptor(context), //
            new FlowLimitInterceptor(limitStrategy), //
            new StatisticsInterceptor(), //
            new RedirectErrorInterceptor(), //
            new HttpDebugInterceptor(context) //
        );
        Client realClient = interceptClient;
        context.setClient(realClient);
        context.setConnectionId(sessionId);
        context.setCookieStorage(new CookieStorage().setDefaultCookie());
        // 每个用户一个
        InteractionService interactionService = new MockInteractionService(userSessionService, sessionId);
        context.setInteractionService(interactionService);

        CompletableFuture<?> loginFuture = null;
        if (login_by_password) {
            loginFuture = loginByPassword(context);
        } else {
            loginFuture = loginByQr(context);
        }

        loginFuture.get();

        stopWatch.record("login time");

        UserSession userSession = new UserSession();
        userSession.setLoginUser(context.getInteractionService().getLoginUser());
        userSession.setContext(context);
        userSessionService.putUserSession(sessionId, userSession);

        ScrambleTask scrambleTask = new ScrambleTask();
        scrambleTask.setId(1L);
        scrambleTask.setScrambleRealUserName(null);
        Date activeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(任务时间, new ParsePosition(0));

        scrambleTask.setActiveTime(activeTime);
        scrambleTask.setFromStation(上海到常州 ? "上海" : "常州");
        scrambleTask.setToStation(上海到常州 ? "常州" : "上海");
        scrambleTask.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd").parse(车次时间, new ParsePosition(0)));
        List<String> trainCodes = new ArrayList<>();
        trainCodes.add(车次);
        scrambleTask.setTrainCodes(trainCodes);
        List<Passenger> passengers = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setName("何映峰");
        passenger.setPassenger_id_no(null);
        passenger.setKey(
            "何映峰_2dcb3f1fd997c1ac3c1384d5e9425af93acd655663abe115d07f0afb26ac8ef550cac39d64fca2c8541c1e7265c9d0af8f41855fba4a0dd7f0218ce69d046a8c4a76746eb2a05700c60e204a01a89ea8443bbd37894cc28911a60e93cf6ba535");
        passengers.add(passenger);
        scrambleTask.setPassengers(passengers);

        ScrambleContext scrambleContext = ScrambleContext.putNew(context);
        scrambleContext.setTask(scrambleTask);

        CompletableFuture<?> preloadFuture = PreLoader.load(context.copyFrom(true));

        preloadFuture.get();

        stopWatch.record("preload time");

        // 限流重置，下面流程的6个接口保证仅限制一次
        Thread.sleep(1000L);

        stopWatch.record("limit wait time");

        Cost.getInstance().enable();

        CompletableFuture<?> executeFuture = executeTask(context.copyFrom(), scrambleTask, stopWatch);

        ((CompletableFuture<FlowContext>) executeFuture).whenComplete(new BiConsumer<FlowContext, Throwable>() {
            @Override
            public void accept(FlowContext ctx, Throwable t) {
                HttpDebugInterceptor.DebugInfo instance = HttpDebugInterceptor.DebugInfo.getInstance(context);
                instance.print();
                instance.release();
                HttpDebugInterceptor.DebugInfo.removeInstance(context);
            }
        });

        executeFuture.get();

        stopWatch.recordCurrent("stop task time");
        stopWatch.recordPass("task processed time");

        realClient.close();
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

    private CompletableFuture<?> executeTask(FlowContext context, ScrambleTask task, StopWatch stopWatch) {

        FlowPipeline pipeline = new FlowPipeline();

        pipeline.addHandler(new QueryXHandler());
        // TODO 这边开始，接口都要判断用户登录情况
        pipeline.addHandler(new CheckUserHandler());
        // TODO limit start
        pipeline.addHandler(new ScrambleTicketDispatcher());
        // limit end

        FlowEngine engine = new FlowEngine(pipeline);

        waitUntilExecute(task, stopWatch);

        stopWatch.recordCurrent("start task time");

        return engine.execute(context);
    }

    private void waitUntilExecute(ScrambleTask task, StopWatch stopWatch) {
        long waitTime = task.getActiveTime().getTime() - System.currentTimeMillis();
        while (waitTime > 0) {
            try {
                stopWatch.recordTime("task wait", waitTime);
                Thread.sleep(waitTime);
                waitTime = task.getActiveTime().getTime() - System.currentTimeMillis();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // throw new RuntimeException(e);
            }
        }
    }
}
