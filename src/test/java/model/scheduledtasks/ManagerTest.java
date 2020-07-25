package model.scheduledtasks;

import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.ExecutableTask;

import java.util.List;
import java.util.Optional;

import static model.scheduledtasks.ScheduledTaskStatus.CANCELLED;
import static model.scheduledtasks.ScheduledTaskStatus.DELETED;

public abstract class ManagerTest {

    protected Manager manager;
    protected int shutdownId;
    protected int restartId;
    protected int remainderId;

    protected abstract ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter);

    @BeforeEach
    public void init() {
        shutdownId = manager.scheduleTask("Shutdown", null, "01:30");
        restartId = manager.scheduleTask("Restart", null, "02:30");
        remainderId = manager.scheduleTask("Remainder", "this is a parameter", "03:30");
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

}
