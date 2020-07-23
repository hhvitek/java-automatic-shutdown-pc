package model.scheduledtasks;

import model.Observer;
import model.TaskModel;
import model.TaskModelImpl;
import model.TimeManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.ExecutableTask;
import tasks.ShutdownTask;

import java.util.List;

import static model.scheduledtasks.ScheduledTaskStatus.CREATED;
import static model.scheduledtasks.ScheduledTaskStatus.ELAPSED;
import static model.scheduledtasks.ScheduledTaskStatus.CANCELLED;
import static model.ModelObservableEvents.*;

public abstract class ScheduledTaskTest {

    protected static TaskModel taskModel;

    protected ScheduledTask defaultScheduledTask;
    protected Observer taskObserver;

    @BeforeAll
    protected static void initStatic() {
        List<String> taskPackageAndClassNames = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
        taskModel = new TaskModelImpl(taskPackageAndClassNames);
    }

    @BeforeEach
    protected void init() {
        ExecutableTask task = new ShutdownTask();
        String parameter = "";
        TimeManager durationDelay = new TimeManager("00:30");

        defaultScheduledTask = createScheduledTask(task, durationDelay, parameter);
        taskObserver = new Observer();
        defaultScheduledTask.addPropertyChangeListener(taskObserver);
    }

    protected abstract ScheduledTask createScheduledTask(ExecutableTask task, TimeManager durationDelay, String parameter);

    @Test
    void createTaskTest() {
        Assertions.assertEquals(CREATED, defaultScheduledTask.getStatus());
    }

    @Test
    void checkIdAfterCreationStatusTaskTest() {
        int id = defaultScheduledTask.getId();
        Assertions.assertTrue(id > 0);
    }

    @Test
    void changeStatusTaskTest() {
        defaultScheduledTask.setStatusIfPossible(ELAPSED);
        Assertions.assertEquals(ELAPSED, defaultScheduledTask.getStatus());
    }

    @Test
    void eventUponStatusChangeTest() {
        defaultScheduledTask.setStatusIfPossible(CANCELLED);
        String lastEventObserved = taskObserver.getLastEvent().getPropertyName();
        ScheduledTask scheduledTask = (ScheduledTask) taskObserver.getLastEvent().getNewValue();

        Assertions.assertEquals(SCHEDULED_TASK_STATUS_CHANGED.toString(), lastEventObserved);
        Assertions.assertEquals(CANCELLED, scheduledTask.getStatus());
    }

    @Test
    void noEventIfSameOrLesserStatusToBeSetChangeTest() {
        defaultScheduledTask.setStatusIfPossible(CANCELLED); // event generated
        defaultScheduledTask.setStatusIfPossible(CREATED); // no event generated

        String lastEventObserved = taskObserver.getLastEvent().getPropertyName();
        ScheduledTask scheduledTask = (ScheduledTask) taskObserver.getLastEvent().getNewValue();

        Assertions.assertEquals(SCHEDULED_TASK_STATUS_CHANGED.toString(), lastEventObserved);
        Assertions.assertEquals(CANCELLED, scheduledTask.getStatus()); // its the first CANCELLED event...
    }
}
