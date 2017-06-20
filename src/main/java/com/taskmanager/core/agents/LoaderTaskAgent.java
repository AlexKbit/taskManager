package com.taskmanager.core.agents;

import com.taskmanager.core.agents.api.AbstractTaskAgent;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Agent for execute tasks in status LOADING -> IN_PROGRESS
 */
@Component
public class LoaderTaskAgent extends AbstractTaskAgent {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(LoaderTaskAgent.class);

    /**
     * Constructor for TaskInProgressAgent
     */
    public LoaderTaskAgent() {
        super(TaskStatus.LOADING, TaskStatus.IN_PROGRESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performTask(Task task) {
        try (InputStream input = new URL(task.getSrc()).openStream()) {
            byte[] bytes = IOUtils.toByteArray(input);
            task.setData(bytes);
            log.info("Data successfully loaded for task with id = {}", task.getId());
        } catch (IOException e) {
            task.setErrorMsg(e.getMessage());
            log.error("Error load data for task with id = {}", task.getId(), e);
        }
    }

    @Override
    @Scheduled(fixedRateString = "${app.fixedDelay.LoaderTask:1000}")
    public void execute() {
        super.execute();
    }
}
