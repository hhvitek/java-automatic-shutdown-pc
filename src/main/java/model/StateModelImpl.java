package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static model.ModelPropertiesEnum.*;

public class StateModelImpl extends AbstractModel implements StateModel  {

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

    @Override
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
        String oldSelectedTaskName = selectedTaskName;
        selectedTaskName = name;

        logger.debug("The new selected task: <{}> -> <{}>", oldSelectedTaskName, selectedTaskName);
        firePropertyChange(SELECTED_TASK, oldSelectedTaskName, selectedTaskName);
    }

    @Override
    public @NotNull String getSelectedTaskName() {
        return selectedTaskName;
    }

    @Override
    public void setScheduledTask(@NotNull String name, @NotNull String durationDelay) {
        String oldScheduledTaskName = scheduledTaskName;
        scheduledTaskName = name;

        logger.debug("The new scheduled task: <{}> -> <{}>:<{}>", oldScheduledTaskName, scheduledTaskName, durationDelay);
        firePropertyChange(SCHEDULED_TASK, oldScheduledTaskName, scheduledTaskName);

        setTimeManager(durationDelay);
    }

    //TODO this is public method?
    public void setTimeManager(@NotNull String durationDelay) {
        logger.debug("The new durationDelay init: <{}>", durationDelay);
        TimeManager oldTimeManager = timeManager;
        timeManager = new TimeManager(durationDelay);

        firePropertyChange(TIME_DURATION_DELAY, oldTimeManager, timeManager);
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
