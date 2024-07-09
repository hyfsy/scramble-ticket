
package com.scrambleticket.handler;

import java.util.ArrayList;
import java.util.List;

import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.client.cookie._embedded_cookie;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.StringUtil;

public class CheckLoginStatusHandler implements FlowHandler {

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {
        CookieStorage cookieStorage = context.getCookieStorage();
        List<_embedded_cookie.Pair> pairs = _embedded_cookie.getPairs();

        List<String> errorInfos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (_embedded_cookie.Pair pair : pairs) {
            String path = pair.getPath();
            String key = pair.getKey();
            String cookie = cookieStorage.getCookie(path, key);
            if (StringUtil.isBlank(cookie)) {
                errorInfos.add(key);
            } else {
                if (sb.length() != 0) {
                    sb.append("___");
                }
                sb.append(path).append("|").append(key).append("|").append(cookie);
            }
        }
        if (!errorInfos.isEmpty()) {
            throw new ScrambleTicketException(
                "cookie validate failed\nillegal cookie: " + String.join(",", errorInfos) + "\nother cookie: " + sb);
        }

        chain.handle(context);
    }

}
