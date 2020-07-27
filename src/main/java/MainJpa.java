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
import view.SwingViewUtils;
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

        SwingViewUtils.setLookAndFeelToSystemDefault();
        SwingViewUtils.setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        TaskModel taskModel = new TaskModelImpl(ACTIVE_TASKS);

        model.db.operations.EntityManagerFactory entityManagerFactory = new SqliteEntityManagerFactory(ENTITY_MANAGER_FACTORY);
        EntityManager appEntityManager = entityManagerFactory.createEntityManager();
        EntityManager periodicModelTimerEntityManager = entityManagerFactory.createEntityManager();

        StateModel stateModel = new StateModelJpaImpl(appEntityManager, DEFAULT_AFTERDELTA, DEFAULT_TASK);

        Manager userManager = new ManagerJpaImpl(taskModel, appEntityManager);
        Manager periodicManager = new ManagerJpaImpl(taskModel, periodicModelTimerEntityManager);

        Main.executeCommom(taskModel, stateModel, userManager, periodicManager);

        logger.info("FINISHED");
    }


}
