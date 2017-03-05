package com.taskmanager.core.agents;

import com.taskmanager.core.algorithms.api.DataLoader;
import com.taskmanager.model.HashType;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repositories.TaskRepository;
import com.taskmanager.service.exception.ServiceException;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link InProgressTaskAgent}
 */
@RunWith(EasyMockRunner.class)
public class InProgressTaskAgentTest  extends EasyMockSupport {

    private static final String DEFAULT_SRC = "src";

    private static final String DEFAULT_HASH = "hash";

    private static final HashType DEFAULT_HASH_TYPE = HashType.MD5;

    @TestSubject
    private InProgressTaskAgent inProgressTaskAgent = new InProgressTaskAgent();

    @Mock
    private TaskRepository repository;

    @Mock
    private DataLoader dataLoader;

    @Test
    public void testExecute() {
        List<Task> tasks = generateTaskList(2);
        expect(repository.findByStatusOrderByTime(TaskStatus.IN_PROGRESS)).andReturn(tasks);
        expect(dataLoader.solveHash(eq(DEFAULT_SRC), eq(DEFAULT_HASH_TYPE))).andReturn(DEFAULT_HASH);
        expect(dataLoader.solveHash(eq(DEFAULT_SRC), eq(DEFAULT_HASH_TYPE))).andReturn(DEFAULT_HASH);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(tasks.get(0));
        expect(repository.findOne(eq(tasks.get(1).getId()))).andReturn(tasks.get(1));
        expect(repository.save(eq(tasks.get(0)))).andReturn(tasks.get(0));
        expect(repository.save(eq(tasks.get(1)))).andReturn(tasks.get(1));
        replayAll();
        inProgressTaskAgent.execute();
        assertEquals(TaskStatus.SUCCESS, tasks.get(0).getStatus());
        assertEquals(TaskStatus.SUCCESS, tasks.get(1).getStatus());
        verifyAll();
    }

    @Test
    public void testExecuteFail() {
        List<Task> tasks = generateTaskList(1);
        expect(repository.findByStatusOrderByTime(TaskStatus.IN_PROGRESS)).andReturn(tasks);
        expect(dataLoader.solveHash(eq(DEFAULT_SRC), eq(DEFAULT_HASH_TYPE))).andThrow(new ServiceException(""));
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(tasks.get(0));
        expect(repository.save(eq(tasks.get(0)))).andReturn(tasks.get(0));
        replayAll();
        inProgressTaskAgent.execute();
        assertEquals(TaskStatus.FAIL, tasks.get(0).getStatus());
        verifyAll();
    }

    @Test
    public void testExecuteFailButTaskStopped() {
        List<Task> tasks = generateTaskList(1);
        expect(repository.findByStatusOrderByTime(TaskStatus.IN_PROGRESS)).andReturn(tasks);
        expect(dataLoader.solveHash(eq(DEFAULT_SRC), eq(DEFAULT_HASH_TYPE))).andThrow(new ServiceException(""));
        Task stoppedTask = new Task();
        stoppedTask.setStatus(TaskStatus.STOP);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(stoppedTask);
        replayAll();
        inProgressTaskAgent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteEmpty() {
        expect(repository.findByStatusOrderByTime(TaskStatus.IN_PROGRESS)).andReturn(Collections.emptyList());
        replayAll();
        inProgressTaskAgent.execute();
        verifyAll();
    }

    private List<Task> generateTaskList(int n) {
        return IntStream.range(0,n)
                .mapToObj(i -> new Task())
                .peek(t -> t.setId(UUID.randomUUID().toString()))
                .peek(t -> t.setSrc(DEFAULT_SRC))
                .peek(t -> t.setAlgo(DEFAULT_HASH_TYPE))
                .peek(t -> t.setStatus(TaskStatus.IN_PROGRESS))
                .collect(Collectors.toList());

    }
}