package model.scheduledtasks;

import model.AbstractObservableModel;
import model.ModelObservableEvents;
import org.jetbrains.annotations.NotNull;
import utilities.timer.MyTimer;
import utilities.timer.MyTimerUtilImpl;

import java.util.List;

/**
 * This is the SOMEONE mentioned in Manager class.
 * <p>
 * Periodically executed timerTick() method that:
 * * changes status of elapsed ScheduledTasks from SCHEDULED to ELAPSED
 * * executes all ELAPSED tasks.
 */
public class PeriodicTaskUpdater extends AbstractObservableModel {

    // this doesn't really need to run every second. If precision is not relevant it could run event every X minutes...
    private static final int DEFAULT_TIMER_TICK_RATE = 10000;

    private final ManagerTimerOperations manager;
    private final MyTimer timer;

    public PeriodicTaskUpdater(@NotNull ManagerTimerOperations manager) {
        this.manager = manager;
        timer = new MyTimerUtilImpl();
    }

    public void startTimer() {
        timer.scheduleAtFixedRate(
                this::timerTick,
                DEFAULT_TIMER_TICK_RATE
        );
    }

    public void stopTimer() {
        timer.stop();
    }

    private void timerTick() {
        manager.recomputeStatusForTasksInScheduledStatus();
        manager.executeElapsedScheduledTasks();
    }
}
