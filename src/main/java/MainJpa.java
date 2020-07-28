import model.StateModel;
import model.TaskModel;
import model.TaskModelImpl;
import model.db.operations.AbstractEntityManagerFactory;
import model.db.operations.ManagerJpaImpl;
import model.db.operations.SqliteEntityManagerFactoryImpl;
import model.db.operations.StateModelJpaImpl;
import model.scheduledtasks.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.SwingViewUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainJpa {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        Main.logger.info("STARTING JPA");

        SwingViewUtils.setLookAndFeelToSystemDefault();
        SwingViewUtils.setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        TaskModel taskModel = new TaskModelImpl(Main.ACTIVE_TASKS);

        AbstractEntityManagerFactory entityManagerFactory = new SqliteEntityManagerFactoryImpl(ENTITY_MANAGER_FACTORY);
        EntityManager appEntityManager = entityManagerFactory.createEntityManager();
        EntityManager periodicModelTimerEntityManager = entityManagerFactory.createEntityManager();

        StateModel stateModel = new StateModelJpaImpl(appEntityManager, Main.DEFAULT_AFTERDELTA, Main.DEFAULT_TASK);

        Manager userManager = new ManagerJpaImpl(taskModel, appEntityManager);
        Manager periodicManager = new ManagerJpaImpl(taskModel, periodicModelTimerEntityManager);

        Main.executeCommon(taskModel, stateModel, userManager, periodicManager);

        Main.logger.info("FINISHED");
    }


}
