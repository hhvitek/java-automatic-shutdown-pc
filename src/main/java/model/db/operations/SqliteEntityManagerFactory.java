package model.db.operations;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * The following was determined to be necessary for multiple threaded writers in Sqlite db:
 *  * PRAGMA journal_mode=WAL - allows for writers and readers to coexist
 *  * PRAGMA busy_timeout=millis - any writer will lock database on file system level. Even for readers. This should
 *                                wait additional request/threads for millis before throwing error-exception
 *  * Also there is jpa-level query.timeout that should be configured appropriately
 *  * Also it is required to use separate EntityManagers for every thread
 */
public class SqliteEntityManagerFactory implements EntityManagerFactory {

    private final javax.persistence.EntityManagerFactory entityManagerFactory;

    public SqliteEntityManagerFactory(@NotNull javax.persistence.EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public @NotNull EntityManager createEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        makeSqliteDbWriteAheadLogging(entityManager);
        makeSqliteBusyTimeout(entityManager);
        return entityManager;
    }

    public void makeSqliteBusyTimeout(@NotNull EntityManager entityManager) {
        String query = "PRAGMA busy_timeout=1111;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }

    public void makeSqliteDbWriteAheadLogging(@NotNull EntityManager entityManager) {
        String query = "PRAGMA journal_mode=WAL;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }

}
