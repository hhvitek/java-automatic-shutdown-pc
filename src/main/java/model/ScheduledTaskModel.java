package model;

import model.scheduledtasks.Manager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static model.ModelObservableEvents.SCHEDULED_TASK_CREATED;

/**
 * Implementation of a scheduled task logic
 *     * Manages scheduled tasks - scheduling, executing, cancelling etc.
 *     * Any change to scheduled tasks should be propagated using observer pattern - {@see ModelObservableEvents}
 *       (such as: java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport)
 */
public abstract class ScheduledTaskModel extends AbstractObservableModel implements PropertyChangeListener {

    protected final Manager scheduledTaskManager;
    protected final TaskModel taskModel;

    protected ScheduledTaskModel(@NotNull Manager manager, @NotNull TaskModel taskModel) {
        this.scheduledTaskManager = manager;
        manager.addPropertyChangeListener(this);
        this.taskModel = taskModel;
    }


    /**
     * @param name the name of a task to schedule. If task not exist throws exception.
     * @param parameter the parameter for a task to schedule, may be null as no parameter
     * @param durationDelay when to schedule task from now() in the format %H:%M 01:00
     * @return scheduled task unique identification
     * @throws TaskNotFoundException if no task is found.
     */
    public int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) throws TaskNotFoundException {
        ScheduledTask scheduledTask = createScheduleTask(name, parameter, durationDelay);
        scheduledTaskManager.addScheduledTask(scheduledTask);
        return scheduledTask.getId();
    }

    protected ScheduledTask createScheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) throws TaskNotFoundException {
        ExecutableTask task = taskModel.getTaskByName(name);
        TimeManager timeManager = new TimeManager(durationDelay);

        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl(task, timeManager, parameter);
        scheduledTask.addPropertyChangeListener(this);

        firePropertyChange(SCHEDULED_TASK_CREATED, scheduledTask.getId(), scheduledTask);

        return scheduledTask;
    }

    protected abstract ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter);

    public void cancelScheduledTask(int id) {
        scheduledTaskManager.cancelScheduledTask(id);
    }


    public void deleteScheduledTask(int id) {
        scheduledTaskManager.deleteScheduledTask(id);
    }


    public @NotNull ScheduledTask getScheduledTask(int id) throws ScheduledTaskNotFoundException {
        return scheduledTaskManager.getScheduledTaskByIdThrowOnError(id);
    }


    public void removeAllTasks() {
        scheduledTaskManager.removeAllTasks();
    }


    public void removeAllFinishedTasks() {
        scheduledTaskManager.removeAllFinishedTasks();
    }

    /**
     * Propagate events from scheduled tasks and manager
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(
                ModelObservableEvents.valueOf(evt.getPropertyName()),
                evt.getOldValue(),
                evt.getNewValue()
        );
    }
}
