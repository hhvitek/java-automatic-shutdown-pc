package model.scheduledtasks;

import model.AbstractObservableModel;
import model.ScheduledTaskMessenger;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import tasks.TaskException;
import tasks.TaskTemplate;

public abstract class ScheduledTask extends AbstractObservableModel implements ScheduledTaskMessenger {

    // messenger, read-only operations

    public abstract @NotNull Integer getId();

    public abstract @NotNull TaskTemplate getTaskTemplate();

    public abstract @NotNull String getTaskParameter();

    public abstract @NotNull TimeManager getWhenElapsed();

    public abstract @NotNull String getErrorMessage();

    public abstract @NotNull String getOutput();

    public abstract @NotNull ScheduledTaskStatus getStatus();

    // write operations

    public abstract void execute() throws TaskException;

    public abstract void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus);

    public abstract void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus();
}
