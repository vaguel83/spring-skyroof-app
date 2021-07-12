package com.intrasoft.skyroof.rest.controller;

import com.intrasoft.skyroof.persistence.dao.ProjectDao;
import com.intrasoft.skyroof.persistence.dao.TaskDao;
import com.intrasoft.skyroof.persistence.model.Project;
import com.intrasoft.skyroof.persistence.model.Task;
import com.intrasoft.skyroof.rest.RestConfig;
import com.intrasoft.skyroof.rest.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@CrossOrigin
//@RequestMapping(path = RestConfig.API_URI + "/projects", //This is not working probably the produces and consumes
//                produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
//                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RequestMapping(path = RestConfig.API_URI)

@Transactional
public class ProjectResource {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TaskDao taskDao;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @PostMapping(path = "/projects")
    public ResponseEntity<Project> create(@RequestBody Project project)
    {
        if (isNull(project.getProjectId())) {
            LOGGER.info("New project created with title: " + project.getTitle());
            return ResponseEntity.ok(projectDao.save(project));
        }
        else{
            LOGGER.warning("New project was NOT created with title: " + project.getTitle());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/projects")
    public ResponseEntity<Project> update(@RequestBody Project project)
    {
        if (nonNull(project.getProjectId()))
        {
            Project found = projectDao.findById(project.getProjectId())//
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            found.setTitle(project.getTitle());
            found.setDescription(project.getDescription());
            LOGGER.info("Project updated with id: " + project.getProjectId());
            return ResponseEntity.ok(projectDao.save(found));
        }
        else{
            LOGGER.warning("Project was NOT updated with id: " + project.getProjectId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/projects/{projectId}")
    public ResponseEntity deleteProject(@PathVariable("projectId") Long projectId) throws ResourceNotFoundException
    {
        Project project = projectDao.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Check if project can be deleted (no tasks are assigned to the project)
        List<Task> foundTasks = taskDao.findTasksByProjectId(projectId);

        if (!foundTasks.isEmpty())
        {
            LOGGER.warning("Tasks are assigned to that project, cannot be deleted. projectId: " + projectId);
            throw new ResourceNotFoundException("Tasks are assigned to that project, cannot be deleted. projectId: " + projectId);
        }

        projectDao.delete(project);
        LOGGER.info("Project with id: " + projectId + " deleted succesfully.");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/projects/{projectId}")
    public ResponseEntity findById(@PathVariable("projectId") Long projectId)
    {
        Project project = projectDao.findById(projectId)//
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok().body(project);
    }

    @GetMapping(path = "/projects")
    public ResponseEntity<List<Project>> findAll()
    {
        return ResponseEntity.ok(projectDao.findAll());
    }

    @GetMapping(path = "/projects/get_state/{projectId}")
    public Map<String, String> getProjectState (@PathVariable("projectId") Long projectId) throws ResourceNotFoundException
    {
        projectDao.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Get all Tasks with the specified projectId
        List<Task> foundTasks = taskDao.findTasksByProjectId(projectId);

        if (foundTasks.isEmpty())
        {
            throw new ResourceNotFoundException("No tasks are assigned to project: "+ projectId);
        }

        boolean completed = false;
        boolean in_progress = false;
        boolean not_started = false;

        for( Task task : foundTasks)
        {
            switch (task.getState())
            {
                case "NOT STARTED":
                    not_started = true;
                    break;
                case "IN PROGRESS":
                    in_progress = true;
                    break;

                case "COMPLETED":
                    completed = true;
                    break;
            }
            //If all are true no need to continue with more tasks.
            if (not_started && in_progress && completed)
            {
                break;
            }
        }
        Map<String, String> response = new HashMap<>();

        if( completed && !in_progress && !not_started) { response.put("project_state", "COMPLETED"); }
        if(!completed &&  in_progress &&  not_started) { response.put("project_state", "IN PROGRESS"); }
        if(!completed && !in_progress &&  not_started) { response.put("project_state", "NOT STARTED"); }
        if( completed && !in_progress &&  not_started) { response.put("project_state", "IN PROGRESS"); }
        if( completed &&  in_progress &&  not_started) { response.put("project_state", "IN PROGRESS"); }

        return response;
    }
}
