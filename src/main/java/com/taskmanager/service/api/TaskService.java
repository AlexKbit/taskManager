package com.taskmanager.service.api;

import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;

/**
 * Service for tasks
 */
public interface TaskService {

    /**
     * Add new task
     * @param task {@link Task}
     */
    void add(Task task);

    /**
     * Stop task by taskId and userId
     * @param taskId taskId
     * @param userId userId
     */
    void stop(String taskId, String userId);

    /**
     * Remove task by taskId and userId
     * @param taskId taskId
     * @param userId userId
     */
    void remove(String taskId, String userId);

    /**
     * Load tasks on page
     * @param page number of page
     * @param count number on page
     * @param userId userId
     * @return {@link Page} of {@link Task}
     */
    Page<Task> load(int page, int count, String userId);
}
