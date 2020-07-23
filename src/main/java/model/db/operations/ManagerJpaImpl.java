package model.db.operations;

import model.db.repo.ElemNotFoundException;
import model.db.repo.ScheduledTaskEntity;
import model.db.repo.ScheduledTaskRepository;
import model.db.repo.SynchronizedScheduledTaskRepository;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.scheduledtasks.ScheduledTaskStatus.SCHEDULED;

public class ManagerJpaImpl extends Manager {

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final EntityManager entityManager;

    public ManagerJpaImpl(@NotNull EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
        scheduledTaskRepository = new SynchronizedScheduledTaskRepository(entityManager);
    }


    @Override
    public void addScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        newScheduledTask.setStatusIfPossible(SCHEDULED);
    }

    @Override
    @NotNull
    public Optional<ScheduledTask> getScheduledTaskById(int id) {
        try {
            ScheduledTaskEntity entity = scheduledTaskRepository.findOneById(id);
            return Optional.of(ScheduledTaskJpaImpl.fromAlreadyExistingEntity(entityManager, entity));
        } catch (ElemNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    protected @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {
        Stream<ScheduledTaskEntity> stream = scheduledTaskRepository.findByStatus(status);
        return stream
                .map(entity -> ScheduledTaskJpaImpl.fromAlreadyExistingEntity(entityManager, entity))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllTasks() {
        Stream<ScheduledTaskEntity> stream = scheduledTaskRepository.findAllAsStream();
        stream.forEach(
                entity -> deleteScheduledTask(entity.getId())
        );
    }

    @Override
    public List<ScheduledTask> getAllScheduledTasks() {
        return scheduledTaskRepository.findAllAsStream()
                .map(entity -> ScheduledTaskJpaImpl.fromAlreadyExistingEntity(entityManager, entity))
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
