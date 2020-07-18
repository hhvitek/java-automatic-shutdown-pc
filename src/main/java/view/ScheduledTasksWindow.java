package view;

import controller.ControllerImpl;
import model.ModelObservableEvents;
import model.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class ScheduledTasksWindow extends AbstractView {
    private JPanel panelGui;
    private JPanel panelControls;
    private JTable tableScheduledTasks;
    private JButton buttonRemoveFinished;
    private JButton buttonRemoveAll;
    private JButton buttonCancelSelected;
    private JScrollPane panelScrollTable;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasksWindow.class);

    private final JFrame guiFrame;

    private final WindowScheduledActions scheduledActions;

    public ScheduledTasksWindow(@NotNull ControllerImpl controller, @NotNull List<ScheduledTask> initialScheduledTasks) {
        super(controller);

        guiFrame = new JFrame("Načasované akce");
        guiFrame.setContentPane(panelGui);
        guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        scheduledActions = new WindowScheduledActions(tableScheduledTasks);

        initializeWindowWithData(initialScheduledTasks);
        buttonRemoveFinished.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionRemoveFinishedTasks();
            }
        });
        buttonRemoveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionRemoveAllTasks();
            }
        });
        buttonCancelSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Integer> selectedTasks = scheduledActions.getSelectedTaskIds();
                controller.actionCancelSelectedTasks(selectedTasks);
            }
        });

        controller.addView(this);
    }

    public void initializeWindowWithData(@NotNull List<ScheduledTask> initialScheduledTask) {
        initialScheduledTask.forEach(
                scheduledTask -> scheduledActions.createUIForOneScheduledTask(scheduledTask)
        );
    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiFrame.pack();
                setCenteredToGoldenRatio(guiFrame);
                guiFrame.setVisible(true);
            }
        });
    }

    /**
     * Callable after swing frame.pack() function to center application window.
     */
    private static void setCenteredToGoldenRatio(JFrame frame) {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = (int) screenDimension.getHeight();
        int screenWidth = (int) screenDimension.getWidth();

        int frameHeight = frame.getHeight();
        int frameWidth = frame.getWidth();

        int goldenRatioHeight = (int) ((screenHeight - frameHeight) * 0.38);

        frame.setLocation((screenWidth / 2) - (frameWidth / 2), goldenRatioHeight);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        //TODO exception illegal argument
        switch (ModelObservableEvents.valueOf(propertyName)) {
            case SCHEDULED_TASK_CREATED: {
                ScheduledTask newTask = (ScheduledTask) evt.getNewValue();
                scheduledActions.createUIForOneScheduledTask(newTask);
                break;
            }
            case SCHEDULED_TASK_STATUS_CHANGED:
            case SCHEDULED_TASK_FINISHED:
            case SCHEDULED_TASK_FINISHED_WITH_ERRORS: {
                ScheduledTask updatedTask = (ScheduledTask) evt.getNewValue();
                scheduledActions.updateUIForOneScheduledTask(updatedTask);
                break;
            }
            case TIMER_TICK: {
                List<ScheduledTask> scheduledTasks = (List<ScheduledTask>) evt.getNewValue();
                scheduledActions.updateUIWhenElapsedForTasks(scheduledTasks);
                break;
            }
        }
    }
}
