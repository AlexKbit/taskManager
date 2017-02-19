package com.taskmanager.core.agents;

import com.taskmanager.model.Task;
import com.taskmanager.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Agent for execute tasks in status IN_PROGRESS -> SUCCESS
 */
@Component
public class RemoveOldTaskAgent {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveOldTaskAgent.class);

    @Autowired
    private TaskRepository taskRepository;

    @Value("${app.removeMoreDays:7}")
    private int daysRemember;

    /**
     * Remove old tasks
     */
    @Scheduled(fixedDelayString = "${app.fixedDelay.removeOld:1000}")
    public void execute() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault());
        localDateTime = localDateTime.minusDays(daysRemember);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        List<Task> taskList = taskRepository.findOldByTime(date);
        taskRepository.delete(taskList);
        LOGGER.info("Some tasks after {} date was removed", date);
    }
}
