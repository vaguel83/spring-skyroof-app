package com.intrasoft.skyroof.rest.controller;

import com.intrasoft.skyroof.persistence.model.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class ProjectResourceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCreateResource() throws Exception {
        Project project = new Project();
        project.setTitle("Title");
        project.setDescription("Description");

        mvc.perform(post("/api/v1/projects")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\"}")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));

        mvc.perform(post("/api/v1/tasks")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\", \"projectId\":1, \"state\":\"NOT STARTED\", \"startDate\":\"2021-07-22\", \"completedDate\":\"2021-07-30\" }")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("NOT STARTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("2021-07-22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completedDate").value("2021-07-30"));

        mvc.perform(post("/api/v1/tasks")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\", \"projectId\":1, \"state\":\"COMPLETED\", \"startDate\":\"2021-07-22\", \"completedDate\":\"2021-07-30\" }")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("COMPLETED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("2021-07-22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completedDate").value("2021-07-30"));

        mvc.perform(delete("/api/v1/tasks/1")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk());

        mvc.perform(delete("/api/v1/tasks/2")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isForbidden());

    }
}