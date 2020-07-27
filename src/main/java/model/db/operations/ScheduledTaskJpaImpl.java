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
import java.util.Objects;
import java.util.Optional;

import static model.ModelObservableEvents.*;
import static model.scheduledtasks.ScheduledTaskStatus.*;

/**
 * Be warned regarding listening to the events...
 * Be warned shared entitymanagers
 */
public class ScheduledTaskJpaImpl extends ScheduledTask {

    private ScheduledTaskEntity entity;
    private ScheduledTaskRepository scheduledTaskRepository;
    private TaskTemplateRepository taskTemplateRepository;
    private ExecutableTask executableTask;

    ScheduledTaskJpaImpl() {

    }

    private ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task) {
        executableTask = task;

        scheduledTaskRepository = new ScheduledTaskRepository(entityManager);
        taskTemplateRepository = new TaskTemplateRepository(entityManager);
    }

    public ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task, @NotNull TimeManager whenElapsed) {
        this(entityManager, task);

        TaskTemplateEntity taskTemplateEntity = findOrCreateTaskTemplate(task);
        entity = new ScheduledTaskEntity(taskTemplateEntity, whenElapsed);
        scheduledTaskRepository.create(entity);
    }

    public ScheduledTaskJpaImpl(@NotNull EntityManager entityManager, @NotNull ExecutableTask task, @NotNull TimeManager whenElapsed, @NotNull String parameter) {
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
    public @NotNull String getTaskParameter() {
        return entity.getParameter();
    }

    @Override
    public @NotNull TimeManager getWhenElapsed() {
        return entity.getWhenElapsed();
    }

    @Override
    public void execute() throws TaskException {
        try {
            String output = executableTask.execute(entity.getParameter());
            if (output != null) {
                entity.setOutput(output);
            }
            setStatusIfPossible(EXECUTED_SUCCESS);
            firePropertyChange(SCHEDULED_TASK_FINISHED, getId(), this);
        } catch (TaskException ex) {
            entity.setErrorMessage(ex.toString());
            setStatusIfPossible(EXECUTED_ERROR);
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
