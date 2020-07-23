package model;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a dynamic part of the application excluding ScheduledTasks.
 *     * Contains storage for the user data
 *     * Represents current configuration of the application
 *     * If no attribute selected, methods should return relevant default values.
 */
public interface StateModel {

    void setSelectedTaskName(@NotNull String name);
    @NotNull String getSelectedTaskName();

    void setSelectedTaskParameter(@NotNull String parameter);
    @NotNull String getSelectedTaskParameter();

    void setTimingDurationDelay(@NotNull String durationDelay);
    @NotNull String getTimingDurationDelay();

    void setLastScheduledTaskId(int id);
    int getLastScheduledTaskId();

}
