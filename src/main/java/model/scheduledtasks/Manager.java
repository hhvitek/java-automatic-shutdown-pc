package model.scheduledtasks;

import model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.ExecutableTask;
import tasks.TaskException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;

import static model.ModelObservableEvents.SCHEDULED_TASK_CREATED;
import static model.scheduledtasks.ScheduledTaskStatus.*;

/**
 * Manages creation, cancellation, and PARTLY execution of scheduled tasks.
 * !!!
 * !!! Provides interface to existing ScheduledTasks - ManagerTimerOperations,
 * !!!  it's expected that SOMEONE other shall periodically use this interface
 * !!!  to check on and change ScheduledTasks state and executes them accordingly.
 * !!!
 * <p>
 * This class is itself observable and in turn observer to scheduled tasks it manages
 * <p>
 * Scheduled tasks produce events in their lifetime. This manager listens to those events and propagate them to it's
 * observers.
 */
public abstract class Manager extends AbstractObservableModel implements ManagerTimerOperations, PropertyChangeListener {

    protected static final Logger logger = LoggerFactory.getLogger(Manager.class);

    protected final TaskModel taskModel;

    protected Manager(@NotNull TaskModel taskModel) {
        this.taskModel = taskModel;
    }

    /**
     * @param name          the name of a task to schedule. If task does not exist, it throws exception.
     * @param parameter     the parameter for a task to schedule, may be null as no parameter
     * @param durationDelay when to schedule task from now() in the format %H:%M 01:00
     * @return scheduled task's unique identification
     * @throws TaskNotFoundException if no task is found.
     */
    public int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay)
            throws TaskNotFoundException {
        ScheduledTask scheduledTask = createScheduleTask(name, parameter, durationDelay);
        return scheduledTask.getId();
    }

    private @NotNull ScheduledTask createScheduleTask(
            @NotNull String name,
            @Nullable String parameter,
            @NotNull String durationDelay) throws TaskNotFoundException {
        ExecutableTask task = taskModel.getTaskByName(name);
        TimeManager timeManager = new TimeManager(durationDelay);

        ScheduledTask scheduledTask = instantiateNewScheduleTask(task, timeManager, parameter);
        scheduledTask.setStatusIfPossible(SCHEDULED);
        scheduledTask.addPropertyChangeListener(this);
        addNewScheduledTask(scheduledTask);

        firePropertyChange(SCHEDULED_TASK_CREATED, scheduledTask.getId(), scheduledTask);

        return scheduledTask;
    }

    protected abstract @NotNull ScheduledTask instantiateNewScheduleTask(
            @NotNull ExecutableTask executableTask,
            @NotNull TimeManager durationDelay,
            @Nullable String parameter
    );

    protected abstract void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask);

    public void cancelScheduledTask(int id) {
        setScheduledTaskStatus(id, CANCELLED);
    }

    private void setScheduledTaskStatus(int id, @NotNull ScheduledTaskStatus newStatus) {
        Optional<ScheduledTask> scheduledTaskOpt = getScheduledTaskById(id);
        scheduledTaskOpt.ifPresent(scheduledTask -> scheduledTask.setStatusIfPossible(newStatus));
    }

    public void deleteScheduledTask(int id) {
        setScheduledTaskStatus(id, DELETED);
    }

    public void deleteAllFinishedScheduledTasks() {
        getAllScheduledTasksByStatus(EXECUTED_SUCCESS).forEach(
                scheduledTask -> deleteScheduledTask(scheduledTask.getId())
        );
    }

    public abstract void deleteAllScheduledTasks();

    public abstract @NotNull Optional<ScheduledTask> getScheduledTaskById(int id);

    public @NotNull ScheduledTask getScheduledTaskByIdThrowOnError(int id) throws ScheduledTaskNotFoundException {
        return getScheduledTaskById(id)
                .orElseThrow(() -> new ScheduledTaskNotFoundException(id));
    }

    public abstract @NotNull List<ScheduledTask> getAllScheduledTasks();

    protected abstract @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(ScheduledTaskStatus status);

    //##############################ManagerTimerOperations##############################################################

    @Override
    public void executeElapsedScheduledTasks() {
        for (ScheduledTask task : getAllTasksInElapsedStatus()) {
            try {
                task.execute();
            } catch (TaskException e) {
                logger.error("Failed to execute task: <{}>", task.getTaskTemplate().getName());
            }
        }
    }

    private @NotNull List<ScheduledTask> getAllTasksInElapsedStatus() {
        return getAllScheduledTasksByStatus(ELAPSED);
    }

    @Override
    public @NotNull List<ScheduledTask> getAllScheduledTasksInScheduledStatus() {
        return getAllScheduledTasksByStatus(SCHEDULED);
    }

    //##############################Propagate events from ScheduledTasks################################################

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(
                ModelObservableEvents.valueOf(evt.getPropertyName()),
                evt.getOldValue(),
                evt.getNewValue()
        );
    }
}
