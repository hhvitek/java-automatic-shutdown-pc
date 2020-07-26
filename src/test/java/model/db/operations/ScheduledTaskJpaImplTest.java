package model.db.operations;

import model.TimeManager;
import model.db.repo.ElemNotFoundException;
import model.db.repo.ScheduledTaskEntity;
import model.db.repo.ScheduledTaskRepository;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import model.scheduledtasks.ScheduledTaskTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.ExecutableTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class ScheduledTaskJpaImplTest extends ScheduledTaskTest {

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private static EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();

    private static ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

    @Override
    protected ScheduledTask createScheduledTask(ExecutableTask task, TimeManager durationDelay, String parameter) {
        return new ScheduledTaskJpaImpl(entityManager, task, durationDelay, parameter);
    }

    @Test
    public void manuallyChangingStatusResultsInImmediatelyChangeInDbTest() throws ElemNotFoundException {
        defaultScheduledTask.setStatusIfPossible(ScheduledTaskStatus.DELETED);

        Assertions.assertEquals(ScheduledTaskStatus.DELETED, defaultScheduledTask.getStatus());


        ScheduledTaskEntity scheduledTask = repository.findOneById(defaultScheduledTask.getId());
        Assertions.assertEquals(ScheduledTaskStatus.DELETED, scheduledTask.getStatus());
    }
}
