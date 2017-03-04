package com.taskmanager.repositories;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Task repository
 */
public interface TaskRepository extends JpaRepository<Task, String> {

    /**
     * Find all tasks on page
     * @param pageable {@link Pageable}
     * @return {@link Page} of tasks
     */
    @Query("select t from Task t where t.userId = :userId")
    Page<Task> findByUserId(@Param("userId") String userId, Pageable pageable);

    /**
     * Load list of tasks with status
     * @param status task status
     * @return List of tasks
     */
    @Query("select t from Task t where t.status = ?1 order by t.createdAt desc")
    List<Task> findByStatusOrderByTime(@Param("status") TaskStatus status);

    /**
     * Load list of tasks after sets date
     * @param date time
     * @return List of tasks
     */
    @Query("select t from Task t where t.createdAt < :date")
    List<Task> findOldByTime(@Param("date") Date date);
}
