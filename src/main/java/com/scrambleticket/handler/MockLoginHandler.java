
package com.scrambleticket.handler;

import java.util.Collections;
import java.util.Map;

import com.scrambleticket.client.cookie.CookieStorage;
import com.scrambleticket.client.cookie._embedded_cookie;
import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.flow.FlowHandler;
import com.scrambleticket.util.HttpUtils;

/**
 *
 * TODO _uab_collina ?
 * <p>
 * String s = "Cookie: _uab_collina=171524823772866925881772; JSESSIONID=4B30C73041F787C1549C23BE477A583F;
 * tk=VMdFju6KUbBD6XLC0VePfeHjmhNwoPUnxhh1h0; _jc_save_fromStation=%u4E0A%u6D77%2CSHH;
 * _jc_save_toStation=%u5E38%u5DDE%2CCZH; _jc_save_toDate=2024-05-09; _jc_save_wfdc_flag=dc;
 * route=495c805987d0f5c8c84b14f60212447d; BIGipServerotn=1675165962.50210.0000;
 * BIGipServerpassport=820510986.50215.0000; guidesStatus=off; highContrastMode=defaltMode; cursorStatus=off;
 * uKey=4aa75764e3ab09a99d4a16519989d87115640354f9696fe8f1c9c477b6c84224; _jc_save_fromDate=2024-05-18"
 */
public class MockLoginHandler implements FlowHandler {

    String cookieString;

    public MockLoginHandler(String cookieString) {
        cookieString = cookieString.substring("Cookie: ".length());
        this.cookieString = cookieString;
    }

    @Override
    public void handle(FlowContext context, FlowHandlerChain chain) {

        CookieStorage cookieStorage = context.getCookieStorage();

        Map<String, Map<String, String>> cookieStringMap =
            HttpUtils.parseCookie(Collections.singletonList(cookieString));
        Map<String, String> newCookieMap = cookieStringMap.get("/"); // header里面的没有path，所以此处统一/

        for (_embedded_cookie.Pair pair : _embedded_cookie.getPairs()) {
            setCookie(cookieStorage, newCookieMap, pair.getPath(), pair.getKey());
        }

        chain.handle(context);
    }

    private void setCookie(CookieStorage cookieStorage, Map<String, String> cookieMap, String path, String key) {
        String value = cookieMap.get(key);
        cookieStorage.setCookie(path, key, value);
    }
}
