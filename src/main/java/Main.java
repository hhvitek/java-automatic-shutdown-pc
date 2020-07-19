import controller.ControllerImpl;
import model.ScheduledTaskModelImpl;
import model.TaskModel;
import model.TaskModelImpl;
import model.db.operations.StateModelJpaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.reflection.ReflectionApi;
import view.ViewImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {

    public static final List<String>  ACTIVE_TASKS = List.of("tasks.ShutdownTask", "tasks.RestartTask", "tasks.RemainderTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "01:00";
    public static final String LOG_FILENAME = "shutdown.log";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("STARTING");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        ReflectionApi.shouldLog(false);

        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager(); // Retrieve an application managed entity manager


        TaskModel taskModel = new TaskModelImpl(ACTIVE_TASKS);
        //StateModelImpl stateModel = new StateModelImpl(DEFAULT_AFTERDELTA, DEFAULT_TASK);
        StateModelJpaImpl stateModel = new StateModelJpaImpl(entityManager, DEFAULT_AFTERDELTA, DEFAULT_TASK);
        ScheduledTaskModelImpl scheduledTaskModel = new ScheduledTaskModelImpl(taskModel);

        ControllerImpl controller = new ControllerImpl(stateModel, scheduledTaskModel);
        controller.addModel(stateModel);
        controller.addModel(scheduledTaskModel);

        ViewImpl view = new ViewImpl(controller, taskModel);

        view.run();

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
