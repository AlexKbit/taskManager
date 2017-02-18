package com.taskmanager.app.core.agents;

import com.taskmanager.app.core.agents.api.Agent;
import com.taskmanager.app.core.service.hash.impl.api.HashSolver;
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

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

/**
 * Test for {@link TaskInProgressAgent}
 */
@RunWith(EasyMockRunner.class)
public class TaskInProgressAgentTest extends EasyMockSupport {

    private static final String TASK_ID = "550e8400e29b41d4a716446655449999";
    private static final String USER_ID = "550e8400e29b41d4a716446655440000";
    private static final String RESULT_HASH = "238asd4sdf7s8237hjdf77df67d6f";

    @Mock
    private HashSolver hashSolver;

    @Mock
    private TaskRepository taskRepository;

    @TestSubject
    private Agent agent = new TaskInProgressAgent();

    @Test
    public void testExecute() throws NoSuchAlgorithmException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Arrays.asList(task));
        expect(hashSolver.hash(task.getHashType(), task.getData())).andReturn(RESULT_HASH);
        expect(taskRepository.findOne(task.getId())).andReturn(task);
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        agent.execute();
        assertEquals(TaskStatus.SUCCESS, task.getTaskStatus());
        assertEquals(RESULT_HASH, task.getResult());
        verifyAll();
    }

    @Test
    public void testExecuteWithEmptyList() throws NoSuchAlgorithmException {
        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Collections.emptyList());

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteButTaskAlreadyRemoved() throws NoSuchAlgorithmException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Arrays.asList(task));
        expect(hashSolver.hash(task.getHashType(), task.getData())).andReturn(RESULT_HASH);
        expect(taskRepository.findOne(task.getId())).andReturn(null);

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteButTaskAlreadyStopped() throws NoSuchAlgorithmException {
        Task task = createTask();
        Task stoppedTask = createTask();
        stoppedTask.setTaskStatus(TaskStatus.STOP);
        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Arrays.asList(task));
        expect(hashSolver.hash(task.getHashType(), task.getData())).andReturn(RESULT_HASH);
        expect(taskRepository.findOne(task.getId())).andReturn(stoppedTask);

        replayAll();
        agent.execute();
        verifyAll();
    }

    @Test
    public void testInvalidTask() throws NoSuchAlgorithmException {
        Task task = createTask();
        task.setData(null);

        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Arrays.asList(task));
        expect(taskRepository.findOne(task.getId())).andReturn(task);
        expect(taskRepository.save(task)).andReturn(task);

        replayAll();
        agent.execute();
        assertEquals(TaskStatus.FAIL, task.getTaskStatus());
        assertNotNull(task.getErrorMsg());
        verifyAll();
    }

    @Test
    public void testExecuteAlgorithmException() throws NoSuchAlgorithmException {
        Task task = createTask();
        expect(taskRepository.findWithStatus(TaskStatus.IN_PROGRESS.name())).andReturn(Arrays.asList(task));
        expect(hashSolver.hash(task.getHashType(), task.getData())).andThrow(new NoSuchAlgorithmException());
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
        byte[] data = new byte[16];
        new Random().nextBytes(data);
        task.setData(data);
        task.setSrc("www.test.com/file.txt");
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        return task;
    }

}