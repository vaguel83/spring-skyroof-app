package com.intrasoft.skyroof.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "task")
@Table(name = "task")
public class Task extends Creation implements Serializable
{
    private static final long serialVersionUID = 1L;

    @PrePersist
    public void prePersist(){
        super.prePersist();
    }

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    @ManyToOne( fetch = FetchType.EAGER)
    private Project project;

    @Column(name = "project_id" , nullable = false)
    private Long projectId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    @Column(name = "state", length = 11, nullable = false)
    private String state;

    @Column(name = "start_date", length = 10, nullable = false)
    private LocalDate startDate;

    @Column(name = "completed_date", length = 10, nullable = false)
    private LocalDate completedDate;

    public Long getTaskId() {
        return taskId;
    }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        if(     state.equals("NOT STARTED")||
                state.equals("IN PROGRESS")||
                state.equals("COMPLETED") )
        {
            this.state = state;
        }
        else
        {
            this.state = "ERROR";
        }
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId){ this.projectId = projectId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate){ this.startDate = startDate; }

    public LocalDate getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDate completedDate){ this.completedDate = completedDate; }

}
