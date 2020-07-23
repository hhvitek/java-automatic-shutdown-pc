package model.scheduledtasks;

import model.AbstractObservableModel;
import model.ScheduledTaskNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskException;
import utilities.timer.MyTimer;
import utilities.timer.MyTimerUtilImpl;

import java.util.List;
import java.util.Optional;

import static model.scheduledtasks.ScheduledTaskStatus.*;

abstract public class Manager extends AbstractObservableModel {

    protected static final Logger logger = LoggerFactory.getLogger(Manager.class);
    private static final int DEFAULT_TIMER_TICK_RATE_1s = 1000;

    private final MyTimer timer;

    protected Manager() {
        timer = new MyTimerUtilImpl();
        startTimer();
    }

    public abstract void addScheduledTask(@NotNull ScheduledTask newScheduledTask);

    public void cancelScheduledTask(int id) {
        setScheduledTaskStatus(id, CANCELLED);
    }

    public void deleteScheduledTask(int id) {
        setScheduledTaskStatus(id, DELETED);
    }

    public void removeAllFinishedTasks() {
        getAllScheduledTasksByStatus(EXECUTED_SUCCESS).forEach(
                scheduledTask -> deleteScheduledTask(scheduledTask.getId())
        );
    }

    protected void setScheduledTaskStatus(int id, @NotNull ScheduledTaskStatus newStatus) {
        Optional<ScheduledTask> scheduledTaskOpt = getScheduledTaskById(id);
        scheduledTaskOpt.ifPresent(scheduledTask -> scheduledTask.setStatusIfPossible(newStatus));
    }

    public abstract  @NotNull Optional<ScheduledTask> getScheduledTaskById(int id);

    public @NotNull ScheduledTask getScheduledTaskByIdThrowOnError(int id) throws ScheduledTaskNotFoundException {
        return getScheduledTaskById(id)
                .orElseThrow(() -> new ScheduledTaskNotFoundException(id));
    }

    public abstract void removeAllTasks();

    public abstract List<ScheduledTask> getAllScheduledTasks();


    //TIMER##############################################################################################################

    private void startTimer() {
        logger.debug("Starting utilities timer...");
        Runnable runnable = this::timerTick;
        timer.scheduleAtFixedRate(runnable, DEFAULT_TIMER_TICK_RATE_1s);
    }

    private void stopTimer() {
        logger.debug("Stopping utilities timer...");
        timer.stop();
    }

    private void timerTick() {
        recomputeStatusForTasksInScheduledStatus();
        executeElapsedScheduledTasks();

        //List<ScheduledTask> tasks = getAllScheduledTasksInScheduledStatus();
        //firePropertyChange(TIMER_TICK, tasks.size(), tasks);
    }

    protected abstract void recomputeStatusForTasksInScheduledStatus();
    protected abstract List<ScheduledTask> getAllScheduledTasksByStatus(ScheduledTaskStatus elapsed);

    private void executeElapsedScheduledTasks() {
        for(ScheduledTask task: getAllTasksInElapsedStatus()) {
            try {
                task.execute();
            } catch (TaskException e) {
                logger.error("Failed to execute task: <{}>", task.getTaskTemplate().getName());
            }
        }
    }

    private List<ScheduledTask> getAllTasksInElapsedStatus() {
        return getAllScheduledTasksByStatus(ELAPSED);
    }

    private List<ScheduledTask> getAllScheduledTasksInScheduledStatus() {
        return getAllScheduledTasksByStatus(SCHEDULED);
    }
}
