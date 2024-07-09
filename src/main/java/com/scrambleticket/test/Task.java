
package com.scrambleticket.test;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowPipeline;
import com.scrambleticket.handler.CheckUserHandler;
import com.scrambleticket.handler.QueryXHandler;
import com.scrambleticket.handler.ScrambleTicketDispatcher;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.service.UserSession;
import com.scrambleticket.service.UserSessionService;

public class Task implements Runnable {

    UserSessionService userSessionService;
    ScrambleTask task;

    CompletableFuture<?> future;

    public Task(UserSessionService userSessionService, ScrambleTask task) {
        this.userSessionService = userSessionService;
        this.task = task;
    }

    @Override
    public void run() {
        waitUntilExecute();

        UserSession userSession = userSessionService.getUserSessionByLoginUserName(task.getScrambleRealUserName());
        if (userSession == null) {
            userSession = userSessionService.getRandomUserSession();
        }
        if (userSession == null) {
            throw new ScrambleTicketException(
                "no user has login: [" + task.getScrambleRealUserName() + "], taskId: " + task.getId());
        }

        // 忽略原有attributes
        FlowContext context = userSession.getContext().copyFrom();
        // 用户抢票流程的上下文
        ScrambleContext scrambleContext = ScrambleContext.putNew(context);
        scrambleContext.setTask(task);

        FlowPipeline pipeline = new FlowPipeline();

        pipeline.addHandler(new QueryXHandler());
        // TODO 这边开始，接口都要判断用户登录情况
        pipeline.addHandler(new CheckUserHandler());
        // TODO limit start
        pipeline.addHandler(new ScrambleTicketDispatcher());
        // limit end

        FlowEngine engine = new FlowEngine(pipeline);
        this.future = engine.execute(context);

    }

    private void waitUntilExecute() {
        long waitTime = task.getActiveTime().getTime() - new Date().getTime();
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            // throw new RuntimeException(e);
        }
    }
}
