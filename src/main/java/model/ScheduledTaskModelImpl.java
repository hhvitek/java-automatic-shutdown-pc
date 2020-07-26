package model;

import model.scheduledtasks.Manager;
import model.scheduledtasks.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Implementation of a scheduled task logic
 *     * Manages scheduled tasks - scheduling, executing, cancelling etc.
 *     * Any change to scheduled tasks should be propagated using observer pattern - {@see ModelObservableEvents}
 *       (such as: java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport)
 */
public class ScheduledTaskModelImpl extends AbstractObservableModel implements PropertyChangeListener {

    protected final Manager scheduledTaskManager;

    public ScheduledTaskModelImpl(@NotNull Manager manager) {
        this.scheduledTaskManager = manager;
        manager.addPropertyChangeListener(this);
    }

    public int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay)
            throws TaskNotFoundException {
        return scheduledTaskManager.scheduleTask(name, parameter, durationDelay);
    }

    public void cancelScheduledTask(int id) {
        scheduledTaskManager.cancelScheduledTask(id);
    }


    public void deleteScheduledTask(int id) {
        scheduledTaskManager.deleteScheduledTask(id);
    }


    public @NotNull ScheduledTaskMessenger getScheduledTask(int id) throws ScheduledTaskNotFoundException {
        ScheduledTask scheduledTask = scheduledTaskManager.getScheduledTaskByIdThrowOnError(id);
        return ScheduledTaskMessenger.createFromScheduledTask(scheduledTask);
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
