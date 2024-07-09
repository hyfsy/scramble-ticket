
package com.scrambleticket.util;

import java.util.*;

import com.scrambleticket.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

public class HttpUtils {

    private static final AsciiString X_WWW_FORM_URLENCODED =
        AsciiString.cached("application/x-www-form-urlencoded; charset=UTF-8");

    private static final List<String> IGNORED_PARAMS = Arrays.asList("Comment", "Discard", "Domain", "Expires",
        "Max-Age", "Path", "Secure", "Version", "HTTPOnly", "path");

    // 大文件用zstd，小文件用br
    // 压缩比率: zstd=br > deflate > gzip > snappy
    // 解压缩速度: snappy > zstd=br > gzip > deflate
    // netty: br > zstd > snappy > gzip > deflate
    // netty的decompressor不支持zstd
    // 网站仅支持gzip
    // chrome: gzip, deflate, br, zstd, snappy
    private static final AsciiString SUPPORT_ACCEPT_ENCODING = AsciiString.cached("gzip, deflate, br, snappy");
    private static final AsciiString GZIP = AsciiString.cached("gzip");

    public static Map<String, Map<String, String>> parseCookie(List<String> cookie) {
        if (CollectionUtil.isEmpty(cookie)) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> cookieMap = new HashMap<>();
        for (String string : cookie) {
            Map<String, String> cookies = new HashMap<>();
            for (String s : string.split(";")) {
                String[] split = s.trim().split("=");
                if (split.length == 2) {
                    cookies.put(split[0], split[1]);
                } else if (split.length == 1) {
                    cookies.put(split[0], "");
                } else {
                    Logger.warn("unknown cookie format, ignored: " + s);
                }
            }
            String path = cookies.get("Path");
            if (path == null) {
                path = cookies.get("path");
            }
            if (path == null) {
                path = "/";
            }
            IGNORED_PARAMS.forEach(cookies::remove);
            cookieMap.putIfAbsent(path, new HashMap<>());
            cookieMap.get(path).putAll(cookies);
        }
        return cookieMap;
    }

    public static void setFormUrlEncodedContentType(HttpMessage message) {
        message.headers().add(HttpHeaderNames.CONTENT_TYPE, X_WWW_FORM_URLENCODED);
    }

    public static void setCacheDisabled(HttpMessage message) {
        message.headers().add(HttpHeaderNames.IF_MODIFIED_SINCE, HttpHeaderValues.ZERO);
        message.headers().add(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);
    }

    public static void setAcceptEncoding(HttpMessage message) {
        message.headers().add(HttpHeaderNames.ACCEPT_ENCODING, SUPPORT_ACCEPT_ENCODING);
    }

    /**
     * @deprecated 需手动指定编码
     */
    @Deprecated
    public static void setContentEncoding(HttpMessage message) {
        message.headers().set(HttpHeaderNames.CONTENT_ENCODING, GZIP);
    }

    public static FullHttpRequest createGetRequest(String uri) {
        return createRequest(HttpMethod.GET, uri, createHeaders(), Unpooled.EMPTY_BUFFER);
    }

    public static FullHttpRequest createPostRequest(String uri) {
        return createRequest(HttpMethod.POST, uri, createHeaders(), Unpooled.EMPTY_BUFFER);
    }

    public static FullHttpRequest createPostRequest(String uri, ByteBuf body) {
        return createRequest(HttpMethod.POST, uri, createHeaders(), body);
    }

    public static FullHttpRequest createPostRequest(String uri, HttpHeaders headers) {
        return createRequest(HttpMethod.POST, uri, headers, Unpooled.EMPTY_BUFFER);
    }

    public static FullHttpRequest createPostRequest(String uri, HttpHeaders headers, ByteBuf body) {
        return createRequest(HttpMethod.POST, uri, headers, body);
    }

    public static FullHttpRequest createRequest(HttpMethod method, String uri, HttpHeaders headers, ByteBuf body) {
        return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, uri, body, headers, EmptyHttpHeaders.INSTANCE);
    }

    private static HttpHeaders createHeaders() {
        return new DefaultHttpHeaders(false);
    }
}
