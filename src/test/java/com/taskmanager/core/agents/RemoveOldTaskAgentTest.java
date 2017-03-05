package com.taskmanager.core.agents;

import com.taskmanager.model.Task;
import com.taskmanager.repositories.TaskRepository;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;


/**
 * Test for {@link RemoveOldTaskAgent}
 */
@RunWith(EasyMockRunner.class)
public class RemoveOldTaskAgentTest extends EasyMockSupport{

    private List<Task> TASK_LIST = Arrays.asList(new Task(), new Task());

    @TestSubject
    private RemoveOldTaskAgent removeOldTaskAgent = new RemoveOldTaskAgent();

    @Mock
    private TaskRepository repository;

    @Test
    public void testTaskList() {
        expect(repository.findOldByTime(anyObject(Date.class))).andReturn(TASK_LIST);
        repository.delete(eq(TASK_LIST));
        expectLastCall();
        replayAll();
        removeOldTaskAgent.execute();
        verifyAll();
    }

    @Test
    public void testTaskListEmpty() {
        expect(repository.findOldByTime(anyObject(Date.class))).andReturn(Collections.emptyList());
        replayAll();
        removeOldTaskAgent.execute();
        verifyAll();
    }
}