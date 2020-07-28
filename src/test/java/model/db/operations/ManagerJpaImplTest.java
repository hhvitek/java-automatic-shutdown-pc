package model.db.operations;

import model.TaskModel;
import model.TaskModelImpl;
import model.db.repo.ScheduledTaskRepository;
import model.scheduledtasks.ManagerTest;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ManagerJpaImplTest extends ManagerTest {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private static final AbstractEntityManagerFactory entityManagerFactory =
            new SqliteEntityManagerFactoryImpl(ENTITY_MANAGER_FACTORY);

    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private static final ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

    public ManagerJpaImplTest() {
        List<String> tasksPackageAndClassName = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.ReminderTask");
        TaskModel taskModel = new TaskModelImpl(tasksPackageAndClassName);
        manager = new ManagerJpaImpl(taskModel, entityManager);
    }


    @BeforeEach
    @Override
    public void init() {
        repository.deleteAll();
        super.init();
    }
}
