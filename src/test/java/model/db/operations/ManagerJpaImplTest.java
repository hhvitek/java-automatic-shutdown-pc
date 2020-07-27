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


    private static EntityManager entityManager = SqliteEntityManagerFactory.createEntityManager();

    private static ScheduledTaskRepository repository = new SynchronizedScheduledTaskRepository(entityManager);;

    public ManagerJpaImplTest() {
        List<String> tasksPackageAndClassName = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
        TaskModel taskModel = new TaskModelImpl(tasksPackageAndClassName);
        manager = new ManagerJpaImpl(taskModel);
    }


    @BeforeEach
    @Override
    public void init() {
        repository.deleteAll();
        super.init();
    }
}
