package model.db.operations;

import model.TimeManager;
import model.db.repo.*;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;
import tasks.TaskException;
import tasks.TaskTemplate;

import javax.persistence.EntityManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import java.util.Optional;

import static model.ModelObservableEvents.*;
import static model.scheduledtasks.ScheduledTaskStatus.*;

public class ScheduledTaskJpaImpl extends ScheduledTask {

    private ScheduledTaskEntity entity;
    private ScheduledTaskRepository scheduledTaskRepository;
    private TaskTemplateRepository taskTemplateRepository;
    private ExecutableTask executableTask;

    ScheduledTaskJpaImpl() {

    }

    private ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task) {
        executableTask = task;

        scheduledTaskRepository = new SynchronizedScheduledTaskRepository(entityManager);
        taskTemplateRepository = new TaskTemplateRepository(entityManager);
    }

    public ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task, @NotNull TimeManager whenElapsed, @Nullable String parameter) {
        this(entityManager, task);

        TaskTemplateEntity taskTemplateEntity = findOrCreateTaskTemplate(task);
        entity = new ScheduledTaskEntity(taskTemplateEntity, whenElapsed, parameter);
        scheduledTaskRepository.create(entity);
    }

    public ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task, @NotNull ScheduledTaskEntity entity) {
        this(entityManager, task);

        this.entity = entity;
    }

    private TaskTemplateEntity findOrCreateTaskTemplate(@NotNull ExecutableTask task) {
        try {
            return taskTemplateRepository.findOneById(task.getName());
        } catch (ElemNotFoundException e) {
            return TaskTemplateEntity.fromTaskTemplate(task);
        }
    }


    @Override
    public @NotNull Integer getId() {
        return entity.getId();
    }

    @Override
    public @NotNull TaskTemplate getTaskTemplate() {
        return entity.getTaskTemplate();
    }

    @Override
    public @NotNull Optional<String> getTaskParameter() {
        String parameter = entity.getParameter();
        if (parameter == null || parameter.isBlank()) {
            return Optional.empty();
        } else {
            return Optional.of(parameter);
        }
    }

    @Override
    public @NotNull TimeManager getWhenElapsed() {
        return entity.getWhenElapsed();
    }

    @Override
    public void execute() throws TaskException {
        ExecutableTask task = executableTask;

        try {
            if (task.acceptParameter()) {
                entity.setOutput(
                        task.execute(entity.getParameter())
                );
            } else {
                entity.setOutput(
                        task.execute()
                );
            }
            setStatusIfPossible(EXECUTED_SUCCESS);
            firePropertyChange(SCHEDULED_TASK_FINISHED, getId(), this);
        } catch (TaskException ex) {
            setStatusIfPossible(EXECUTED_ERROR);
            entity.setErrorMessage(ex.toString());
            firePropertyChange(SCHEDULED_TASK_FINISHED_WITH_ERRORS, getId(), this);
            throw ex;
        } finally {
            scheduledTaskRepository.update(entity);
        }
    }

    @Override
    public @NotNull String getErrorMessage() {
        return entity.getErrorMessage();
    }

    @Override
    public @NotNull String getOutput() {
        return entity.getOutput();
    }

    @Override
    public void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus) {
        if (entity.getStatus().isLessThan(newStatus)) {
            entity.setStatus(newStatus);
            scheduledTaskRepository.update(entity);
            firePropertyChange(SCHEDULED_TASK_STATUS_CHANGED, getId(), this);
        }
    }

    @Override
    public @NotNull ScheduledTaskStatus getStatus() {
        return entity.getStatus();
    }

    private boolean isScheduled() {
        return getStatus() == SCHEDULED;
    }

    @Override
    public void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus() {
        if (isScheduled() && entity.getWhenElapsed().hasElapsed()) {
            setStatusIfPossible(ELAPSED);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledTaskJpaImpl that = (ScheduledTaskJpaImpl) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity);
    }
}
