package model.scheduledtasks;

import model.Observer;
import model.ScheduledTaskMessenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static model.ModelObservableEvents.SCHEDULED_TASK_STATUS_CHANGED;
import static model.scheduledtasks.ScheduledTaskStatus.CANCELLED;
import static model.scheduledtasks.ScheduledTaskStatus.DELETED;

public abstract class ManagerTest {

    protected Manager manager;
    protected Observer observer;
    protected int shutdownId;
    protected int restartId;
    protected int reminderId;

    @BeforeEach
    public void init() {
        shutdownId = manager.scheduleTask("Shutdown", null, "01:30");
        restartId = manager.scheduleTask("Restart", null, "02:30");
        reminderId = manager.scheduleTask("Reminder", "this is a parameter", "03:30");

        observer = new Observer();
        manager.addPropertyChangeListener(observer);
    }

    @Test
    public void thereAreThreeScheduledTasksInitializedTest() {
        List<ScheduledTask> tasks = manager.getAllScheduledTasks();
        Assertions.assertEquals(3, tasks.size());
    }

    @Test
    public void removeTaskTest() {
        manager.deleteScheduledTask(shutdownId);
        Optional<ScheduledTask> task = manager.getScheduledTaskById(shutdownId);

        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(DELETED, task.get().getStatus());
    }

    @Test
    public void cancelTaskTest() {
        manager.cancelScheduledTask(shutdownId);
        Optional<ScheduledTask> task = manager.getScheduledTaskById(shutdownId);

        Assertions.assertTrue(task.isPresent());
        Assertions.assertEquals(CANCELLED, task.get().getStatus());
    }

    @Test
    public void getTaskByIdTest() {
        Optional<ScheduledTask> taskOpt = manager.getScheduledTaskById(shutdownId);
        Assertions.assertTrue(taskOpt.isPresent());
        Assertions.assertEquals(shutdownId, taskOpt.get().getId());
    }

    @Test
    public void eventUponStatusChangeTest() {
        manager.cancelScheduledTask(shutdownId);

        Assertions.assertNotNull(observer.getLastEvent());
        Assertions.assertEquals(SCHEDULED_TASK_STATUS_CHANGED.toString(), observer.getLastEvent().getPropertyName());

        ScheduledTaskMessenger task = (ScheduledTaskMessenger) observer.getLastEvent().getNewValue();
        Assertions.assertEquals(CANCELLED, task.getStatus());
    }

}
