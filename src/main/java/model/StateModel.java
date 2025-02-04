package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of a dynamic part of the application excluding ScheduledTasks.
 * * Contains storage for user data
 * * Represents current configuration of the application
 * * If no attribute is selected, methods should return relevant default values.
 */
public abstract class StateModel extends AbstractObservableModel {

    public abstract void setSelectedTaskName(@NotNull String name);

    public abstract @NotNull String getSelectedTaskName();

    public abstract void setSelectedTaskParameter(@NotNull String parameter);

    public abstract @NotNull String getSelectedTaskParameter();

    public abstract void setTimingDurationDelay(@NotNull String durationDelay);

    public abstract @NotNull String getTimingDurationDelay();

    public abstract void setLastScheduledTaskId(int id);

    public abstract int getLastScheduledTaskId();

    public void setSelectedTaskNameAndParameter(@NotNull String taskName, @Nullable String taskParameter) {
        setSelectedTaskName(taskName);
        if (taskParameter == null) {
            setSelectedTaskParameter("");
        } else {
            setSelectedTaskParameter(taskParameter);
        }
    }

}
