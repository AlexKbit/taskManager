package com.taskmanager.core.agents;

import com.taskmanager.core.agents.api.AbstractTaskAgent;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.api.ClassExecuteService;
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
    private static final Logger log = LoggerFactory.getLogger(InProgressTaskAgent.class);

    @Autowired
    private ClassExecuteService classExecuteServuce;

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
        classExecuteServuce.execute(task);
        log.info("Class executed for task with id = {}", task.getId());
    }

    @Override
    @Scheduled(fixedRateString = "${app.fixedDelay.ExecuteTask:1000}")
    public void execute() {
        super.execute();
    }
}
