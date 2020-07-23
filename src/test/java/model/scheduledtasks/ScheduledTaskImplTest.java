package model.scheduledtasks;

import model.TimeManager;
import tasks.ExecutableTask;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledTaskImplTest extends ScheduledTaskTest{

    @Override
    protected ScheduledTask createScheduledTask(ExecutableTask task, TimeManager durationDelay, String parameter) {
        return new ScheduledTaskImpl(task, durationDelay, parameter);
    }
}
