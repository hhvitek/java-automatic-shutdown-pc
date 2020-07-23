package model.db.operations;

import model.TimeManager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskTest;
import tasks.ExecutableTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class ScheduledTaskJpaImplTest extends ScheduledTaskTest {

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    @Override
    protected ScheduledTask createScheduledTask(ExecutableTask task, TimeManager durationDelay, String parameter) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        return new ScheduledTaskJpaImpl(entityManager, task, durationDelay, parameter);
    }
}
