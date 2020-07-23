package model;

import model.scheduledtasks.ManagerImpl;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

/**
 * Uses Java's java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport to implements observer pattern
 *
 * Creates scheduled tasks, uses ScheduledTaskManager to manage created scheduled tasks
 *
 * Observes scheduled tasks events.
 */
public class ScheduledTaskModelImpl extends ScheduledTaskModel {

    public ScheduledTaskModelImpl(@NotNull TaskModel taskModel) {
        super(new ManagerImpl(), taskModel);
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskImpl(task, durationDelay, parameter);
    }
}
