package model.scheduledtasks;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface to access Scheduled task status through Manager object
 * * Change status SCHEDULED to ELAPSED
 * * Execute ELAPSED tasks.
 */
public interface ManagerTimerOperations {

    void recomputeStatusForTasksInScheduledStatus();

    void executeElapsedScheduledTasks();

    @NotNull List<ScheduledTask> getAllScheduledTasksInScheduledStatus();
}
