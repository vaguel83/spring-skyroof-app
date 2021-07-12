package com.intrasoft.skyroof.persistence.dao;

import com.intrasoft.skyroof.persistence.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<Project, Long>
{

}
