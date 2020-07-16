package controller;

import model.StateModel;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewImpl;

import java.util.Timer;
import java.util.TimerTask;

public class ControllerImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

    private final StateModel stateModel;
    private ViewImpl view;

    private Timer timer;
    private static long TICK_IN_MS = 1000L;

    public ControllerImpl(@NotNull StateModel stateModel) {
        this.stateModel = stateModel;
    }

    public void setView(@NotNull ViewImpl view) {
        this.view = view;
    }

    public void actionExitByUser() {

    }

    public void actionNewTaskScheduledByUser(@NotNull String taskName, @NotNull String strTimingDelay, @Nullable String taskParameter) {
        stateModel.setScheduledTask(taskName, strTimingDelay);
        startTimer();
    }

    private void timerTick() {
        TimeManager durationDelay = stateModel.getTimeManager();
        view.refreshTimingCountdowns(durationDelay);

        if (durationDelay.hasElapsed()) {
            stopTimer();
            stateModel.restoreInitialState();
            view.refreshTimingCountdowns(stateModel.getTimeManager());
        }
    }

    private void startTimer() {
        if (isTimerRunning()) {
            logger.error("The timer is already running. Cannot start the new one.");
        } else {
            timer = new Timer();
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            timerTick();
                        }
                    },
                    0L,
                    TICK_IN_MS

            );
        }
    }

    private void stopTimer() {
        if (isTimerRunning()) {
            timer.cancel();
            timer = null;
        }
    }

    public void actionCancelScheduledTaskByUser() {
        if (isTimerRunning()) {
            stopTimer();
            stateModel.cancelScheduledTask();
        }
    }

    private boolean isTimerRunning() {
        return timer != null;
    }

    public void actionNewTaskSelectedByUser(@NotNull String newTaskName) {
        setModelProperty("SelectedTaskName", newTaskName);
    }

    public void changedTimingByUser(@NotNull String newValue) {
        setModelProperty("TimeManager", newValue);
    }
}
