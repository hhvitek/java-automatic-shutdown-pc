package model.db.repo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

public class TaskTemplateRepository extends AbstractRepository<TaskTemplateEntity> {

    public TaskTemplateRepository(@NotNull EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<TaskTemplateEntity> getEntityClazz() {
        return TaskTemplateEntity.class;
    }
}
