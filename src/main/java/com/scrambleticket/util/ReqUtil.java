
package com.scrambleticket.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ReqUtil {

    static HttpServletRequest request = null;

    private static final String FORWARDED_HEADER = "Forwarded"; // rfc7239 standard
    private static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    private static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";
    private static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    private static final String HOST_HEADER = "Host";

    public static String getRealFor() {
        return getRealX(X_FORWARDED_FOR_HEADER, Function.identity(), () -> request.getRemoteAddr());
    }

    public static String getRealHost() {
        return getRealX(X_FORWARDED_HOST_HEADER, Function.identity(), () -> request.getHeader(HOST_HEADER));
    }

    public static int getRealPort() {
        return getRealX(X_FORWARDED_PORT_HEADER, Integer::parseInt, () -> request.getRemotePort());
    }

    public static String getRealProtocol() {
        return getRealX(X_FORWARDED_PROTO_HEADER, Function.identity(), () -> request.getProtocol());
    }

    private static <T> T getRealX(String header, Function<String, T> mapper, Supplier<T> fallback) {
        List<String> values = Collections.list(request.getHeaders(header));
        if (CollectionUtils.isEmpty(values)) {
            return fallback.get();
        }

        List<String> ports = values.stream().map(StringUtils::commaDelimitedListToStringArray).flatMap(Arrays::stream)
            .collect(Collectors.toList());
        return mapper.apply(ports.get(0));
    }

}
