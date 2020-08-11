import controller.ControllerMainImpl;
import controller.ControllerScheduledTasksImpl;
import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import ini.myini.CustomIIniConfig;
import model.*;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ManagerImpl;
import model.scheduledtasks.ScheduledTaskMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.SwingViewUtils;
import view.main.MainWindow;
import view.tasks.TasksWindow;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class Main {

    public static final List<String> ACTIVE_TASKS = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.ReminderTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "01:00";

    public static final IIniConfig iniConfig = new CustomIIniConfig();

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, InvalidConfigFileFormatException {
        logger.info("************STARTING configuration*************");

        SwingViewUtils.setLookAndFeelToSystemDefault();
        SwingViewUtils.setDefaultFont();
        loadIniFile();

        List<String> activeTasks = getActiveTasksFromConfigFile();
        String defaultTask = iniConfig.getValue("tasks", "default_task");
        String defaultAfterDelta = iniConfig.getValue("tasks", "default_afterdelta");

        if (activeTasks.isEmpty()) {
            activeTasks = ACTIVE_TASKS;
        }
        if (defaultTask == null) {
            defaultTask = DEFAULT_TASK;
        }
        if (defaultAfterDelta == null) {
            defaultAfterDelta = DEFAULT_AFTERDELTA;
        }

        TaskModel taskModel = new TaskModelImpl(activeTasks);
        StateModel stateModel = new StateModelImpl(defaultAfterDelta, defaultTask);

        Manager manager = new ManagerImpl(taskModel);

        executeCommonAfter(taskModel, stateModel, manager, manager);

        logger.info("************FINISHED configuration*************");
    }

    public static void loadIniFile() throws IOException, InvalidConfigFileFormatException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        File iniFile = new File(loader.getResource("configuration.ini").getFile());

        iniConfig.load(iniFile);
    }

    public static List<String> getActiveTasksFromConfigFile() {
        String iniTasksValue = iniConfig.getValue("tasks", "package_class_names");
        if (iniTasksValue != null) {
            return List.of(iniTasksValue.split("\\s*,\\s*"));
        }
        return Collections.emptyList();
    }


    public static void executeCommonAfter(TaskModel taskModel, StateModel stateModel, Manager userManager, Manager periodicManager) {
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
