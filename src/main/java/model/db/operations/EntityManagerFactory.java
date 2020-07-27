package model.db.operations;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

public interface EntityManagerFactory {

    @NotNull EntityManager createEntityManager();
}
