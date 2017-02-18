package com.taskmanager.app.core.agents;

import com.taskmanager.app.core.agents.api.AbstractTaskAgent;
import com.taskmanager.app.core.service.hash.impl.api.HashSolver;
import com.taskmanager.app.model.Task;
import com.taskmanager.app.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.function.Predicate;

/**
 * Agent for execute tasks in status IN_PROGRESS -> SUCCESS
 */
@Component
public class TaskInProgressAgent extends AbstractTaskAgent {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskInProgressAgent.class);

    /**
     * Hash solver
     */
    @Autowired
    private HashSolver hashSolver;

    /**
     * Constructor for TaskInProgressAgent
     */
    public TaskInProgressAgent() {
        super(TaskStatus.IN_PROGRESS, TaskStatus.SUCCESS);
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
        String hash = null;
        try {
            hash = hashSolver.hash(task.getHashType(), task.getData());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error hash solve for task with id = {}", task.getId());
            saveFailed(task, e.getMessage(), time);
            return;
        }
        task.setResult(hash);
        saveSuccess(task, time);
        LOGGER.info("Hash solve was completed successfully for task with id = {}", task.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Predicate<Task> getValidatePredicate() {
        return t -> t.getData() != null && t.getData().length > 0;
    }
}
