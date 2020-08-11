import model.StateModel;
import model.TaskModel;
import model.TaskModelImpl;
import model.db.operations.AbstractEntityManagerFactory;
import model.db.operations.ManagerJpaImpl;
import model.db.operations.SqliteEntityManagerFactoryImpl;
import model.db.operations.StateModelJpaImpl;
import model.scheduledtasks.Manager;
import view.SwingViewUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;

public final class MainJpa {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("my-sqlite");

    private static final AbstractEntityManagerFactory entityManagerFactory = new SqliteEntityManagerFactoryImpl(ENTITY_MANAGER_FACTORY);

    private static final EntityManager appEntityManager = entityManagerFactory.createEntityManager();

    private static final EntityManager periodicModelTimerEntityManager = entityManagerFactory.createEntityManager();

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        Main.logger.info("STARTING JPA");

        SwingViewUtils.setLookAndFeelToSystemDefault();
        SwingViewUtils.setDefaultFont();

        TaskModel taskModel = new TaskModelImpl(Main.ACTIVE_TASKS);

        StateModel stateModel = new StateModelJpaImpl(appEntityManager, Main.DEFAULT_AFTERDELTA, Main.DEFAULT_TASK);

        Manager userManager = new ManagerJpaImpl(taskModel, appEntityManager);
        Manager periodicManager = new ManagerJpaImpl(taskModel, periodicModelTimerEntityManager);

        Main.executeCommonAfter(taskModel, stateModel, userManager, periodicManager);

        addShutdownHooks();
        Main.logger.info("FINISHED initialization");
    }

    public static void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run()
            {
                Main.logger.info("EXITING application");
                periodicModelTimerEntityManager.close();
                appEntityManager.close();
                ENTITY_MANAGER_FACTORY.close();
            }
        });
    }


}
