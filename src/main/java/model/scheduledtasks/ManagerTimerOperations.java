package model.scheduledtasks;

import model.ModelObservableEvents;
import model.ScheduledTaskMessenger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface to access Scheduled task status through Manager object
 *  * Change status of SCHEDULED to ELAPSED
 *  * Execute ELAPSED tasks.
 */
public interface ManagerTimerOperations {

    void recomputeStatusForTasksInScheduledStatus();
    void executeElapsedScheduledTasks();
    @NotNull List<ScheduledTask> getAllScheduledTasksInScheduledStatus();
}
