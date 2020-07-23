package model.scheduledtasks;

import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import static org.junit.jupiter.api.Assertions.*;

class ManagerImplTest extends ManagerTest{

    public ManagerImplTest() {
        super(new ManagerImpl());
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskImpl(task, durationDelay, parameter);
    }
}
