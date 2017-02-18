package com.taskmanager.app.core.agents.api;

import com.taskmanager.app.model.Task;
import com.taskmanager.app.model.TaskStatus;
import com.taskmanager.app.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract class for agents
 */
public abstract class AbstractTaskAgent implements Agent {

    /**
     * Fixed delay for execute
     */
    private static final String FIXED_DELAY = "1000";

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
    protected List<Task> loadTasks() {
        return taskRepository.findWithStatus(sourceTaskStatus.name());
    }

    /**
     * Save task with target status
     * @param task {@link Task}
     */
    protected void saveSuccess(Task task, LocalTime startTime) {
        task.setTaskStatus(targetTaskStatus);
        task.applyTime(ChronoUnit.MILLIS.between(startTime, LocalTime.now()));
        extraSave(task);
    }

    /**
     * Save task with fail status and error
     * @param task {@link Task}
     * @param msg error message
     */
    protected void saveFailed(Task task, String msg, LocalTime startTime) {
        task.setTaskStatus(TaskStatus.FAIL);
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
        if (TaskStatus.STOP == oldTask.getTaskStatus()) {
            return; // this task already stopped
        }
        try {
            taskRepository.save(task);
        } catch (DataAccessException re) {
            task.setData(null);
            saveFailed(task, re.getMessage(), LocalTime.now());
        }
    }

    /**
     * Execute agent
     */
    @Override
    @Scheduled(fixedDelayString = FIXED_DELAY)
    public void execute() {
        List<Task> taskList = loadTasks();
        if (taskList == null || taskList.isEmpty()) {
            return;
        }
        taskList.forEach(t -> performTask(t));
    }

    /**
     * Perform task
     * @param task {@link Task}
     */
    protected abstract void performTask(Task task);

    /**
     * Validate task for agent
     * @param task {@link Task}
     * @return true if is valid, else false
     */
    protected boolean isValid(Task task, LocalTime startTime) {
        if (!getValidatePredicate().test(task)) {
            saveFailed(task, "Validation error", startTime);
            return false;
        }
        return true;
    }

    /**
     * Predicate of validate task for agent
     * @return {@link Task}
     */
    protected abstract Predicate<Task> getValidatePredicate();
}
