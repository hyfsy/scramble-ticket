
package com.scrambleticket.handler;

import java.util.concurrent.CompletableFuture;

import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.exception.TicketExceedException;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowEngine;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.flow.FlowPipeline;
import com.scrambleticket.handler.scramble.candidate.CandidateTicketDispatcher;

// TODO wait check
public class NoTicketToWaitTicketHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        context.getFuture().exceptionally(t -> {
            Throwable cause = t;
            while (cause instanceof ScrambleTicketException) {
                if (cause instanceof TicketExceedException) {
                    FlowPipeline pipeline = new FlowPipeline();
                    pipeline.addHandler(new CandidateTicketDispatcher());
                    FlowEngine engine = new FlowEngine(pipeline);
                    FlowContext newContext = context.copyFrom();
                    CompletableFuture<?> newFuture = engine.execute(context);
                    break;
                }
                cause = cause.getCause();
            }
            return context;
        });
        // if (context.getFuture().isCompletedExceptionally()) {
        //     try {
        //         context.getFuture().get();
        //     } catch (InterruptedException ignored) {
        //     } catch (ExecutionException e) {
        //         Throwable cause = e.getCause();
        //         while (cause instanceof ScrambleTicketException) {
        //             if (cause instanceof TicketExceedException) {
        //                 context.setFuture(new CompletableFuture<>());
        //                 ChechFaceHandler handler = new ChechFaceHandler();
        //                 handler.handle(context, chain);
        //                 break;
        //             }
        //             cause = cause.getCause();
        //         }
        //     }
        // }
    }
}
