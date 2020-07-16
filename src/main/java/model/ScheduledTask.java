package model;

import org.jetbrains.annotations.NotNull;

public interface ScheduledTask {

    TaskTemplate getTaskTemplate();
    String getTaskParameter();
    TimeManager getWhenElapsed();
    String execute();

    void setStatus(@NotNull ScheduledTaskStatus newStatus);
    ScheduledTaskStatus getStatus();
}
