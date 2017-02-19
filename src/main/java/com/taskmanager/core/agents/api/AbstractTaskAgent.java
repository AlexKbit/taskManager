package com.taskmanager.core.agents.api;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repositories.TaskRepository;
import com.taskmanager.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedRuntimeException;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Abstract class for agents
 */
public abstract class AbstractTaskAgent implements Agent {

    /**
     * Fixed delay for execute
     */
    @Value("")
    private static String FIXED_DELAY;

    /**
     * Task repository
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Source status for tasks
     */
    private TaskStatus sourceTaskStatus;

    /**
     * Target status for tasks
     */
    private TaskStatus targetTaskStatus;

    /**
     * Create AbstractTaskAgent
     * @param sourceTaskStatus source {@link TaskStatus}
     * @param targetTaskStatus target {@link TaskStatus}
     */
    public AbstractTaskAgent(TaskStatus sourceTaskStatus, TaskStatus targetTaskStatus) {
        this.sourceTaskStatus = sourceTaskStatus;
        this.targetTaskStatus = targetTaskStatus;
    }

    /**
     * Load tasks with source status
     * @return List of {@link Task}
     */
    private List<Task> loadTasks() {
        return taskRepository.findByStatusOrderByTime(sourceTaskStatus.name());
    }

    /**
     * Save task with target status
     * @param task {@link Task}
     */
    private void saveSuccess(Task task, LocalTime startTime) {
        task.setStatus(targetTaskStatus);
        task.applyTime(ChronoUnit.MILLIS.between(startTime, LocalTime.now()));
        extraSave(task);
    }

    /**
     * Save task with fail status and error
     * @param task {@link Task}
     * @param msg error message
     */
    private void saveFailed(Task task, String msg, LocalTime startTime) {
        task.setStatus(TaskStatus.FAIL);
        task.setErrorMsg(msg);
        task.applyTime(ChronoUnit.SECONDS.between(LocalTime.now(), startTime));
        extraSave(task);
    }

    /**
     * Extra save
     * @param task {@link Task}
     */
    private void extraSave(Task task) {
        Task oldTask = taskRepository.findOne(task.getId());
        if (oldTask == null) {
            return; // this task already removed
        }
        if (TaskStatus.STOP == oldTask.getStatus()) {
            return; // this task already stopped
        }
        taskRepository.save(task);
    }

    /**
     * Execute agent
     */
    @Override
    @Scheduled(fixedDelayString = "${app.fixedDelay:1000}")
    public void execute() {
        List<Task> taskList = loadTasks();
        if (taskList == null || taskList.isEmpty()) {
            return;
        }
        for (Task task : taskList) {
            try {
                final LocalTime timeStart = LocalTime.now();
                performTask(task);
                saveSuccess(task, timeStart);
            } catch (ServiceException | NestedRuntimeException e) {
                saveFailed(task, e.getMessage(), LocalTime.now());
            }
        }
    }

    /**
     * Perform task
     * @param task {@link Task}
     * @return Success/Fail
     */
    protected abstract void performTask(Task task);
}
