package com.taskmanager.service.impl;


import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repositories.TaskRepository;
import com.taskmanager.service.api.TaskService;
import com.taskmanager.service.exception.ServiceException;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Test for {@link TaskServiceImpl}
 */
@RunWith(EasyMockRunner.class)
public class TaskServiceImplTest extends EasyMockSupport {

    private static final String TASK_ID = "550e8400e29b41d4a716446655449999";
    private static final String USER_ID = "550e8400e29b41d4a716446655440000";

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Task task;

    @Mock
    private Page<Task> pageTasks;

    @TestSubject
    private TaskService taskService = new TaskServiceImpl();

    @Test
    public void testAdd() throws ServiceException {
        task.setStatus(TaskStatus.WAIT);
        expectLastCall();
        expect(taskRepository.save(task)).andReturn(task);
        expect(task.getId()).andReturn(TASK_ID);

        replayAll();
        taskService.add(task);
        verifyAll();
    }

    @Test
    public void testAddNull() throws ServiceException {
        replayAll();
        try {
            taskService.add(null);
            fail("Exception should be throw");
        } catch (ServiceException se) {
            assertNotNull(se.getMessage());
        }
        verifyAll();
    }

    @Test
    public void testStop() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(task);
        expect(task.getUserId()).andReturn(USER_ID);
        expect(task.getStatus()).andReturn(TaskStatus.WAIT).anyTimes();
        task.setStatus(TaskStatus.STOP);
        expectLastCall();
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        taskService.stop(TASK_ID, USER_ID);
        verifyAll();
    }

    @Test
    public void testStopTaskAlreadyStoped() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(task);
        expect(task.getStatus()).andReturn(TaskStatus.STOP);

        replayAll();
        taskService.stop(TASK_ID, USER_ID);
        verifyAll();
    }

    @Test
    public void testStopTaskNotFound() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(null);

        replayAll();
        try {
            taskService.stop(TASK_ID, USER_ID);
            fail("Exception should be throw");
        } catch (ServiceException se) {
            assertNotNull(se.getMessage());
        }
        verifyAll();
    }

    @Test
    public void testStopTaskForOfAnotherUser() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(task);
        expect(task.getUserId()).andReturn("1").anyTimes();
        expect(task.getStatus()).andReturn(TaskStatus.WAIT).anyTimes();

        replayAll();
        try {
            taskService.stop(TASK_ID, USER_ID);
            fail("Exception should be throw");
        } catch (ServiceException se) {
            assertNotNull(se.getMessage());
        }
        verifyAll();
    }

    @Test
    public void testRemove() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(task);
        expect(task.getUserId()).andReturn(USER_ID);
        taskRepository.delete(TASK_ID);
        expectLastCall();

        replayAll();
        taskService.remove(TASK_ID, USER_ID);
        verifyAll();
    }

    @Test
    public void testRemoveTaskNotFound() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(null);
        replayAll();
        taskService.remove(TASK_ID, USER_ID);
        verifyAll();
    }

    @Test
    public void testRemoveTaskForOfAnotherUser() throws ServiceException {
        expect(taskRepository.findOne(TASK_ID)).andReturn(task);
        expect(task.getUserId()).andReturn("1").anyTimes();

        replayAll();
        try {
            taskService.remove(TASK_ID, USER_ID);
            fail("Exception should be throw");
        } catch (ServiceException se) {
            assertNotNull(se.getMessage());
        }
        verifyAll();
    }

    @Test
    public void testLoad() throws ServiceException {
        expect(taskRepository.findByUserId(eq(USER_ID), anyObject(Pageable.class))).andReturn(pageTasks);
        expect(pageTasks.getSize()).andReturn(10);
        expect(pageTasks.getTotalPages()).andReturn(4);
        replayAll();
        Page<Task> page = taskService.load(2, 10, USER_ID);
        assertNotNull(page);
        verifyAll();
    }
}