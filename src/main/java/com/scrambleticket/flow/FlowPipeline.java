
package com.scrambleticket.flow;

import java.util.ArrayList;
import java.util.List;

public class FlowPipeline {

    public List<FlowHandler> handlers = new ArrayList<>();

    public FlowPipeline addHandler(FlowHandler flowHandler) {
        handlers.add(flowHandler);
        return this;
    }

    public List<FlowHandler> getHandlers() {
        return handlers;
    }
}
