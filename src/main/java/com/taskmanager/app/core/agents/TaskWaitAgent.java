package com.taskmanager.app.core.agents;

import com.taskmanager.app.core.agents.api.AbstractTaskAgent;
import com.taskmanager.app.core.service.load.api.DataLoader;
import com.taskmanager.app.core.service.load.impl.DataLoaderImpl;
import com.taskmanager.app.model.Task;
import com.taskmanager.app.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.function.Predicate;

/**
 * Agent for execute tasks in status WAIT -> IN_PROGRESS
 */
@Component
public class TaskWaitAgent extends AbstractTaskAgent {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWaitAgent.class);

    /**
     * Data loader
     */
    @Autowired
    private DataLoader dataLoader;

    /**
     * Constructor for TaskWaitAgent
     */
    public TaskWaitAgent() {
        super(TaskStatus.WAIT, TaskStatus.IN_PROGRESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performTask(Task task) {
        LocalTime time = LocalTime.now();
        if (!isValid(task, time)) {
            return;
        }
        byte[] data;
        try {
            data = dataLoader.read(task.getSrc());
        } catch (IOException e) {
            LOGGER.error("Error load data for task with id = {}", task.getId());
            saveFailed(task, e.getMessage(), time);
            return;
        }
        task.setData(data);
        saveSuccess(task, time);
        LOGGER.info("Data load was completed successfully for task with id = {}", task.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Predicate<Task> getValidatePredicate() {
        return t -> !(t.getSrc()==null || t.getSrc().isEmpty());
    }
}
