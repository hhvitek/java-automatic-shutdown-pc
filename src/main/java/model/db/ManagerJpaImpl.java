package model.db;

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
import java.util.Optional;
import java.util.stream.Collectors;

import static model.scheduledtasks.ScheduledTaskStatus.SCHEDULED;

/**
 * Implementation of Manager
 * Persistence implemented using Jpa persistence
 */
public class ManagerJpaImpl extends Manager {

    private final EntityManager entityManager;
    private final ScheduledTaskRepository repository;

    public ManagerJpaImpl(@NotNull TaskModel taskModel, @NotNull EntityManager entityManager) {
        super(taskModel);

        this.entityManager = entityManager;
        repository = new ScheduledTaskRepository(entityManager);
    }

    @Override
    protected @NotNull ScheduledTask instantiateNewScheduleTask(
            @NotNull ExecutableTask executableTask,
            @NotNull TimeManager durationDelay,
            @Nullable String parameter)
    {
        return new ScheduledTaskJpaImpl(entityManager, executableTask, durationDelay, parameter);
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        // nothing to do already updated by hibernate in database
    }

    @Override
    public void deleteAllScheduledTasks() {
        repository.findAll()
                .forEach(
                        entity -> deleteScheduledTask(entity.getId())
                );
    }

    @Override
    public @NotNull Optional<ScheduledTask> getScheduledTaskById(int id) {
        try {
            ScheduledTaskEntity entity = repository.findOneById(id);
            ScheduledTask task = createTaskFromAlreadyExistingEntity(entity);
            return Optional.of(task);
        } catch (ElemNotFoundException e) {
            logger.warn("Trying to get task that doesn't exist. Id: <{}>", id);
            return Optional.empty();
        }
    }

    private @NotNull ScheduledTask createTaskFromAlreadyExistingEntity(@NotNull ScheduledTaskEntity existingEntity) {
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
    public @NotNull List<ScheduledTask> getAllScheduledTasks() {
        return repository.findAll()
                .stream()
                .map(this::createTaskFromAlreadyExistingEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    protected @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::createTaskFromAlreadyExistingEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    //##############################ManagerTimerOperations##############################################################

    @Override
    public void recomputeStatusForTasksInScheduledStatus() {
        getAllScheduledTasksByStatus(SCHEDULED).forEach(
                ScheduledTask::recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus
        );
    }
}
