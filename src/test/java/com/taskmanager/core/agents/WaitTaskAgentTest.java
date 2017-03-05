package com.taskmanager.core.agents;

import com.taskmanager.model.HashType;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repositories.TaskRepository;
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
 * Test for {@link WaitTaskAgent}
 */
@RunWith(EasyMockRunner.class)
public class WaitTaskAgentTest extends EasyMockSupport {

    @TestSubject
    private WaitTaskAgent waitTaskAgent = new WaitTaskAgent();

    @Mock
    private TaskRepository repository;

    @Test
    public void testExecute() {
        List<Task> tasks = generateTaskList(2);
        expect(repository.findByStatusOrderByTime(TaskStatus.WAIT)).andReturn(tasks);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(tasks.get(0));
        expect(repository.findOne(eq(tasks.get(1).getId()))).andReturn(tasks.get(1));
        expect(repository.save(eq(tasks.get(0)))).andReturn(tasks.get(0));
        expect(repository.save(eq(tasks.get(1)))).andReturn(tasks.get(1));
        replayAll();
        waitTaskAgent.execute();
        assertEquals(TaskStatus.IN_PROGRESS, tasks.get(0).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, tasks.get(1).getStatus());
        verifyAll();
    }

    @Test
    public void testExecuteNotFound() {
        List<Task> tasks = generateTaskList(1);
        expect(repository.findByStatusOrderByTime(TaskStatus.WAIT)).andReturn(tasks);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(null);
        replayAll();
        waitTaskAgent.execute();
        verifyAll();
    }

    @Test
    public void testExecuteNotValidBySrc() {
        List<Task> tasks = generateTaskList(1);
        tasks.get(0).setSrc(null);
        expect(repository.findByStatusOrderByTime(TaskStatus.WAIT)).andReturn(tasks);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(tasks.get(0));
        expect(repository.save(eq(tasks.get(0)))).andReturn(tasks.get(0));
        replayAll();
        waitTaskAgent.execute();
        assertEquals(TaskStatus.FAIL, tasks.get(0).getStatus());
        verifyAll();
    }

    @Test
    public void testExecuteNotValidByAlgo() {
        List<Task> tasks = generateTaskList(1);
        tasks.get(0).setAlgo(null);
        expect(repository.findByStatusOrderByTime(TaskStatus.WAIT)).andReturn(tasks);
        expect(repository.findOne(eq(tasks.get(0).getId()))).andReturn(tasks.get(0));
        expect(repository.save(eq(tasks.get(0)))).andReturn(tasks.get(0));
        replayAll();
        waitTaskAgent.execute();
        assertEquals(TaskStatus.FAIL, tasks.get(0).getStatus());
        verifyAll();
    }

    @Test
    public void testExecuteEmpty() {
        expect(repository.findByStatusOrderByTime(TaskStatus.WAIT)).andReturn(Collections.emptyList());
        replayAll();
        waitTaskAgent.execute();
        verifyAll();
    }

    private List<Task> generateTaskList(int n) {
        return IntStream.range(0,n)
                .mapToObj(i -> new Task())
                .peek(t -> t.setId(UUID.randomUUID().toString()))
                .peek(t -> t.setSrc("src"))
                .peek(t -> t.setAlgo(HashType.MD5))
                .peek(t -> t.setStatus(TaskStatus.WAIT))
                .collect(Collectors.toList());

    }
}