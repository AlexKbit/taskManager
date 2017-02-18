package com.taskmanager.app.core.agents;

import com.taskmanager.app.core.agents.api.Agent;
import com.taskmanager.app.core.service.load.api.DataLoader;
import com.taskmanager.app.model.HashType;
import com.taskmanager.app.model.Task;
import com.taskmanager.app.model.TaskStatus;
import com.taskmanager.app.repositories.TaskRepository;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

/**
 * Test for {@link TaskWaitAgent}
 */
@RunWith(EasyMockRunner.class)
public class TaskWaitAgentTest extends EasyMockSupport {

    private static final String TASK_ID = "550e8400e29b41d4a716446655449999";
    private static final String USER_ID = "550e8400e29b41d4a716446655440000";
    private static final byte[] RESULT_DATA = new byte[16];

    @Mock
    private DataLoader dataLoader;

    @Mock
    private TaskRepository taskRepository;

    @TestSubject
    private Agent agent = new TaskWaitAgent();

    @Test
    public void testExecute() throws IOException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Arrays.asList(task));
        expect(dataLoader.read(task.getSrc())).andReturn(RESULT_DATA);
        expect(taskRepository.findOne(task.getId())).andReturn(task);
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        agent.execute();
        assertEquals(TaskStatus.IN_PROGRESS, task.getTaskStatus());
        assertEquals(RESULT_DATA, task.getData());
        verifyAll();
    }

    @Test
    public void testExecuteWithEmptyList() throws NoSuchAlgorithmException {
        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Collections.emptyList());

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteButTaskAlreadyRemoved() throws IOException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Arrays.asList(task));
        expect(dataLoader.read(task.getSrc())).andReturn(RESULT_DATA);
        expect(taskRepository.findOne(task.getId())).andReturn(null);

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteButTaskAlreadyStopped() throws IOException {
        Task task = createTask();
        Task stoppedTask = createTask();
        stoppedTask.setTaskStatus(TaskStatus.STOP);
        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Arrays.asList(task));
        expect(dataLoader.read(task.getSrc())).andReturn(RESULT_DATA);
        expect(taskRepository.findOne(task.getId())).andReturn(stoppedTask);

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testInvalidTask() {
        Task task = createTask();
        task.setSrc(null);

        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Arrays.asList(task));
        expect(taskRepository.findOne(task.getId())).andReturn(task);
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        agent.execute();
        assertEquals(TaskStatus.FAIL, task.getTaskStatus());
        assertNotNull(task.getErrorMsg());
        verifyAll();
    }

    @Test
    public void testExecuteAlgorithmException() throws IOException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.WAIT.name())).andReturn(Arrays.asList(task));
        expect(dataLoader.read(task.getSrc())).andThrow(new IOException());
        expect(taskRepository.findOne(task.getId())).andReturn(task);
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        agent.execute();
        assertEquals(TaskStatus.FAIL, task.getTaskStatus());
        verifyAll();
    }

    private Task createTask() {
        Task task = new Task(TASK_ID, USER_ID);
        task.setAlgo(HashType.SHA_256.name());
        task.setSrc("www.test.com/file.txt");
        task.setTaskStatus(TaskStatus.WAIT);
        return task;
    }
}