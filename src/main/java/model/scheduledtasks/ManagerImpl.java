package model.scheduledtasks;

import model.TaskModel;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages creation, cancellation, execution of scheduled tasks.
 *
 * This class is itself observable and in turn observer to scheduled tasks it manages
 *
 * Scheduled tasks produce events in their lifetime. This manager listens to those events and propagate them to it's
 * observers.
 */
public class ManagerImpl extends Manager {

    private final List<ScheduledTask> scheduledTasks;

    public ManagerImpl(@NotNull TaskModel taskModel) {
        super(taskModel);
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask executableTask, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskImpl(executableTask, durationDelay, parameter);
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        scheduledTasks.add(newScheduledTask);
    }

    @Override
    @NotNull
    public Optional<ScheduledTask> getScheduledTaskById(int id) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getId() == id)
                .findAny();
    }

    @Override
    protected  @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getStatus() == status)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeAllTasks() {
        scheduledTasks.forEach(
                scheduledTask -> deleteScheduledTask(scheduledTask.getId())
        );
    }

    @Override
    public List<ScheduledTask> getAllScheduledTasks() {
        return scheduledTasks.stream().collect(Collectors.toUnmodifiableList());
    }

    //TIMER#####################
    @Override
    protected void recomputeStatusForTasksInScheduledStatus() {
        scheduledTasks.forEach(
                ScheduledTask::recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus
        );
    }


}
