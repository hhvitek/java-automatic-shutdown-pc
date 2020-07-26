package model.db.operations;

import model.TaskModel;
import model.TimeManager;
import model.db.repo.ElemNotFoundException;
import model.db.repo.ScheduledTaskEntity;
import model.db.repo.ScheduledTaskRepository;
import model.db.repo.SynchronizedScheduledTaskRepository;
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
import java.util.stream.Stream;

import static model.scheduledtasks.ScheduledTaskStatus.SCHEDULED;

public class ManagerJpaImpl extends Manager {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final EntityManager entityManager;

    public ManagerJpaImpl(@NotNull EntityManager entityManager, @NotNull TaskModel taskModel) {
        super(taskModel);
        this.entityManager = entityManager;
        scheduledTaskRepository = new SynchronizedScheduledTaskRepository(entityManager);
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask executableTask, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskJpaImpl(entityManager, executableTask, durationDelay, parameter);
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        // nothing to do already updated by hibernate in database
    }

    private @Nullable ScheduledTask createTaskFromAlreadyExistingEntity(@NotNull Integer id) {
        try {
            ScheduledTaskEntity entity = scheduledTaskRepository.findOneById(id);
            ExecutableTask executableTask = taskModel.getTaskByName(entity.getTaskTemplate().getName());
            return new ScheduledTaskJpaImpl(
                    entityManager,
                    executableTask,
                    entity
            );
        } catch (ElemNotFoundException e) {
            logger.error("RuntimeError trying to add convert non-existing entity into scheduled task. Every scheduled task should have been already created in db before adding to manager.", e);
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
        Stream<ScheduledTaskEntity> stream = scheduledTaskRepository.findByStatus(status);
        return stream
                .map(entity -> createTaskFromAlreadyExistingEntity(entity.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllTasks() {
        List<ScheduledTaskEntity> tasks = scheduledTaskRepository.findAll();
        tasks.forEach(
                entity -> deleteScheduledTask(entity.getId())
        );
    }

    @Override
    public List<ScheduledTask> getAllScheduledTasks() {
        return scheduledTaskRepository.findAll()
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
