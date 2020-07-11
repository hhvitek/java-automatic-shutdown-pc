package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateModelImpl implements StateModel {

    public static String DEFAULT_DURATION_DELTA = "00:30";

    private static final Logger logger = LoggerFactory.getLogger(StateModelImpl.class);


    private String defaultDurationDelta;
    private String defaultSelectedTaskName;

    private String scheduledTaskName = "";
    private String selectedTaskName = "";
    private TimeManager timeManager;

    public StateModelImpl() {
        this(DEFAULT_DURATION_DELTA, "");
    }

    public StateModelImpl(@NotNull String defaultDurationDelta, @NotNull String defaultSelectedTaskName) {
        this.defaultDurationDelta = defaultDurationDelta;
        this.defaultSelectedTaskName = defaultSelectedTaskName;

        restoreInitialState();
    }

    public void restoreInitialState() {
        logger.debug("Restoring initial StateModel state.");
        setScheduledTaskToDefault();
        setSelectedTaskNameToDefault();
    }

    private void setScheduledTaskToDefault() {
        setScheduledTask("", defaultDurationDelta);
    }

    private void setSelectedTaskNameToDefault() {
        setSelectedTaskName(defaultSelectedTaskName);
    }

    @Override
    public void setSelectedTaskName(@NotNull String name) {
        logger.debug("The new scheduled task: <{}>", name);
        this.selectedTaskName = name;
    }

    @Override
    public @NotNull String getSelectedTaskName() {
        return selectedTaskName;
    }

    @Override
    public void setScheduledTask(@NotNull String name, @NotNull String durationDelay) {
        logger.debug("The new scheduled task: <{}>:<{}>", name, durationDelay);
        scheduledTaskName = name;
        timeManager = new TimeManager(durationDelay);
    }


    @Override
    public @NotNull String getScheduledTaskName() {
        return scheduledTaskName;
    }

    @Override
    public void cancelScheduledTask() {
        if (isScheduled()) {
            logger.info("Scheduled task <{}> cancelled.", scheduledTaskName);
            restoreInitialState();
        }
    }

    @Override
    public @NotNull TimeManager getTimeManager() {
        return timeManager;
    }

    @Override
    public boolean hasElapsed() {
        return timeManager.hasElapsed();
    }

    @Override
    public boolean isScheduled() {
        return scheduledTaskName != null && !scheduledTaskName.isBlank() && timeManager != null;
    }
}
