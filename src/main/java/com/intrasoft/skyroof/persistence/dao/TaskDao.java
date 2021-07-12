package com.intrasoft.skyroof.persistence.dao;

import com.intrasoft.skyroof.persistence.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDao extends JpaRepository<Task, Long>, TaskRepositoryCustom
{

}

