package com.taskmanager.core.agents;

import com.taskmanager.core.agents.api.Agent;
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
 * Agent for remove old tasks
 */
@Component
public class RemoveOldTaskAgent implements Agent {

    private static final Logger log = LoggerFactory.getLogger(RemoveOldTaskAgent.class);

    @Autowired
    private TaskRepository taskRepository;

    @Value("${app.removeMoreDays:7}")
    private int daysRemember;

    @Override
    @Scheduled(fixedRateString = "${app.fixedDelay.removeOld:1000}")
    public void execute() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault());
        localDateTime = localDateTime.minusDays(daysRemember);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        List<Task> taskList = taskRepository.findOldByTime(date);
        if (taskList.isEmpty()) {
            return;
        }
        log.info("The {} tasks after {} date was removed", taskList.size(), date);
        taskRepository.delete(taskList);
    }
}
