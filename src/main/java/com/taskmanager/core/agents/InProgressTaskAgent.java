package com.taskmanager.core.agents;

import com.taskmanager.core.agents.api.AbstractTaskAgent;
import com.taskmanager.core.algorithms.api.DataLoader;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Agent for execute tasks in status IN_PROGRESS -> SUCCESS
 */
@Component
public class InProgressTaskAgent extends AbstractTaskAgent {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InProgressTaskAgent.class);

    /**
     * Hash solver
     */
    @Autowired
    private DataLoader dataLoader;

    /**
     * Constructor for TaskInProgressAgent
     */
    public InProgressTaskAgent() {
        super(TaskStatus.IN_PROGRESS, TaskStatus.SUCCESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performTask(Task task) {
        String result = dataLoader.solveHash(task.getSrc(), task.getAlgo());
        task.setResult(result);
        LOGGER.info("Hash solve was completed successfully for task with id = {}", task.getId());
    }

    @Override
    @Scheduled(fixedDelayString = "${app.fixedDelay.hashSolveTask:1000}")
    public void execute() {
        super.execute();
    }
}
