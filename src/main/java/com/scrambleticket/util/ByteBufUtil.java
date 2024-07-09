
package com.scrambleticket.util;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufUtil {

    public static ByteBuf create(byte[] bytes) {
        return Unpooled.copiedBuffer(bytes);
    }

    public static ByteBuf create(String msg) {
        return Unpooled.copiedBuffer(msg.getBytes(StandardCharsets.UTF_8));
        // ByteBuf urlByteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        // urlByteBuf.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        // return urlByteBuf;
    }

    public static ByteBuf createObject(Object obj) {
        return create(JSONObject.toJSONString(obj));
    }

    public static CompositeByteBuf compose(ByteBuf... byteBufs) {
        CompositeByteBuf compositeByteBuf = new CompositeByteBuf(ByteBufAllocator.DEFAULT, false, 0);
        for (ByteBuf byteBuf : byteBufs) {
            compositeByteBuf.addComponent(byteBuf);
        }
        return compositeByteBuf;
    }

    public static void reuse(ByteBuf... byteBufs) {
        for (ByteBuf byteBuf : byteBufs) {
            byteBuf.resetReaderIndex();
        }
    }

    public static String toString(ByteBuf buf) {
        return buf.toString(CharsetUtil.UTF_8);
    }

    public static JSONObject toJSONObject(ByteBuf buf) {
        return JSONObject.parseObject(toString(buf));
    }

    public static <T> T toObject(ByteBuf buf, Class<T> clazz) {
        return JSONObject.parseObject(toString(buf), clazz);
    }
}
