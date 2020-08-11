package model.db;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Abstract parent class used for creating new connections into underline databases.
 * Allows for a specific configuration for all connections based on an underline database system. Simultaneously it allows
 * for unified usage...
 */
public abstract class AbstractEntityManagerFactory {

    protected final EntityManagerFactory entityManagerFactory;

    protected AbstractEntityManagerFactory(@NotNull EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public abstract @NotNull EntityManager createEntityManager();
}
