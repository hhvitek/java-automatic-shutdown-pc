package model.scheduledtasks;

import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.ExecutableTask;
import tasks.TaskException;
import tasks.TaskTemplate;

import java.util.Objects;

import static model.ModelObservableEvents.*;
import static model.scheduledtasks.ScheduledTaskStatus.*;

/**
 * ScheduledTask's implementation using Java variables as storage...
 * Id of a scheduled task is auto-generated upon creation - in a constructor
 */
public class ScheduledTaskImpl extends ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskImpl.class);
    private static int sequenceId = 1000;

    private final int id;
    private final ExecutableTask executableTask;
    private final TimeManager whenElapsed;

    private String parameter = "";
    private String output = "";
    private String errorMessage = "";
    private ScheduledTaskStatus status = CREATED;

    public ScheduledTaskImpl(@NotNull ExecutableTask executableTask, @NotNull TimeManager whenElapsed) {
        id = getNextSequenceId();
        this.executableTask = executableTask;
        this.whenElapsed = whenElapsed;
    }

    private static int getNextSequenceId() {
        return sequenceId++;
    }

    public ScheduledTaskImpl(@NotNull ExecutableTask executableTask, @NotNull TimeManager whenElapsed, @NotNull String parameter) {
        this(executableTask, whenElapsed);
        this.parameter = parameter;
    }

    @Override
    public @NotNull Integer getId() {
        return id;
    }

    @Override
    public @NotNull TaskTemplate getTaskTemplate() {
        return executableTask;
    }

    @Override
    public @NotNull String getTaskParameter() {
        return parameter;
    }

    @Override
    public @NotNull TimeManager getWhenElapsed() {
        return new TimeManager(whenElapsed);
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
    public @NotNull ScheduledTaskStatus getStatus() {
        return status;
    }

    @Override
    public void execute() throws TaskException {
        try {
            if (executableTask.canAcceptParameter()) {
                output = executableTask.execute(parameter);
            } else {
                output = executableTask.execute();
            }
            setStatusIfPossible(EXECUTED_SUCCESS);
            firePropertyChange(SCHEDULED_TASK_FINISHED, id, this);
        } catch (TaskException ex) {
            errorMessage = ex.toString();
            setStatusIfPossible(EXECUTED_ERROR);
            firePropertyChange(SCHEDULED_TASK_FINISHED_WITH_ERRORS, id, this);
            throw ex;
        }
    }

    @Override
    public void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus) {
        if (status.isLessThan(newStatus)) {
            status = newStatus;
            firePropertyChange(SCHEDULED_TASK_STATUS_CHANGED, id, this);
        }
    }

    @Override
    public void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus() {
        if (isScheduled() && whenElapsed.hasElapsed()) {
            setStatusIfPossible(ELAPSED);
        }
    }

    private boolean isScheduled() {
        return status == SCHEDULED;
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
                executableTask.getName(),
                status,
                whenElapsed
        );
    }
}
