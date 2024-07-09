
package com.scrambleticket.flow;

public class ContextCompletionHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        context.done();
    }
}
