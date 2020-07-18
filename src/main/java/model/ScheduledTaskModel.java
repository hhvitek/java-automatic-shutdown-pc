package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Implementation of a scheduled task logic
 *     * Manages scheduled tasks - scheduling, executing, cancelling etc.
 *     * Any change to scheduled tasks should be propagated using observer pattern
 *       (such as: java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport)
 */
public interface ScheduledTaskModel {

    /**
     * @param name the name of a task to schedule
     * @param parameter the parameter for a task to schedule, may be null as no parameter
     * @param durationDelay If null the default value is chosen.
     * @return scheduled task identification
     */
    int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) throws TaskNotFoundException;
    void cancelScheduledTask(int id);
    void deleteScheduledTask(int id);
    @NotNull ScheduledTask getScheduledTask(int id) throws ScheduledTaskNotFoundException;

    void removeAllTasks();
    void removeAllFinishedTasks();
    @NotNull List<ScheduledTask> getAllScheduledTask();
}
