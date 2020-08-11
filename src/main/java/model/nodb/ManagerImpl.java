package model.nodb;

import model.TaskModel;
import model.TimeManager;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of Manager using just Java's synchronizedList
 * No persistence implemented...
 */
public class ManagerImpl extends Manager {

    private final List<ScheduledTask> scheduledTasks;

    public ManagerImpl(@NotNull TaskModel taskModel) {
        super(taskModel);
        scheduledTasks = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    protected @NotNull ScheduledTask instantiateNewScheduleTask(
            @NotNull ExecutableTask executableTask,
            @NotNull TimeManager durationDelay,
            @Nullable String parameter)
    {
        if (parameter == null) {
            return new ScheduledTaskImpl(executableTask, durationDelay);
        } else {
            return new ScheduledTaskImpl(executableTask, durationDelay, parameter);
        }
    }

    @Override
    protected void addNewScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        scheduledTasks.add(newScheduledTask);
    }

    @Override
    public void deleteAllScheduledTasks() {
        scheduledTasks.forEach(
                scheduledTask -> deleteScheduledTask(scheduledTask.getId())
        );
    }

    @Override
    public @NotNull Optional<ScheduledTask> getScheduledTaskById(int id) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getId() == id)
                .findAny();
    }

    @Override
    public @NotNull List<ScheduledTask> getAllScheduledTasks() {
        return scheduledTasks.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    protected @NotNull List<ScheduledTask> getAllScheduledTasksByStatus(@NotNull ScheduledTaskStatus status) {
        return scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getStatus() == status)
                .collect(Collectors.toUnmodifiableList());
    }

    //##############################ManagerTimerOperations##############################################################

    @Override
    public void recomputeStatusForTasksInScheduledStatus() {
        scheduledTasks.forEach(
                ScheduledTask::recomputeStatusIfTaskHasElapsedChangeIntoElapsedStatus
        );
    }
}
