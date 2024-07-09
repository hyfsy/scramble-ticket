
package com.scrambleticket.test;

import java.util.concurrent.CompletableFuture;

import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowPipeline;
import com.scrambleticket.handler.PassengersQueryHandler;
import com.scrambleticket.handler.StationNameJsHandler;

public class PreLoader {

    public static CompletableFuture<?> load(FlowContext context) {

        // passengers
        FlowPipeline pipeline = new FlowPipeline();
        pipeline.addHandler(new StationNameJsHandler());
        pipeline.addHandler(new PassengersQueryHandler()); // TODO 用户登录校验
        FlowEngine engine = new FlowEngine(pipeline);
        return engine.execute(context);
    }
}
