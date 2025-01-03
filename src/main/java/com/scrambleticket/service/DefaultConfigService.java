
package com.scrambleticket.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.scrambleticket.entity.Config;
import com.scrambleticket.exception.ScrambleTicketException;
import com.scrambleticket.util.StringUtil;

public class DefaultConfigService implements ConfigService {

    static final int VERSION = 1;
    File configFile =
        new File(String.join(File.separator, System.getProperty("user.home"), ".scramble-ticket", "static/config/config_test.json"));
    volatile String cache;
    volatile AtomicLong revision = new AtomicLong(-1L);
    Lock lock = new ReentrantLock();
    long persistInterval = 2000L;
    ScheduledExecutorService scheduledExecutorService;

    {
        init();
    }

    public void init() {
        initConfig();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(this::persistConfig, persistInterval, persistInterval,
            TimeUnit.MILLISECONDS);
    }

    public void destroy() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        if (cache != null) {
            saveFileContent(cache);
        }
    }

    @Override
    public Config getConfig() {
        // deep copy
        return JSONObject.parseObject(cache, Config.class);
    }

    @Override
    public boolean updateConfig(Config config) {
        long l = System.currentTimeMillis();
        if (!revision.compareAndSet(config.getLastModified(), l)) {
            return false;
        }

        config.setLastModified(l);
        this.cache = JSONObject.toJSONString(config);
        return true;
    }

    private String initConfig() {
        if (cache == null) {
            lock.lock();
            try {
                if (cache == null) {
                    String content = loadFileContent();
                    Config config;
                    if (StringUtil.isBlank(content)) {
                        config = new Config();
                        config.setVersion(VERSION);
                        saveFileContent(JSONObject.toJSONString(config, JSONWriter.Feature.PrettyFormat));
                    } else {
                        config = JSONObject.parseObject(content, Config.class);
                    }
                    this.revision = new AtomicLong(config.getLastModified());
                    this.cache = config.toString();
                }
            } finally {
                lock.unlock();
            }
        }
        return cache;
    }

    private void persistConfig() {
        saveFileContent(cache);
    }

    private String loadFileContent() {
        try {
            byte[] bytes = Files.readAllBytes(configFile.toPath());
            return new String(bytes);
        } catch (IOException e) {
            throw new ScrambleTicketException("loadFileContent failed", e);
        }
    }

    private void saveFileContent(String content) {
        try {
            Files.write(configFile.toPath(), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ScrambleTicketException("saveFileContent failed", e);
        }
    }
}
