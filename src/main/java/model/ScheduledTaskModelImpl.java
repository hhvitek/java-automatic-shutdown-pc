package model;

import model.scheduledtasks.Manager;
import model.scheduledtasks.ManagerTimerOperations;
import model.scheduledtasks.PeriodicTaskUpdater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the Scheduled task logic
 * * Manages scheduled tasks - scheduling, executing, cancelling etc.
 * * Any change to scheduled tasks should be propagated using observer pattern - {@see ModelObservableEvents}
 * (such as: java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport)
 * <p>
 * * Divided into two parts: user-driven and autonomous-periodic-task
 * * user-part manages user actions
 * * autonomous represents "countdown"/"timer" of the application
 * * every period change state of elapsed task from SCHEDULED to ELAPSED
 * * executes every ELAPSED task
 */
public class ScheduledTaskModelImpl extends AbstractObservableModel implements PropertyChangeListener {

    protected final Manager manager;
    protected final PeriodicTaskUpdater periodicTaskUpdater;

    public ScheduledTaskModelImpl(@NotNull Manager manager, @NotNull ManagerTimerOperations managerTimerOperations) {
        this.manager = manager;
        manager.addPropertyChangeListener(this);

        periodicTaskUpdater = new PeriodicTaskUpdater(managerTimerOperations);
        periodicTaskUpdater.addPropertyChangeListener(this);
    }

    /**
     * Starts autonomous-periodic-task
     */
    public void startTimer() {
        periodicTaskUpdater.startTimer();
    }

    public int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay)
            throws TaskNotFoundException {
        return manager.scheduleTask(name, parameter, durationDelay);
    }

    public void cancelScheduledTask(int id) {
        manager.cancelScheduledTask(id);
    }

    public void deleteScheduledTask(int id) {
        manager.deleteScheduledTask(id);
    }

    public void deleteAllScheduledTasks() {
        manager.deleteAllScheduledTasks();
    }

    public void deleteAllFinishedScheduledTasks() {
        manager.deleteAllFinishedScheduledTasks();
    }

    public @NotNull ScheduledTaskMessenger getScheduledTaskByIdThrowOnError(int id) throws ScheduledTaskNotFoundException {
        return manager.getScheduledTaskByIdThrowOnError(id);
    }

    public @NotNull List<ScheduledTaskMessenger> getAllScheduledTasks() {
        List<? extends ScheduledTaskMessenger> base = manager.getAllScheduledTasks();
        return (List<ScheduledTaskMessenger>) base;
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
