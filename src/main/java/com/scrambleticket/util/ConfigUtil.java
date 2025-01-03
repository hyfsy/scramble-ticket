
package com.scrambleticket.util;

import java.io.IOException;
import java.io.InputStream;

public class ConfigUtil {

    public static String getFromClassPath(String path) {
        byte[] bytes;
        try (InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("config path not found: " + path);
            }
            try {
                bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }
}
