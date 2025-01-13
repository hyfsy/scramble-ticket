
package com.scrambleticket.handler.scramble.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.scrambleticket.Logger;
import com.scrambleticket.config.Switch;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.handler.pay.PayOrderInitHandler;
import com.scrambleticket.handler.scramble.common.GetPassengerDTOsHandler;

public class CandidateTicketDispatcher implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);

        List<String> trainCodes = scrambleContext.getTask().getTrainCodes();

        AtomicInteger latch = new AtomicInteger(trainCodes.size());
        Runnable callback = () -> chain.handle(context);

        List<FlowHandler> flowHandlers = new ArrayList<>();
        flowHandlers.add(new ChechFaceHandler());
        flowHandlers.add(new CandidateSubmitOrderRequestHandler());
        flowHandlers.add(new GetPassengerDTOsHandler());
        flowHandlers.add(new PassengerInitApiHandler());
        flowHandlers.add(new QuerySuccessRateHandler());
        flowHandlers.add(new GetQueueNumHandler());
        if (Switch.candidate_commit_order_real) {
            flowHandlers.add(new ConfirmHBHandler());
            flowHandlers.add(new QueryQueueHandler());
            if (Switch.flow_type.isPay()) {
                flowHandlers.add(new PayOrderInitHandler());
                // flowHandlers.add(new PayCheckHandler());
                // flowHandlers.add(new PayGatewayHandler());
            }
            flowHandlers.add(new FlowHandler() {
                @Override
                public void handle(FlowContext context, FlowHandlerChain chain) {
                    Logger.info("执行完毕，请尽快支付！");
                    chain.handle(context);
                }
            });
        }

        flowHandlers.add(new AwaitForCallbackTriggerHandler(latch, callback));

        // TODO 改为链式的调用，非并发调用
        for (String trainCode : trainCodes) {

            ScrambleTicketFlowHandlerChain scrambleTicketChain = new ScrambleTicketFlowHandlerChain(flowHandlers);

            TrainScrambleContext trainScrambleContext = new TrainScrambleContext();
            trainScrambleContext.setTrainCode(trainCode);
            trainScrambleContext.setScrambleContext(scrambleContext);
            FlowContext newContext = context.copyFrom();
            newContext.putAttribute(scrambleTicketChain.toString(), trainScrambleContext);
            newContext.setFuture(context.getFuture());
            scrambleTicketChain.handle(newContext);

        }
    }

    private static class ScrambleTicketFlowHandlerChain extends VirtualFlowHandlerChain {

        public ScrambleTicketFlowHandlerChain(List<FlowHandler> filters) {
            super(filters);
        }
    }

    private static class AwaitForCallbackTriggerHandler implements FlowHandler {
        AtomicInteger latch;
        Runnable callback;

        public AwaitForCallbackTriggerHandler(AtomicInteger latch, Runnable callback) {
            this.latch = latch;
            this.callback = callback;
        }

        @Override
        public void handle(FlowContext context, FlowHandlerChain chain) {
            int remain = latch.decrementAndGet();
            if (remain == 0) {
                callback.run();
            }
        }
    }
}
