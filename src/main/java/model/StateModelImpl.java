package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timer.MyTimer;
import timer.MyTimerUtil;

import static model.ModelPropertiesEnum.*;

public class StateModelImpl extends AbstractModel implements StateModel  {

    public static String DEFAULT_TIMING_DURATION_DELAY = "01:00";
    private static long DEFAULT_TIMER_TICK_RATE = 1000L;
    private static final Logger logger = LoggerFactory.getLogger(StateModelImpl.class);

    private String defaultTimingDurationDelay;
    private String defaultSelectedTaskName;

    // data in ui
    private String scheduledTaskName = "";
    private String selectedTaskName = "";
    private String timingDurationDelay;

    private String scheduledTaskParameter = null;
    // countdown to this point of time
    private TimeManager timeManager;

    private final MyTimer timer;

    public StateModelImpl() {
        this(DEFAULT_TIMING_DURATION_DELAY, "");
    }

    public StateModelImpl(@NotNull String defaultDurationDelay, @NotNull String defaultSelectedTaskName) {
        defaultTimingDurationDelay = defaultDurationDelay;
        this.defaultSelectedTaskName = defaultSelectedTaskName;
        timingDurationDelay = DEFAULT_TIMING_DURATION_DELAY;
        timer = new MyTimerUtil();

        restoreInitialState();
    }

    private void restoreInitialState() {
        logger.debug("Restoring initial StateModel state.");
        setScheduledTaskToDefault();
        setSelectedTaskNameToDefault();
        stopTimer();
    }

    private void setScheduledTaskToDefault() {
        setScheduledTask("", null, defaultTimingDurationDelay);
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
    public void setScheduledTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) {
        String oldScheduledTaskName = scheduledTaskName;
        scheduledTaskName = name;

        logger.debug("The new scheduled task: <{}> -> <{}>:<{}>", oldScheduledTaskName, scheduledTaskName, durationDelay);
        firePropertyChange(SCHEDULED_TASKNAME_CHANGED, oldScheduledTaskName, scheduledTaskName);

        setScheduledTaskParameter(parameter);

        setTimeManager(durationDelay);

        startTimer();
    }

    private void setScheduledTaskParameter(@Nullable String newParameter) {
        String oldParameter = scheduledTaskParameter;
        scheduledTaskParameter = newParameter;

        logger.debug("The new scheduled task parameter: <{}> -> <{}>", oldParameter, scheduledTaskParameter);

        firePropertyChange(SCHEDULED_TASKPARAMETER_CHANGED, oldParameter, scheduledTaskParameter);
    }

    //TODO this is public method?
    private void setTimeManager(@NotNull String durationDelay) {
        logger.debug("The new durationDelay init: <{}>", durationDelay);
        TimeManager oldTimeManager = timeManager;
        timeManager = new TimeManager(durationDelay);
    }

    @Override
    public @NotNull String getScheduledTaskName() {
        return scheduledTaskName;
    }

    @Override
    public void cancelScheduledTask() {
        if (isScheduled()) {
            stopTimer();
            logger.info("Scheduled task <{}> cancelled.", scheduledTaskName);
            restoreInitialState();
        }
    }

    @Override
    public @NotNull TimeManager getTimeManager() {
        return timeManager.clone();
    }

    @Override
    public boolean hasElapsed() {
        return timeManager.hasElapsed();
    }

    @Override
    public boolean isScheduled() {
        return scheduledTaskName != null && !scheduledTaskName.isBlank() && timeManager != null && timer.isRunning();
    }

    @Override
    public void setTimingDurationDelay(@NotNull String timingDurationDelay) {
        String oldTimingDurationDelay = this.timingDurationDelay;
        this.timingDurationDelay = timingDurationDelay;

        logger.debug("The new timingDurationDelay: <{}> -> <{}>", oldTimingDurationDelay, timingDurationDelay);
        firePropertyChange(CHOSEN_TIMING_DURATION_DELAY_CHANGED, oldTimingDurationDelay, timingDurationDelay);
    }

    @Override
    public String getTimingDurationDelay() {
        return timingDurationDelay;
    }

    private void startTimer() {
        if (!isScheduled()) {
            logger.info("Starting timer for the new scheduled task: <{}>:<{}>", scheduledTaskName, timeManager.getRemainingDurationInHHMMSS_ifElapsedZeros());
            Runnable runnable = () -> timerTick();
            timer.scheduleAtFixedRate(runnable, DEFAULT_TIMER_TICK_RATE);
        }
    }

    private void stopTimer() {
        timer.stop();
    }

    private void timerTick() {
        logger.debug("Model Timer tick");
        firePropertyChange(TIMER_TICK, null, timeManager);

        if (hasElapsed()) {
            logger.info("The running timer has elapsed: <{}>", scheduledTaskName);
            stopTimer();
        }
    }
}
