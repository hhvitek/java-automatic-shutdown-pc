import controller.ControllerMainImpl;
import controller.ControllerScheduledTasksImpl;
import model.*;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.main.MainWindow;
import view.SwingViewUtils;
import view.tasks.TasksWindow;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {

    public static final List<String> ACTIVE_TASKS = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.ReminderTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "01:00";
    public static final String LOG_FILENAME = "shutdown.log";

    public static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("************STARTING configuration*************");

        SwingViewUtils.setLookAndFeelToSystemDefault();
        SwingViewUtils.setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        TaskModel taskModel = new TaskModelImpl(ACTIVE_TASKS);
        StateModel stateModel = new StateModelImpl(DEFAULT_AFTERDELTA, DEFAULT_TASK);

        Manager manager = new ManagerImpl(taskModel);

        executeCommon(taskModel, stateModel, manager, manager);

        logger.info("************FINISHED configuration*************");
    }

    static void executeCommon(TaskModel taskModel, StateModel stateModel, Manager userManager, Manager periodicManager) {
        ScheduledTaskModelImpl scheduledTaskModel = new ScheduledTaskModelImpl(userManager, periodicManager);

        ControllerScheduledTasksImpl controllerScheduledTasks = new ControllerScheduledTasksImpl(scheduledTaskModel);

        ControllerMainImpl controller = new ControllerMainImpl(stateModel, scheduledTaskModel, controllerScheduledTasks);
        MainWindow mainView = new MainWindow(controller, taskModel);

        List<ScheduledTaskMessenger> alreadyExistingTasks = scheduledTaskModel.getAllScheduledTasks();
        TasksWindow taskView = new TasksWindow(controllerScheduledTasks, alreadyExistingTasks);

        controller.setViews(mainView, taskView);
        controller.run();

        scheduledTaskModel.startTimer();
    }


}
