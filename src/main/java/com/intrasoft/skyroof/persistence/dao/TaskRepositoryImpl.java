package com.intrasoft.skyroof.persistence.dao;

import com.intrasoft.skyroof.persistence.model.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

//Each Task has a taskId and a projectId we need this to find all the tasks with a specific projectId.
@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom
{
    EntityManager em;

    @Override
    public List<Task> findTasksByProjectId (Long projectId)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);

        Root<Task> task = cq.from(Task.class);
        Predicate projectIdPredicate = cb.equal(task.get("projectId"), projectId);
        cq.where(projectIdPredicate);

        TypedQuery<Task> query = em.createQuery(cq);
        return query.getResultList();
    }
}
