package model.db.operations;

import model.TaskModel;
import model.TaskModelImpl;
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
import java.util.List;

public class ManagerJpaImplTest extends ManagerTest {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private static final model.db.operations.EntityManagerFactory entityManagerFactory =
            new SqliteEntityManagerFactory(ENTITY_MANAGER_FACTORY);

    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private static final ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

    public ManagerJpaImplTest() {
        List<String> tasksPackageAndClassName = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
        TaskModel taskModel = new TaskModelImpl(tasksPackageAndClassName);
        manager = new ManagerJpaImpl(taskModel, entityManagerFactory);
    }


    @BeforeEach
    @Override
    public void init() {
        repository.deleteAll();
        super.init();
    }
}
