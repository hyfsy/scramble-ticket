
package com.scrambleticket.flow;

import java.util.List;

import com.scrambleticket.Constants;

public interface FlowHandler extends Constants {

    void handle(FlowContext context, FlowHandlerChain chain);

    interface FlowHandlerChain {
        void handle(FlowContext context);
    }

    class VirtualFlowHandlerChain implements FlowHandlerChain {

        private final List<FlowHandler> filters;
        private int pos;

        public VirtualFlowHandlerChain(List<FlowHandler> filters) {
            this.filters = filters;
        }

        @Override
        public void handle(FlowContext context) {
            if (this.pos != this.filters.size()) {
                this.pos++;
                FlowHandler nextFilter = this.filters.get(this.pos - 1);
                nextFilter.handle(context, this);
            }
        }
    }

    class FlowHandlerChainManager implements FlowHandlerChain {

        private final List<FlowHandler> filters;

        public FlowHandlerChainManager(List<FlowHandler> filters) {
            this.filters = filters;
        }

        @Override
        public void handle(FlowContext context) {
            new VirtualFlowHandlerChain(filters).handle(context);
        }
    }
}
