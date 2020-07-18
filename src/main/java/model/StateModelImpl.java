package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static model.ModelObservableEvents.*;

public class StateModelImpl extends AbstractObservableModel implements StateModel {

    private static final String DEFAULT_TIMING_DURATION_DELAY = "01:00";

    private static final Logger logger = LoggerFactory.getLogger(StateModelImpl.class);

    private final String defaultTimingDurationDelay;
    private final String defaultSelectedTaskName;

    // data in ui
    private String selectedTaskName = "";
    private String timingDurationDelay;

    private int lastScheduledTaskId;


    public StateModelImpl() {
        this(DEFAULT_TIMING_DURATION_DELAY, "");
    }

    public StateModelImpl(@NotNull String defaultDurationDelay, @NotNull String defaultSelectedTaskName) {
        defaultTimingDurationDelay = defaultDurationDelay;
        this.defaultSelectedTaskName = defaultSelectedTaskName;
        timingDurationDelay = defaultTimingDurationDelay;

        restoreInitialState();
    }

    private void restoreInitialState() {
        logger.debug("Restoring initial StateModel state.");
        setSelectedTaskNameToDefault();
    }

    private void setSelectedTaskNameToDefault() {
        setSelectedTaskName(defaultSelectedTaskName);
    }

    @Override
    public void setSelectedTaskName(@NotNull String name) {
        String oldSelectedTaskName = selectedTaskName;
        selectedTaskName = name;

        logger.debug("The new selected task: <{}> -> <{}>", oldSelectedTaskName, selectedTaskName);
        firePropertyChange(SELECTED_TASK_CHANGED, oldSelectedTaskName, selectedTaskName);
    }

    @Override
    public @NotNull String getSelectedTaskName() {
        return selectedTaskName;
    }

    @Override
    public void setTimingDurationDelay(@NotNull String durationDelay) {
        String oldTimingDurationDelay = timingDurationDelay;
        timingDurationDelay = durationDelay;

        logger.debug("The new timingDurationDelay: <{}> -> <{}>", oldTimingDurationDelay, durationDelay);
        firePropertyChange(CHOSEN_TIMING_DURATION_DELAY_CHANGED, oldTimingDurationDelay, durationDelay);
    }

    @Override
    public @NotNull String getTimingDurationDelay() {
        return timingDurationDelay;
    }

    @Override
    public void setLastScheduledTaskId(int id) {
        int oldValue = lastScheduledTaskId;
        lastScheduledTaskId = id;
        firePropertyChange(LAST_SCHEDULED_TASK_CHANGED, oldValue, lastScheduledTaskId);
    }

    @Override
    public int getLastScheduledTaskId() {
        return lastScheduledTaskId;
    }
}
