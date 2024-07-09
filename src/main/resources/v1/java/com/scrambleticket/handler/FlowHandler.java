
package com.scrambleticket.handler;

public interface FlowHandler<REQ, RESP> {

    RESP handle(REQ request);

}
