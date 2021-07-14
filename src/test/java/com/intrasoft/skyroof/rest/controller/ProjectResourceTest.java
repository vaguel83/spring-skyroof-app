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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        //Write project test
        mvc.perform(post("/api/v1/projects")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\"}")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));

        //Write task with state:Not started
        mvc.perform(post("/api/v1/tasks")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\", \"projectId\":1, \"state\":\"NOT STARTED\", \"startDate\":\"22/07/2021\", \"completedDate\":\"30/07/2021\" }")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("NOT STARTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("22/07/2021"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completedDate").value("30/07/2021"));

        //Write task with completed state
        mvc.perform(post("/api/v1/tasks")//
                .content(" {\"title\":\"Title\", \"description\":\"Description\", \"projectId\":1, \"state\":\"COMPLETED\", \"startDate\":\"22/07/2021\", \"completedDate\":\"30/07/2021\" }")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("COMPLETED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("22/07/2021"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completedDate").value("30/07/2021"));

        //Delete the task with not started state (it should be deleted)
        mvc.perform(delete("/api/v1/tasks/1")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk());

        //Delete the task with completed(it should fail)
        mvc.perform(delete("/api/v1/tasks/2")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isForbidden());

        //Update the task with state not started
        mvc.perform(put("/api/v1/tasks")//
                .content(" {\"taskId\":\"2\", \"title\":\"Title\", \"description\":\"Description\", \"projectId\":1, \"state\":\"NOT STARTED\", \"startDate\":\"22/07/2021\", \"completedDate\":\"30/07/2021\" }")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk())//
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))//
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("NOT STARTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("22/07/2021"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completedDate").value("30/07/2021"));

        //Delete the task 2 with not started state (it should be deleted)
        mvc.perform(delete("/api/v1/tasks/2")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk());

        //Delete the project, since all tasks are deleted the project should be deleted too.
        mvc.perform(delete("/api/v1/projects/1")//
                .contentType(MediaType.APPLICATION_JSON))//
                .andExpect(status().isOk());

    }
}