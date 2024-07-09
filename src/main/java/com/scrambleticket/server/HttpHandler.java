
package com.scrambleticket.server;

import com.scrambleticket.Logger;
import com.scrambleticket.entity.Passenger;
import com.scrambleticket.entity.Task;
import com.scrambleticket.service.*;
import com.scrambleticket.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpHandler {

    ConfigService configService = new DefaultConfigService();
    PassengerService passengerService = new DefaultPassengerService(configService);
    TaskService taskService = new DefaultTaskService(configService);

    byte[] html;
    byte[] js;

    {
        html = loadResource("i.html");
        js = loadResource("t.js");
    }

    public FullHttpResponse handle(FullHttpRequest request) {
        try {
            String uri = request.uri();
            if (uri.equals("") || uri.equals("/")) {
                return successRaw(html);
            } else if (uri.equals("/t.js")) {
                return successRaw(js);
            } else if (uri.startsWith("/p/l")) {
                return success(passengerService.list());
            } else if (uri.startsWith("/p/g")) {
                return success(passengerService.get(Long.valueOf(uri.substring(4))));
            } else if (uri.startsWith("/p/u")) {
                return success(passengerService.update(ByteBufUtil.toObject(request.content(), Passenger.class)));
            } else if (uri.startsWith("/p/d")) {
                return success(passengerService.delete(Long.valueOf(uri.substring(4))));
            } else if (uri.startsWith("/t/l")) {
                return success(taskService.list());
            } else if (uri.startsWith("/t/g")) {
                return success(taskService.get(Long.valueOf(uri.substring(4))));
            } else if (uri.startsWith("/t/u")) {
                return success(taskService.update(ByteBufUtil.toObject(request.content(), Task.class)));
            } else if (uri.startsWith("/t/d")) {
                return success(taskService.delete(Long.valueOf(uri.substring(4))));
            } else {
                return error("invalid url");
            }
        } catch (Exception e) {
            Logger.error("request handle failed", e);
            return error("请求失败，请联系管理员");
        }
    }

    private FullHttpResponse error(String m) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
            HttpResponseStatus.INTERNAL_SERVER_ERROR, ByteBufUtil.createObject(new R().setC(1).setM(m)),
            EmptyHttpHeaders.INSTANCE, EmptyHttpHeaders.INSTANCE);
        return response;
    }

    private FullHttpResponse successRaw(byte[] bytes) {
        return success(EmptyHttpHeaders.INSTANCE, ByteBufUtil.create(bytes));
    }

    private FullHttpResponse success(Object d) {
        return success(EmptyHttpHeaders.INSTANCE, ByteBufUtil.createObject(new R().setC(1).setD(d)));
    }

    private FullHttpResponse success(HttpHeaders headers, ByteBuf body) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
            body, headers, EmptyHttpHeaders.INSTANCE);
        return response;
    }

    private byte[] loadResource(String resource) {
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
            ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            if (is == null) {
                return new byte[0];
            }
            int len;
            byte[] bytes = new byte[8192];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            Logger.error("read resource failed: " + resource, e);
            return new byte[0];
        }
    }

    @Data
    @Accessors(chain = true)
    public static class R {
        int c;
        String m;
        Object d;
    }
}
