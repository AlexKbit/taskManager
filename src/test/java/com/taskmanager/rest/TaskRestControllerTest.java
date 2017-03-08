package com.taskmanager.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link TaskRestController}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("int-test")
@SpringBootTest(classes = {ApplicationLauncher.class})
public class TaskRestControllerTest {

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

    private Task createTask() {
        Task t = new Task();
        t.setUserId(UUID.randomUUID().toString());
        t.setAlgo(HashType.MD5);
        t.setStatus(TaskStatus.WAIT);
        return t;
    }



}