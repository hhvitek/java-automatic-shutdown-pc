import model.StateModel;
import model.StateModelImpl;
import model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.Gui;
import view.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {

    public static final List<String>  ACTIVE_TASKS = List.of("tasks.RestartTask", "tasks.ShutdownTask");
    public static final String DEFAULT_TASK = "Shutdown";
    public static final String DEFAULT_AFTERDELTA = "00:30";
    public static final String LOG_FILENAME = "shutdown.log";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        logger.info("STARTING");

        TaskModel taskModel = new TaskModel(ACTIVE_TASKS);
        StateModel stateModel = new StateModelImpl(DEFAULT_AFTERDELTA, DEFAULT_TASK);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        Gui gui = new Gui(taskModel, stateModel);
        gui.run();

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
