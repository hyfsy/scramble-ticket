
package com.scrambleticket.service;

import com.scrambleticket.entity.Config;
import com.scrambleticket.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class DefaultTaskService implements TaskService {

    ConfigService configService;

    public DefaultTaskService(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<Task> list() {
        return new ArrayList<>(configService.getConfig().getTasks().values());
    }

    @Override
    public Task get(Long id) {
        return configService.getConfig().getTasks().get(id);
    }

    @Override
    public int update(Task task) {
        Config config = configService.getConfig();
        config.getTasks().put(task.getId(), task);
        return configService.updateConfig(config) ? 1 : 0;
    }

    @Override
    public int delete(Long id) {
        Config config = configService.getConfig();
        config.getTasks().remove(id);
        return configService.updateConfig(config) ? 1 : 0;
    }
}
