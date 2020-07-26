package model.scheduledtasks;

import model.TaskModel;
import model.TaskModelImpl;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import java.util.List;

class ManagerImplTest extends ManagerTest{

    public ManagerImplTest() {
        List<String> tasksPackageAndClassName = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
        TaskModel taskModel = new TaskModelImpl(tasksPackageAndClassName);
        manager = new ManagerImpl(taskModel);
    }
}
