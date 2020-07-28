package model.db.operations;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class AbstractEntityManagerFactory {

    protected final EntityManagerFactory entityManagerFactory;

    protected AbstractEntityManagerFactory(@NotNull EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public abstract @NotNull EntityManager createEntityManager();
}
