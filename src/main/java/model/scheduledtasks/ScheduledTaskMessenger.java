package model.scheduledtasks;

import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import tasks.TaskTemplate;

/**
 * ScheduledTask without execution etc. Just access to data
 */
public interface ScheduledTaskMessenger {

    @NotNull Integer getId();

    @NotNull TaskTemplate getTaskTemplate();

    @NotNull String getTaskParameter();

    @NotNull TimeManager getWhenElapsed();

    @NotNull String getErrorMessage();

    @NotNull String getOutput();

    @NotNull ScheduledTaskStatus getStatus();
}
