package com.intrasoft.skyroof.persistence.dao;

import com.intrasoft.skyroof.persistence.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepositoryCustom
{
    List<Task> findTasksByProjectId (Long projectId);
}
