
package com.scrambleticket.handler;

import com.scrambleticket.Logger;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.test.Switch;

public class CookieStringPrintHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        if (Switch.log_cookie_string) {
            String cookieString = context.getCookieStorage().getCookieString();
            Logger.info("Cookie: " + cookieString);
        }
        chain.handle(context);
    }

}
