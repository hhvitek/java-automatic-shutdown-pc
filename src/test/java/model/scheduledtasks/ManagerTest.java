package model.scheduledtasks;

import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.ExecutableTask;
import tasks.RemainderTask;
import tasks.RestartTask;
import tasks.ShutdownTask;

import java.util.List;
import java.util.Optional;

import static model.scheduledtasks.ScheduledTaskStatus.CANCELLED;
import static model.scheduledtasks.ScheduledTaskStatus.DELETED;

public abstract class ManagerTest {

    protected final Manager manager;
    protected ScheduledTask shutdownScheduledTask;
    protected ScheduledTask restartScheduledTask;
    protected ScheduledTask remainderScheduledTask;

    protected abstract ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter);

    protected ManagerTest(@NotNull Manager manager) {
        this.manager = manager;
    }

    @BeforeEach
    public void init() {
        ShutdownTask shutdownTask = new ShutdownTask();
        RestartTask restartTask = new RestartTask();
        RemainderTask remainderTask = new RemainderTask();

        TimeManager durationDelay = new TimeManager("01:30");

        shutdownScheduledTask = instantiateNewScheduleTask(shutdownTask, durationDelay, null);
        restartScheduledTask = instantiateNewScheduleTask(restartTask, durationDelay, null);
        remainderScheduledTask = instantiateNewScheduleTask(remainderTask, durationDelay, "parameter value");

        manager.addScheduledTask(shutdownScheduledTask);
        manager.addScheduledTask(restartScheduledTask);
        manager.addScheduledTask(remainderScheduledTask);
    }

    @Test
    public void thereAreThreeScheduledTasksInitializedTest() {
        List<ScheduledTask> tasks = manager.getAllScheduledTasks();
        Assertions.assertEquals(3, tasks.size());
    }

    @Test
    public void removeTaskTest() {
        int id = shutdownScheduledTask.getId();
        manager.deleteScheduledTask(id);

        Assertions.assertEquals(DELETED, shutdownScheduledTask.getStatus());
    }

    @Test
    public void cancelTaskTest() {
        int id = shutdownScheduledTask.getId();
        manager.cancelScheduledTask(id);

        Assertions.assertEquals(CANCELLED, shutdownScheduledTask.getStatus());
    }

    @Test
    public void getTaskByIdTest() {
        int id = shutdownScheduledTask.getId();

        Optional<ScheduledTask> taskOpt = manager.getScheduledTaskById(id);
        Assertions.assertTrue(taskOpt.isPresent());
        Assertions.assertEquals(id, taskOpt.get().getId());
    }

}
