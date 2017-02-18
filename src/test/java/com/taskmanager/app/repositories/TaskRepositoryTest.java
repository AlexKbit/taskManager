package com.taskmanager.app.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.taskmanager.app.config.AppConfig;
import com.taskmanager.app.model.Task;
import com.taskmanager.app.model.TaskStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tast for {@link TaskRepository}
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {AppConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-cases/tasks.xml")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void save() {
        Task task = new Task();
        task.setResult("result");
        taskRepository.save(task);
        Task taskNew = taskRepository.findOne(task.getId());
        assertNotNull(taskNew);
        assertEquals(task.getId(), taskNew.getId());
        assertEquals(task.getCreatedAt(), taskNew.getCreatedAt());
        assertEquals(task.getResult(), taskNew.getResult());
    }

    @Test
    public void saveVeryLongByte() {
        Task task = new Task();
        task.setResult("result");
        Random random = new Random();
        byte[] data = new byte[32128];
        random.nextBytes(data);
        task.setData(data);
        taskRepository.save(task);
        Task taskNew = taskRepository.findOne(task.getId());
        assertNotNull(taskNew);
        assertEquals(task.getId(), taskNew.getId());
        assertEquals(task.getCreatedAt(), taskNew.getCreatedAt());
        assertEquals(task.getResult(), taskNew.getResult());
    }

    @Test
    public void findOne() {
        Task task = taskRepository.findOne("id-1");
        assertTrue(task.getUserId().equals("userid-1"));
    }

    @Test
    public void findAll() {
        Collection<Task> tasks = (Collection<Task>) taskRepository.findAll();
        assertEquals(3, tasks.size());
    }

    @Test
    public void findAllPagination() {
        Page<Task> personPage = taskRepository.findAll(new PageRequest(0, 2));
        assertEquals(2, personPage.getTotalPages());
        assertEquals(2, personPage.getSize());
    }

    @Test
    public void findAllByStatusWait() {
        List<Task> tasks = taskRepository.findWithStatus(TaskStatus.WAIT.name());
        assertEquals(1, tasks.size());
    }

    @Test
    public void findAllByStatusSuccess() {
        List<Task> tasks = taskRepository.findWithStatus(TaskStatus.SUCCESS.name());
        assertEquals(2, tasks.size());
    }

    @Test
    public void delete() {
        Task task = new Task();
        taskRepository.save(task);
        taskRepository.delete(task);
    }

    @Test
    public void deleteById() {
        Task task = new Task();
        taskRepository.save(task);
        taskRepository.delete(task.getId());
    }

    @Test
    public void count() {
        assertEquals(3, taskRepository.count());
    }
}
