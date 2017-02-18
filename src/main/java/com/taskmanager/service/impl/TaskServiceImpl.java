package com.taskmanager.service.impl;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repositories.TaskRepository;
import com.taskmanager.service.api.TaskService;
import com.taskmanager.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link TaskService}
 */
@Service
public class TaskServiceImpl implements TaskService {

    /**
    * Logger
    */
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void add(Task task) throws ServiceException {
        if (task == null) {
            LOGGER.error("Task is null");
            throw new ServiceException("Task is null");
        }
        task.setStatus(TaskStatus.WAIT);
        task = taskRepository.save(task);
        LOGGER.info("Add new task with id = {}", task.getId());
    }

    @Override
    public void stop(String taskId, String userId) throws ServiceException {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            LOGGER.error("Task with id = {} not found", taskId);
            throw new ServiceException("Task not found");
        }
        if (TaskStatus.STOP == task.getStatus() || TaskStatus.SUCCESS == task.getStatus()) {
            LOGGER.info("Task with id = {} already is stopped or completed", taskId);
            return;
        }
        if (!task.getUserId().equals(userId)) {
            LOGGER.error("User with userId = {} not allowed for this task with userId = {}",
                    userId, task.getUserId());
            throw new ServiceException("User not allowed for this task");
        }
        task.setStatus(TaskStatus.STOP);
        taskRepository.save(task);
        LOGGER.info("Task with id = {} was stopped", taskId);
    }

    @Override
    public void remove(String taskId, String userId) throws ServiceException {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            LOGGER.info("Task with id = {} already removed", taskId);
            return;
        }
        if (!task.getUserId().equals(userId)) {
            LOGGER.error("User with userId = {} not allowed for this task with userId = {}",
                    userId, task.getUserId());
            throw new ServiceException("User not allowed for this task");
        }
        taskRepository.delete(taskId);
        LOGGER.info("Task with id = {} was deleted", taskId);
    }

    @Override
    public Page<Task> load(int page, int count, String userId) {
        Pageable pageable = new PageRequest(page, count);
        Page<Task> pageTasks = taskRepository.findAll(pageable);
        LOGGER.info("Load {} tasks on page {}/{}", pageTasks.getSize(), page, pageTasks.getTotalPages());
        return pageTasks;
    }
}
