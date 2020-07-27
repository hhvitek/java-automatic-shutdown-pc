package view.scheduledtasks;

import controller.ControllerScheduledTasksImpl;
import model.ModelObservableEvents;
import model.ScheduledTaskMessenger;
import model.scheduledtasks.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.AbstractView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Scheduled action Window
 * both View and Controller for this frame
 * Uses JTable
 */
public class ViewScheduledTasksImpl extends AbstractView {
    private JPanel panelGui;
    private JPanel panelControls;
    private JTable tableScheduledTasks;
    private JButton buttonRemoveFinished;
    private JButton buttonRemoveAll;
    private JButton buttonCancelSelected;
    private JScrollPane panelScrollTable;

    private static final Logger logger = LoggerFactory.getLogger(ViewScheduledTasksImpl.class);

    private final JFrame guiFrame;

    private final TableViewController scheduledActions;

    public ViewScheduledTasksImpl(@NotNull ControllerScheduledTasksImpl controller,
                                  @NotNull List<ScheduledTaskMessenger> initialAlreadyExistingTasks) {
        super(controller);

        guiFrame = new JFrame("Načasované akce");
        guiFrame.setContentPane(panelGui);
        guiFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        scheduledActions = new TableViewController(tableScheduledTasks, initialAlreadyExistingTasks);

        buttonRemoveFinished.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionRemoveAllFinishedTasks();
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

        //TODO will receive events from the main controller
        //controller.addView(this);
    }

    public void runAndShowScheduledTasksOverviewWindow() {
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
                ScheduledTaskMessenger newTask = (ScheduledTaskMessenger) evt.getNewValue();
                scheduledActions.createUIForOneScheduledTask(newTask);
                break;
            }
            case SCHEDULED_TASK_STATUS_CHANGED:
            case SCHEDULED_TASK_FINISHED:
            case SCHEDULED_TASK_FINISHED_WITH_ERRORS: {
                ScheduledTaskMessenger updatedTask = (ScheduledTaskMessenger) evt.getNewValue();
                scheduledActions.updateUIForOneScheduledTask(updatedTask);
                break;
            }
            case TIMER_TICK: {
                List<ScheduledTaskMessenger> scheduledTasks = (List<ScheduledTaskMessenger>) evt.getNewValue();
                scheduledActions.updateUIWhenElapsedForTasks(scheduledTasks);
                break;
            }
        }
    }
}
