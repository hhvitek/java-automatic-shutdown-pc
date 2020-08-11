package model.db;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * The following was determined to be necessary for multiple threaded writers in Sqlite database:
 * 1] PRAGMA journal_mode=WAL - allows for writers and readers to coexist,
 *                            - sqlite database is spread into three separate files.
 * 2] PRAGMA busy_timeout=millis - any writer will lock database on the file system level. Even for readers.
 *                               - This should stop/wait any additional request/threads for millis before throwing error-exception
 * 3] Also there is Jpa-level query timeout that should be configured appropriately
 * 4] Also it is required to use separate EntityManagers / connections for every thread
 *
 * This class ensures the 1] and 2]. The third one is placed into persistence.xml. The fourth one is ensured by creation
 * of only two entityManagers for the whole application. One for app-user operations and the other one for scheduled periodic
 * operation that is automatically executed every X seconds and may result into write into DB.
 */
public class SqliteEntityManagerFactoryImpl extends AbstractEntityManagerFactory {

    public SqliteEntityManagerFactoryImpl(@NotNull EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    public @NotNull EntityManager createEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        makeSqliteDbWriteAheadLogging(entityManager);
        makeSqliteBusyTimeout(entityManager);
        return entityManager;
    }

    public void makeSqliteDbWriteAheadLogging(@NotNull EntityManager entityManager) {
        String query = "PRAGMA journal_mode=WAL;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }

    public void makeSqliteBusyTimeout(@NotNull EntityManager entityManager) {
        String query = "PRAGMA busy_timeout=1111;";

        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(query).getResultList();
        entityManager.getTransaction().commit();
    }
}
