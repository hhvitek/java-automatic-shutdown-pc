package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StateModel {

    void setSelectedTaskName(@NotNull String name);
    @NotNull String getSelectedTaskName();

    /**
     * @param name the name of a task to schedule
     * @param parameter the parameter for a task to schedule, may be null as no parameter
     * @param durationDelay If null the default value is chosen.
     */
    void setScheduledTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay);
    @NotNull String getScheduledTaskName();

    void cancelScheduledTask();

    @NotNull TimeManager getTimeManager();
    boolean hasElapsed();
    boolean isScheduled();

    void setTimingDurationDelay(@NotNull String durationDelay);
    String getTimingDurationDelay();

}
