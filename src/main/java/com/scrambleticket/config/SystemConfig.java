
package com.scrambleticket.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.scrambleticket.util.ConfigUtil;

public class SystemConfig {

    private static final Map<String, String> configMap = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        String config = ConfigUtil.getFromClassPath("static/config/system.properties");
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        properties.forEach((k, v) -> configMap.put(String.valueOf(k), properties.getProperty(String.valueOf(k))));
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(configMap.get(key));
    }

    public static String getStr(String key) {
        return configMap.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(configMap.get(key));
    }
}
