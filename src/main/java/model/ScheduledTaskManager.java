package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskException;
import utilities.timer.MyTimer;
import utilities.timer.MyTimerUtilImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static model.ScheduledTaskStatus.*;
import static model.ModelObservableEvents.*;

public class ScheduledTaskManager extends AbstractObservableModel {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskManager.class);
    private static final int DEFAULT_TIMER_TICK_RATE_1s = 1000;

    private final List<ScheduledTask> scheduledTasks;

    private final MyTimer timer;

    public ScheduledTaskManager() {
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
        timer = new MyTimerUtilImpl();
        startTimer();
    }

    public void addScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        scheduledTasks.add(newScheduledTask);
        newScheduledTask.setStatusIfPossible(SCHEDULED);
    }

    public void cancelScheduledTask(int id) {
        setScheduledTaskStatus(id, CANCELLED);
    }

    private void setScheduledTaskStatus(int id, @NotNull ScheduledTaskStatus newStatus) {
        Optional<ScheduledTask> scheduledTaskOpt = getScheduledTaskById(id);
        scheduledTaskOpt.ifPresent(scheduledTask -> scheduledTask.setStatusIfPossible(newStatus));
    }

    private @NotNull Optional<ScheduledTask> getScheduledTaskById(int id) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getId() == id)
                .findAny();
    }

    public void deleteScheduledTask(int id) {
        setScheduledTaskStatus(id, DELETED);
    }

    public @NotNull ScheduledTask getScheduledTask(int id) throws ScheduledTaskNotFoundException {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getId() == id)
                .findAny()
                .orElseThrow(() -> new ScheduledTaskNotFoundException(id));
    }

    public @NotNull List<ScheduledTask> getAllScheduledTasks() {
        return scheduledTasks.stream()
                .collect(Collectors.toUnmodifiableList());
    }

    private @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getStatus() == status)
                .collect(Collectors.toUnmodifiableList());
    }

    public void removeAllTasks() {
        scheduledTasks.forEach(this::deleteTask);
    }

    private void deleteTask(@NotNull ScheduledTask task) {
        task.setStatusIfPossible(DELETED);
    }

    public void removeAllFinishedTasks() {
        getAllScheduledTasksByStatus(EXECUTED_SUCCESS).forEach(
                this::deleteTask
        );
    }

    //TIMER##############################################################################################################

    private void startTimer() {
        logger.debug("Starting utilities.timer...");
        Runnable runnable = this::timerTick;
        timer.scheduleAtFixedRate(runnable, DEFAULT_TIMER_TICK_RATE_1s);
    }

    private void stopTimer() {
        logger.debug("Stopping utilities.timer...");
        timer.stop();
    }

    private void timerTick() {
        recomputeStatusForTasksInScheduledStatus();
        executeElapsedScheduledTasks();

        List<ScheduledTask> tasks = getAllScheduledTasksInScheduledStatus();
        firePropertyChange(TIMER_TICK, tasks.size(), tasks);
    }

    private void recomputeStatusForTasksInScheduledStatus() {
        scheduledTasks.forEach(
                ScheduledTask::recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus
        );
    }

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
