import controller.ControllerMainImpl;
import controller.ControllerScheduledTasksImpl;
import model.*;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewMainImpl;
import view.scheduledtasks.ViewScheduledTasksImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {

    public static final List<String>  ACTIVE_TASKS = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "01:00";
    public static final String LOG_FILENAME = "shutdown.log";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("STARTING");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));


        TaskModel taskModel = new TaskModelImpl(ACTIVE_TASKS);
        StateModelImpl stateModel = new StateModelImpl(DEFAULT_AFTERDELTA, DEFAULT_TASK);

        Manager manager = new ManagerImpl(taskModel);
        ScheduledTaskModelImpl scheduledTaskModel = new ScheduledTaskModelImpl(manager);

        ControllerMainImpl controller = new ControllerMainImpl(stateModel, scheduledTaskModel);
        controller.addModel(stateModel);
        controller.addModel(scheduledTaskModel);

        ViewMainImpl view = new ViewMainImpl(controller, taskModel);

        ControllerScheduledTasksImpl controllerScheduledTasks = new ControllerScheduledTasksImpl(scheduledTaskModel);
        controllerScheduledTasks.addModel(scheduledTaskModel);

        List<ScheduledTaskMessenger> alreadyExistingTasks = scheduledTaskModel.getAllScheduledTasks();
        ViewScheduledTasksImpl viewScheduledTasks = new ViewScheduledTasksImpl(controllerScheduledTasks, alreadyExistingTasks);
        //TODO isssue when this view is bind to main controller instead of his own controller
        controller.addView(viewScheduledTasks);

        controller.run();

        logger.info("FINISHED");
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
