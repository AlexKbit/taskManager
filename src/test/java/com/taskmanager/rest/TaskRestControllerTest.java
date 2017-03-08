package com.taskmanager.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import com.taskmanager.app.ApplicationLauncher;
import com.taskmanager.model.HashType;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link TaskRestController}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("int-test")
@SpringBootTest(classes = {ApplicationLauncher.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DbUnitConfiguration(dataSetLoader = FlatXmlDataSetLoader.class, databaseConnection = "dataSource")
public class TaskRestControllerTest {

    private static final String USER_ID = "9ac8087d-fc14-4e4f-b9d0-5c4b9e05a000";
    private static final String TASK_IN_PROGRESS_ID = "8ac8087d-fc14-4e4f-b9d0-5c4b9e05a501";
    private static final String TASK_SUCCESS_ID = "8ac8087d-fc14-4e4f-b9d0-5c4b9e05a502";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreate() throws Exception {
        mvc.perform(
                post("/tasks/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTask())))
                .andExpect(status().isCreated());
    }

    @Test
    @DatabaseSetup(value = "/datasets/task_collection.xml")
    public void testStop() throws Exception {
        mvc.perform(
                get(String.format("/tasks/stop?taskId=%s&userId=%s", TASK_IN_PROGRESS_ID, USER_ID))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTask())))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup(value = "/datasets/task_collection.xml")
    public void testDelete() throws Exception {
        mvc.perform(
                delete(String.format("/tasks?taskId=%s&userId=%s", TASK_SUCCESS_ID, USER_ID))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTask())))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup(value = "/datasets/task_collection.xml")
    public void testLoad() throws Exception {
        mvc.perform(
                get(String.format("/tasks?page=0&count=7&userId=%s", USER_ID))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTask())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(7)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    private Task createTask() {
        Task t = new Task();
        t.setUserId(UUID.randomUUID().toString());
        t.setAlgo(HashType.MD5);
        t.setStatus(TaskStatus.WAIT);
        return t;
    }

}