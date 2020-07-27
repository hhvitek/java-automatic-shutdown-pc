package model.scheduledtasks;

import model.AbstractObservableModel;
import model.ScheduledTaskMessenger;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import tasks.TaskException;
import tasks.TaskTemplate;

import java.util.Optional;

public abstract class ScheduledTask extends AbstractObservableModel implements ScheduledTaskMessenger {

    public abstract @NotNull Integer getId();
    public abstract @NotNull TaskTemplate getTaskTemplate();
    public abstract @NotNull String getTaskParameter();
    public abstract  @NotNull TimeManager getWhenElapsed();
    public abstract void execute() throws TaskException;

    public abstract @NotNull String getErrorMessage();
    public abstract @NotNull String getOutput();

    public abstract void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus);
    public abstract @NotNull ScheduledTaskStatus getStatus();
    public abstract void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus();
}
