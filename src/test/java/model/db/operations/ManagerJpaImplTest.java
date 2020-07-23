package model.db.operations;

import model.TimeManager;
import model.db.repo.ScheduledTaskRepository;
import model.db.repo.SynchronizedScheduledTaskRepository;
import model.scheduledtasks.ManagerTest;
import model.scheduledtasks.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import tasks.ExecutableTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagerJpaImplTest extends ManagerTest {

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private static EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();;
    private static ScheduledTaskRepository repository = new SynchronizedScheduledTaskRepository(entityManager);;

    protected ManagerJpaImplTest() {
        super(new ManagerJpaImpl(entityManager));
    }


    @BeforeEach
    @Override
    public void init() {
        repository.deleteAll();
        super.init();
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskJpaImpl(entityManager, task, durationDelay, parameter);
    }
}
