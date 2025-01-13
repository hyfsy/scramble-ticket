
package com.scrambleticket.handler.scramble.candidate;

import java.util.ArrayList;
import java.util.List;

import com.scrambleticket.Logger;
import com.scrambleticket.config.Switch;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.handler.context.CandidateContext;
import com.scrambleticket.handler.context.ScrambleContext;
import com.scrambleticket.handler.context.TrainScrambleContext;
import com.scrambleticket.handler.pay.PayOrderInitHandler;
import com.scrambleticket.handler.scramble.common.GetPassengerDTOsHandler;

public class CandidateTicketDispatcher implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        ScrambleContext scrambleContext = ScrambleContext.get(context);
        CandidateContext candidateContext = CandidateContext.get(context);
        String trainCode = candidateContext.getFirstPlanTrainCode();

        List<FlowHandler> flowHandlers = new ArrayList<>();
        flowHandlers.add(new CheckAllCandidateHandler());
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

        // 其他流程会用到
        TrainScrambleContext trainScrambleContext = new TrainScrambleContext();
        trainScrambleContext.setTrainCode(trainCode);
        trainScrambleContext.setScrambleContext(scrambleContext);
        FlowContext newContext = context.copyFrom();
        newContext.putAttribute(chain.toString(), trainScrambleContext);
        newContext.setFuture(context.getFuture());
        chain.handle(newContext);
    }

}
