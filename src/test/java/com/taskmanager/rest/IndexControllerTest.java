package com.taskmanager.rest;

import com.taskmanager.app.ApplicationLauncher;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test for {@link IndexController}
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("int-test")
@SpringBootTest(classes = {ApplicationLauncher.class})
public class IndexControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(
                get("/")
                        .accept(MediaType.TEXT_HTML)
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}