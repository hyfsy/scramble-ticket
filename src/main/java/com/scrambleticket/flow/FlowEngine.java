
package com.scrambleticket.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FlowEngine {

    private final FlowPipeline pipeline;
    private final FlowHandler.FlowHandlerChainManager manager;

    public FlowEngine(FlowPipeline pipeline) {
        this.pipeline = pipeline;
        List<FlowHandler> flowHandlers = new ArrayList<>();
        flowHandlers.addAll(pipeline.getHandlers());
        flowHandlers.add(new ContextCompletionHandler());
        this.manager = new FlowHandler.FlowHandlerChainManager(flowHandlers);
    }

    public CompletableFuture<?> execute(FlowContext context) {
        this.manager.handle(context);
        return context.getFuture();
    }

    public FlowPipeline getPipeline() {
        return pipeline;
    }
}
