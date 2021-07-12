package com.intrasoft.skyroof.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "project")
@Table(name = "project")
public class Project extends Creation implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrePersist
    public void prePersist(){
        super.prePersist();
    }

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
