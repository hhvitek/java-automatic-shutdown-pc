package model;

import org.jetbrains.annotations.NotNull;
import tasks.TaskException;

import java.util.Optional;

public interface ScheduledTask {

    @NotNull Integer getId();
    @NotNull TaskTemplate getTaskTemplate();
    @NotNull Optional<String> getTaskParameter();
    @NotNull TimeManager getWhenElapsed();
    void execute() throws TaskException;

    @NotNull String getErrorMessage();
    @NotNull String getOutput();

    void setStatusIfPossible(@NotNull ScheduledTaskStatus newStatus);
    @NotNull ScheduledTaskStatus getStatus();
    boolean isScheduled();
    void recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus();
}
