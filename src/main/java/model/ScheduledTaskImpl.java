package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Task;
import tasks.TaskException;

import java.util.Objects;
import java.util.Optional;

import static model.ModelObservableEvents.*;
import static model.ScheduledTaskStatus.*;

public class ScheduledTaskImpl extends AbstractObservableModel implements ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskImpl.class);
    private static int sequenceId = 1000;

    private final int id;
    private final Task task;
    private final TimeManager whenElapsed;

    private String parameter = "";

    private String output = "";
    private String errorMessage = "";

    private ScheduledTaskStatus status;

    public ScheduledTaskImpl(@NotNull Task task, @NotNull TimeManager whenElapsed) {
        id = getNextSequenceId();
        this.task = task;
        this.whenElapsed = whenElapsed;
        status = CREATED;

        logger.info("The new task has been scheduled: <{}>.", this);
    }

    public ScheduledTaskImpl(@NotNull Task task, @NotNull TimeManager whenElapsed, @Nullable String parameter) {
        this(task, whenElapsed);
        this.parameter = parameter;
    }

    private static int getNextSequenceId() {
        return sequenceId++;
    }

    @Override
    public @NotNull Integer getId() {
        return id;
    }

    @Override
    public @NotNull TaskTemplate getTaskTemplate() {
        return task;
    }

    @Override
    public @NotNull Optional<String> getTaskParameter() {
        return Optional.ofNullable(parameter);
    }

    @Override
    public @NotNull TimeManager getWhenElapsed() {
        return new TimeManager(whenElapsed);
    }

    @Override
    public void execute() throws TaskException {
        try {
            if (task.acceptParameter()) {
                output = task.execute(parameter);
            } else {
                output = task.execute();
            }
            setStatusIfPossible(EXECUTED_SUCCESS);
            firePropertyChange(SCHEDULED_TASK_FINISHED, id, this);
        } catch (TaskException ex) {
            setStatusIfPossible(EXECUTED_ERROR);
            errorMessage = ex.toString();
            firePropertyChange(SCHEDULED_TASK_FINISHED_WITH_ERRORS, id, this);
            throw ex;
        }
    }

    @Override
    public @NotNull String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public @NotNull String getOutput() {
        return output;
    }

    @Override
    public void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus) {
        if (status.isLessThan(newStatus)) {
            status = newStatus;
            firePropertyChange(SCHEDULED_TASK_STATUS_CHANGED, id, this);
        }
    }

    @Override
    public @NotNull ScheduledTaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean isScheduled() {
        return status == SCHEDULED;
    }

    @Override
    public void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus() {
        if (isScheduled() && whenElapsed.hasElapsed()) {
            setStatusIfPossible(ELAPSED);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScheduledTaskImpl that = (ScheduledTaskImpl) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("ScheduledTask Id: <%d>, Name: <%s>, Status: <%s>, WhenElapsed: <%s>",
                id,
                task.getName(),
                status,
                whenElapsed
        );
    }
}
