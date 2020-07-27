import controller.ControllerMainImpl;
import controller.ControllerScheduledTasksImpl;
import model.*;
import model.db.operations.ManagerJpaImpl;
import model.db.operations.SqliteEntityManagerFactory;
import model.db.operations.StateModelJpaImpl;
import model.scheduledtasks.Manager;
import model.scheduledtasks.ManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewMainImpl;
import view.scheduledtasks.ViewScheduledTasksImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainJpa {

    public static final List<String>  ACTIVE_TASKS = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "01:00";
    public static final String LOG_FILENAME = "shutdown.log";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("STARTING JPA");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        TaskModel taskModel = new TaskModelImpl(ACTIVE_TASKS);

        model.db.operations.EntityManagerFactory entityManagerFactory = new SqliteEntityManagerFactory(ENTITY_MANAGER_FACTORY);
        EntityManager stateModelEntityManager = entityManagerFactory.createEntityManager();

        StateModelJpaImpl stateModel = new StateModelJpaImpl(stateModelEntityManager, DEFAULT_AFTERDELTA, DEFAULT_TASK);

        Manager manager = new ManagerJpaImpl(taskModel, entityManagerFactory);

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
