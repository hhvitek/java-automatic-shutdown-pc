package model.scheduledtasks;

import model.TaskModel;
import model.TaskModelImpl;

import java.util.List;

class ManagerImplTest extends ManagerTest {

    public ManagerImplTest() {
        List<String> tasksPackageAndClassName = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.ReminderTask");
        TaskModel taskModel = new TaskModelImpl(tasksPackageAndClassName);
        manager = new ManagerImpl(taskModel);
    }
}
