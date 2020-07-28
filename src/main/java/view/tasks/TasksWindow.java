package view.tasks;

import controller.ControllerScheduledTasksImpl;
import model.ModelObservableEvents;
import model.ScheduledTaskMessenger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.AbstractWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Scheduled action Window
 * both View and Controller for this frame
 * Uses JTable
 */
public class TasksWindow extends AbstractWindow {
    private JPanel panelGui;
    private JPanel panelControls;
    private JTable tableScheduledTasks;
    private JButton buttonRemoveFinished;
    private JButton buttonRemoveAll;
    private JButton buttonCancelSelected;
    private JScrollPane panelScrollTable;

    private static final Logger logger = LoggerFactory.getLogger(TasksWindow.class);

    private final CustomTableController tableController;

    public TasksWindow(@NotNull ControllerScheduledTasksImpl controller,
                       @NotNull List<ScheduledTaskMessenger> initialAlreadyExistingTasks) {
        super(controller,new JFrame("Načasované akce"));

        guiFrame.setContentPane(panelGui);
        guiFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        tableController = new CustomTableController(tableScheduledTasks, initialAlreadyExistingTasks);

        buttonRemoveFinished.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.actionDeleteAllFinishedScheduledTasks();
            }
        });
        buttonRemoveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.actionDeleteAllScheduledTasks();
            }
        });
        buttonCancelSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Integer> selectedTasks = tableController.getSelectedTaskIds();
                controller.actionCancelSelectedTasks(selectedTasks);
            }
        });
    }

    public void runAndShowScheduledTasksOverviewWindow() {
        run();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        //TODO exception illegal argument
        switch (ModelObservableEvents.valueOf(propertyName)) {
            case SCHEDULED_TASK_CREATED: {
                ScheduledTaskMessenger newTask = (ScheduledTaskMessenger) evt.getNewValue();
                tableController.createUIForOneScheduledTask(newTask);
                break;
            }
            case SCHEDULED_TASK_STATUS_CHANGED:
            case SCHEDULED_TASK_FINISHED:
            case SCHEDULED_TASK_FINISHED_WITH_ERRORS: {
                ScheduledTaskMessenger updatedTask = (ScheduledTaskMessenger) evt.getNewValue();
                tableController.updateUIForOneScheduledTask(updatedTask);
                break;
            }
            case TIMER_TICK: {
                List<ScheduledTaskMessenger> scheduledTasks = (List<ScheduledTaskMessenger>) evt.getNewValue();
                tableController.updateUIWhenElapsedForTasks(scheduledTasks);
                break;
            }
        }
    }
}
