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
 * Agent for execute tasks in status IN_PROGRESS -> SUCCESS
 */
@Component
public class WaitTaskAgent extends AbstractTaskAgent {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitTaskAgent.class);

    /**
     * Constructor for TaskInProgressAgent
     */
    public WaitTaskAgent() {
        super(TaskStatus.WAIT, TaskStatus.IN_PROGRESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performTask(Task task) {
        if (task.getSrc() == null) {
            throw new ServiceException("Unknown url to resources");
        }
        if (task.getAlgo() == null) {
            throw new ServiceException("Unknown algorithm for solve hash");
        }
        LOGGER.info("Hash solve was completed successfully for task with id = {}", task.getId());
    }

    @Override
    @Scheduled(fixedDelayString = "${app.fixedDelay.validateTask:1000}")
    public void execute() {
        super.execute();
    }
}
