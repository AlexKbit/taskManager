package com.taskmanager.app.service.api;

import com.taskmanager.app.model.Task;
import com.taskmanager.app.service.exception.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service for tasks
 */
public interface TaskService {

    /**
     * Add new task
     * @param task {@link Task}
     * @throws ServiceException
     */
    void add(Task task) throws ServiceException;

    /**
     * Stop task by taskId and userId
     * @param taskId taskId
     * @param userId userId
     * @throws ServiceException
     */
    void stop(String taskId, String userId) throws ServiceException;

    /**
     * Remove task by taskId and userId
     * @param taskId taskId
     * @param userId userId
     * @throws ServiceException
     */
    void remove(String taskId, String userId) throws ServiceException;

    /**
     * Load tasks on page
     * @param page number of page
     * @param count number on page
     * @param userId userId
     * @return {@link Page} of {@link Task}
     */
    Page<Task> load(int page, int count, String userId);
}
