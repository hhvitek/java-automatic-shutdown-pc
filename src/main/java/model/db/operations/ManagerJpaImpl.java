package model.db.operations;

import model.TaskModel;
import model.TimeManager;
import model.db.repo.ElemNotFoundException;
import model.db.repo.ScheduledTaskEntity;
import model.db.repo.ScheduledTaskRepository;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static model.scheduledtasks.ScheduledTaskStatus.SCHEDULED;

public class ManagerJpaImpl extends Manager {

    public ManagerJpaImpl(@NotNull TaskModel taskModel) {
        super(taskModel);

        startTimer();
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask executableTask, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        if (parameter == null) {
            return new ScheduledTaskJpaImpl(SqliteEntityManagerFactory.createEntityManager(), executableTask, durationDelay);
        } else {
            return new ScheduledTaskJpaImpl(SqliteEntityManagerFactory.createEntityManager(), executableTask, durationDelay, parameter);
        }
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        // nothing to do already updated by hibernate in database
    }

    private @Nullable ScheduledTask createTaskFromAlreadyExistingEntity(@NotNull Integer id) {

        EntityManager entityManager = SqliteEntityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        try {
            ScheduledTaskEntity entity = repository.findOneById(id);
            ExecutableTask executableTask = taskModel.getTaskByName(entity.getTaskTemplate().getName());
            ScheduledTask task = new ScheduledTaskJpaImpl(
                    entityManager,
                    executableTask,
                    entity
            );
            task.addPropertyChangeListener(this);
            return task;
        } catch (ElemNotFoundException e) {
            logger.error("RuntimeError trying to add convert non-existing entity into scheduled task. Every scheduled task should have been already created in db before adding to manager.", e);
            entityManager.close();
            return null;
        }
    }

    @Override
    @NotNull
    public Optional<ScheduledTask> getScheduledTaskById(int id) {
        return Optional.ofNullable(createTaskFromAlreadyExistingEntity(id));
    }

    @Override
    protected @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {

        EntityManager entityManager = SqliteEntityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> entities = repository.findByStatus(status);

        entityManager.close();

        return entities.stream()
                .map(entity -> createTaskFromAlreadyExistingEntity(entity.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllTasks() {

        EntityManager entityManager = SqliteEntityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> tasks = repository.findAll();
        entityManager.close();

        tasks.forEach(
                entity -> deleteScheduledTask(entity.getId())
        );
    }

    @Override
    public List<ScheduledTask> getAllScheduledTasks() {

        EntityManager entityManager = SqliteEntityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> scheduledTasks = repository.findAll();
        entityManager.close();

        return scheduledTasks
                .stream()
                .map(entity -> createTaskFromAlreadyExistingEntity(entity.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    //TIMER##############################################################################################################

    @Override
    protected void recomputeStatusForTasksInScheduledStatus() {
        getAllScheduledTasksByStatus(SCHEDULED).forEach(
                ScheduledTask::recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus
        );
    }
}
