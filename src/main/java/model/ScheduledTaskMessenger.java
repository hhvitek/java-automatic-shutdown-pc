package model;

import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import tasks.TaskTemplate;

import java.util.Optional;

public interface ScheduledTaskMessenger {

    Integer getId();

    TaskTemplate getTaskTemplate();

    String getTaskParameter();

    TimeManager getWhenElapsed();

    String getErrorMessage();

    String getOutput();

    ScheduledTaskStatus getStatus();
}
