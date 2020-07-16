package model;

import org.jetbrains.annotations.NotNull;

public interface StateModel {

    void setSelectedTaskName(@NotNull String name);
    @NotNull String getSelectedTaskName();

    /**
     * Initialize datetime when should expire.
     * now() + durationDelay
     *
     * @param name
     * @param durationDelay If null the default value is chosen.
     */
    void setScheduledTask(@NotNull String name, @NotNull String durationDelay);
    @NotNull String getScheduledTaskName();

    void cancelScheduledTask();
    void restoreInitialState();

    @NotNull TimeManager getTimeManager();
    boolean hasElapsed();
    boolean isScheduled();

}
