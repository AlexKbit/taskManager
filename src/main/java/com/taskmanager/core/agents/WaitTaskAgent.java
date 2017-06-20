package com.taskmanager.core.agents;

import com.taskmanager.core.agents.api.AbstractTaskAgent;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Agent for execute tasks in status WAIT -> IN_PROGRESS
 */
@Component
public class WaitTaskAgent extends AbstractTaskAgent {

    private static final Logger log = LoggerFactory.getLogger(WaitTaskAgent.class);

    /**
     * Constructor for TaskInProgressAgent
     */
    public WaitTaskAgent() {
        super(TaskStatus.WAIT, TaskStatus.LOADING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performTask(Task task) {
        if (task.getSrc() == null || task.getSrc().isEmpty()) {
            throw new ServiceException("Unknown url to resources");
        }
        log.debug("Validation successfully completed for task with id = {}", task.getId());
    }

    @Override
    @Scheduled(fixedRateString = "${app.fixedDelay.validateTask:1000}")
    public void execute() {
        super.execute();
    }
}
