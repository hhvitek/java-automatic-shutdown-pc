package model;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a dynamic part of the application. This represents "the logic".
 *     * Contains storage for the user data
 *     * Represents current configuration of the application
 */
public interface StateModel {

    void setSelectedTaskName(@NotNull String name);
    @NotNull String getSelectedTaskName();

    void setTimingDurationDelay(@NotNull String durationDelay);
    @NotNull String getTimingDurationDelay();

    void setLastScheduledTaskId(int id);
    int getLastScheduledTaskId();

}
