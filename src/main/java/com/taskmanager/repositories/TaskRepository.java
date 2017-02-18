package com.taskmanager.repositories;

import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Task repository
 */
public interface TaskRepository extends CrudRepository<Task, String> {

    /**
     * Find all tasks on page
     * @param pageable {@link Pageable}
     * @return {@link Page} of tasks
     */
    Page<Task> findAll(Pageable pageable);

    /**
     * Load list of tasks with status
     * @param status task status
     * @return List of tasks
     */
    @Query("select t from Task t where t.status = ?1 order by t.createdAt desc")
    List<Task> findByStatusOrderByTime(String status);
}
