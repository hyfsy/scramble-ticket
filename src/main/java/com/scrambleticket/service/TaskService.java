
package com.scrambleticket.service;

import java.util.List;

import com.scrambleticket.entity.Task;

public interface TaskService {
    List<Task> list();

    Task get(Long id);

    int update(Task task);

    int delete(Long id);
}
