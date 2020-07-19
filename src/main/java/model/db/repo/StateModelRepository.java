package model.db.repo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

public class StateModelRepository extends AbstractRepository<StateModelEntity> {

    public StateModelRepository(@NotNull EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Class<StateModelEntity> getEntityClazz() {
        return StateModelEntity.class;
    }
}
