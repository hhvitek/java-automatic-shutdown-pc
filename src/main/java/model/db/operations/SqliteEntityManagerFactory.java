package model.db.operations;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class SqliteEntityManagerFactory {

    private static final javax.persistence.EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private SqliteEntityManagerFactory() {

    }

    public static EntityManager createEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        makeSqliteDbWriteAheadLogging(entityManager);
        makeSqliteBusyTimeout(entityManager);
        return entityManager;
    }

    public static void makeSqliteBusyTimeout(@NotNull EntityManager entityManager) {
        String query = "PRAGMA busy_timeout=1111;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }

    public static void makeSqliteDbWriteAheadLogging(@NotNull EntityManager entityManager) {
        String query = "PRAGMA journal_mode=WAL;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }

}
