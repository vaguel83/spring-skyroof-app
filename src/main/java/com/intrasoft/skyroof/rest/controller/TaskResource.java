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
import java.util.List;
import java.util.logging.Logger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@CrossOrigin
//@RequestMapping(path = RestConfig.API_URI, "/tasks", //This is not working probably the produces and consumes
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
//        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RequestMapping(path = RestConfig.API_URI)

@Transactional
public class TaskResource
{
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ProjectDao projectDao;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @PostMapping(path = "/tasks")
    public ResponseEntity<Task> create(@RequestBody Task task) throws ResourceNotFoundException
    {
        if (isNull(task.getTaskId()) )
        {
            //Check if the Project ID exists
            Project project = projectDao.findById(task.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id: " + task.getProjectId()));

            if(task.getState().equals("ERROR"))
            {
                LOGGER.warning("Wrong state in new task, not saved.");
                throw new ResourceNotFoundException("Wrong state in new task, not saved.");
            }

            LOGGER.info("Task saved with title: " + task.getTitle() + " for project with id: " + project.getProjectId());
            return ResponseEntity.ok(taskDao.save(task));
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/tasks")
    public ResponseEntity<Task> update(@RequestBody Task task) throws ResourceNotFoundException
    {
        System.out.println("Put Task called");
        if (nonNull(task.getTaskId())) {
            Task found = taskDao.findById(task.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Wrong id: " + task.getTaskId()));
            if(task.getState().equals("ERROR"))
            {
                LOGGER.warning("Wrong state in new task, not saved.");
                throw new ResourceNotFoundException("Wrong state in new task, not saved.");
            }
            else
            {
                found.setState(task.getState());
            }

            //If projectId is found in the JSON and it is not the same with the one already assigned show error.
            if(!found.getProjectId().equals(task.getProjectId()) && task.getProjectId() != null  )
            {
                LOGGER.warning("Project Id cannot change.");
                throw new ResourceNotFoundException("Project Id cannot change.");
            }

            found.setTitle(task.getTitle());
            found.setDescription(task.getDescription());
            found.setStartDate(task.getStartDate());
            found.setCompletedDate(task.getCompletedDate());
            return ResponseEntity.ok(taskDao.save(found));
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping( path = "/tasks/{taskId}")
    public ResponseEntity deleteById(@PathVariable(value = "taskId") Long taskId) throws ResourceNotFoundException
    {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Wrong id: " + taskId));

        if (!task.getState().equals("NOT STARTED"))
        {
            LOGGER.warning("Only tasks that are in state not started can be deleted. state: " + task.getState());
            throw new ResourceNotFoundException("Only tasks that are in state not started can be deleted. state: " + task.getState());
        }

        taskDao.delete(task);
        LOGGER.info("Task with id: " + taskId + " deleted succesfully.");
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/tasks/{taskId}")
    public ResponseEntity findById(@PathVariable("taskId") Long taskId)
    {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok().body(task);
    }

    @GetMapping(path = "/tasks")
    public ResponseEntity<List<Task>> findAll()
    {
        return ResponseEntity.ok(taskDao.findAll());
    }

}
