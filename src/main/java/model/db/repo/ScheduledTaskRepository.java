package model.db.repo;

import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ScheduledTaskRepository extends AbstractRepository<ScheduledTaskEntity> {

    public ScheduledTaskRepository(@NotNull EntityManager entityManager) {
        super(entityManager, ScheduledTaskEntity.class);
    }

    public List<ScheduledTaskEntity> findByStatus(@NotNull ScheduledTaskStatus status) {
        TypedQuery<ScheduledTaskEntity> query = entityManager.createQuery(
                "SELECT s FROM ScheduledTaskEntity s WHERE s.status = :status",
                ScheduledTaskEntity.class
        ).setParameter("status", status);
        return query.getResultList();
    }
}
