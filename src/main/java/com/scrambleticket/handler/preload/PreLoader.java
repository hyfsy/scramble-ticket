
package com.scrambleticket.handler.preload;

import java.util.concurrent.CompletableFuture;

import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowPipeline;

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
