
package com.scrambleticket.client.cookie;

import com.scrambleticket.util.CompositeMap;
import com.scrambleticket.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieStorage {

    private final Map<String, Map<String, String>> cookies = new ConcurrentHashMap<>();

    public Map<String, Map<String, String>> getCookies() {
        return Collections.unmodifiableMap(cookies);
    }

    public CookieStorage setDefaultCookie() {
        for (_embedded_cookie.Pair pair : _embedded_cookie.getDefaultPairs()) {
            setCookie(pair.getPath(), pair.getKey(), pair.getValue());
        }
        return this;
    }

    public CookieStorage setCookie(String path, String key, String value) {
        cookies.putIfAbsent(path, new ConcurrentHashMap<>());
        Map<String, String> cookie = cookies.get(path);
        if (isRemovalValue(value)) {
            cookie.remove(key);
        } else {
            cookie.put(key, value);
        }
        return this;
    }

    public CookieStorage setCookies(String path, Map<String, String> values) {
        cookies.putIfAbsent(path, new ConcurrentHashMap<>());
        Map<String, String> cookie = cookies.get(path);
        values.forEach((key, value) -> {
            if (isRemovalValue(value)) {
                cookie.remove(key);
            } else {
                cookie.put(key, value);
            }
        });
        return this;
    }

    public CookieStorage setCookies(Map<String, Map<String, String>> cookies) {
        for (Map.Entry<String, Map<String, String>> entry : cookies.entrySet()) {
            setCookies(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public String getCookie(_embedded_cookie.Cookie cookie) {
        return getCookie(cookie.getPath(), cookie.getKey());
    }

    public String getCookie(String path, String key) {
        return getCookies(path).get(key);
    }

    public Map<String, String> getCookies(String path) {
        List<Map<String, String>> map = new ArrayList<>();
        cookies.forEach((k, v) -> {
            if ("*".equals(path) || path.startsWith(k)) {
                map.add(v);
            }
        });
        return new CompositeMap<>(map);
    }

    public String getCookieString(String path) {

        Map<String, String> cookies = getCookies(path);

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }

    public String getCookieString() {
        return getCookieString("*");
    }

    private boolean isRemovalValue(String value) {
        return value == null || StringUtil.isBlank(value) || "\"\"".equals(value);
    }
}
