package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.Task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static model.ModelObservableEvents.SCHEDULED_TASK_CREATED;

/**
 * Uses Java's java.beans.PropertyChangeListener, java.beans.PropertyChangeSupport to implements observer pattern
 *
 * Creates scheduled tasks, uses ScheduledTaskManager to manage created scheduled tasks
 *
 * Observes scheduled tasks events.
 */
public class ScheduledTaskModelImpl extends AbstractObservableModel implements ScheduledTaskModel, PropertyChangeListener {

    private final ScheduledTaskManager scheduledTaskManager;
    private final TaskModel taskModel;

    public ScheduledTaskModelImpl(@NotNull TaskModel taskModel) {
        this.taskModel = taskModel;

        scheduledTaskManager = new ScheduledTaskManager();
        scheduledTaskManager.addPropertyChangeListener(this);
    }

    @Override
    public int scheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) throws TaskNotFoundException {
        ScheduledTask scheduledTask = createScheduleTask(name, parameter, durationDelay);
        scheduledTaskManager.addScheduledTask(scheduledTask);
        return scheduledTask.getId();
    }

    private ScheduledTask createScheduleTask(@NotNull String name, @Nullable String parameter, @NotNull String durationDelay) throws TaskNotFoundException {
        Task task = taskModel.getTaskByName(name);
        TimeManager timeManager = new TimeManager(durationDelay);

        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl(task, timeManager, parameter);
        scheduledTask.addPropertyChangeListener(this);

        firePropertyChange(SCHEDULED_TASK_CREATED, scheduledTask.getId(), scheduledTask);

        return scheduledTask;
    }

    @Override
    public void cancelScheduledTask(int id) {
        scheduledTaskManager.cancelScheduledTask(id);
    }

    @Override
    public void deleteScheduledTask(int id) {
        scheduledTaskManager.deleteScheduledTask(id);
    }

    @Override
    public @NotNull ScheduledTask getScheduledTask(int id) throws ScheduledTaskNotFoundException {
        return scheduledTaskManager.getScheduledTask(id);
    }

    @Override
    public void removeAllTasks() {
        scheduledTaskManager.removeAllTasks();
    }

    @Override
    public void removeAllFinishedTasks() {
        scheduledTaskManager.removeAllFinishedTasks();
    }

    @Override
    public @NotNull List<ScheduledTask> getAllScheduledTask() {
        return scheduledTaskManager.getAllScheduledTasks();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(ModelObservableEvents.valueOf(evt.getPropertyName()), evt.getOldValue(), evt.getNewValue());
    }
}
