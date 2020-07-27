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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static model.scheduledtasks.ScheduledTaskStatus.SCHEDULED;

public class ManagerJpaImpl extends Manager {

    private final EntityManagerFactory entityManagerFactory;

    public ManagerJpaImpl(@NotNull TaskModel taskModel, @NotNull EntityManagerFactory entityManagerFactory) {
        super(taskModel);

        this.entityManagerFactory = entityManagerFactory;

        startTimer();
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask executableTask, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        if (parameter == null) {
            return new ScheduledTaskJpaImpl(entityManagerFactory.createEntityManager(), executableTask, durationDelay);
        } else {
            return new ScheduledTaskJpaImpl(entityManagerFactory.createEntityManager(), executableTask, durationDelay, parameter);
        }
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        // nothing to do already updated by hibernate in database
    }

    private @Nullable ScheduledTask createTaskFromAlreadyExistingEntity(@NotNull ScheduledTaskEntity existingEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ExecutableTask executableTask = taskModel.getTaskByName(existingEntity.getTaskTemplate().getName());
        ScheduledTask task = new ScheduledTaskJpaImpl(
                entityManager,
                executableTask,
                existingEntity
        );
        task.addPropertyChangeListener(this);
        return task;
    }

    @Override
    @NotNull
    public Optional<ScheduledTask> getScheduledTaskById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        try {
            ScheduledTaskEntity entity = repository.findOneById(id);
            ScheduledTask task = createTaskFromAlreadyExistingEntity(entity);
            return Optional.of(task);
        } catch (ElemNotFoundException e) {
            logger.warn("Trying to get task that doesn't exist. Id: <{}>", id);
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    protected @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> entities = repository.findByStatus(status);

        entityManager.close();

        return entities.stream()
                .map(entity -> createTaskFromAlreadyExistingEntity(entity))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllTasks() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> tasks = repository.findAll();
        entityManager.close();

        tasks.forEach(
                entity -> deleteScheduledTask(entity.getId())
        );
    }

    @Override
    public List<ScheduledTask> getAllScheduledTasks() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ScheduledTaskRepository repository = new ScheduledTaskRepository(entityManager);

        List<ScheduledTaskEntity> scheduledTasks = repository.findAll();
        entityManager.close();

        return scheduledTasks
                .stream()
                .map(entity -> createTaskFromAlreadyExistingEntity(entity))
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
