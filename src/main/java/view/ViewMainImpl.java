package view;

import controller.ControllerMainImpl;
import model.ModelObservableEvents;
import model.StateModel;
import model.TaskModel;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskTemplate;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class ViewMainImpl extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(ViewMainImpl.class);

    private final TaskModel taskModel;
    private final ControllerMainImpl controller;
    private final MainWindowCreator windowCreator;


    public ViewMainImpl(@NotNull ControllerMainImpl controller, @NotNull TaskModel taskModel) {
        super(controller);

        this.controller = controller;
        this.taskModel = taskModel;

        List<TaskTemplate> tasks = taskModel.getTaskTemplates();
        windowCreator = new MainWindowCreator(tasks);
        createAllListeners(windowCreator);

        // must be after windowCreator is created because model is already generating events..
        controller.setView(this);
        controller.addView(this);
    }

    private void createAllListeners(@NotNull MainWindowCreator windowCreator) {
        windowCreator.spinnerChooseTiming.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String newTiming = windowCreator.getTimingValue();
                controller.changedTimingByUser(newTiming);
            }
        });
        windowCreator.buttonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTaskName = windowCreator.getSelectedTaskName();
                String timing = windowCreator.getTimingValue();
                String selectedTaskParameter = windowCreator.getSelectedTaskParameter();
                controller.actionNewTaskScheduledByUser(selectedTaskName, timing, selectedTaskParameter);
            }
        });
        windowCreator.buttonShowScheduledTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionShowScheduledTasks();
            }
        });
        windowCreator.buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionExitByUser();
            }
        });
        windowCreator.uiChooseTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTaskName = windowCreator.getSelectedTaskName();
                controller.actionNewTaskSelectedByUser(selectedTaskName);
            }
        });
        windowCreator.buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.actionCancelLastScheduledTask();
            }
        });
    }

    public void run() {
        //refreshTimingCountdowns(new TimeManager(WindowCreator.TIMING_DEFAULT_VALUE));
        windowCreator.run();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        //TODO exception illegal argument
        switch (ModelObservableEvents.valueOf(propertyName)) {
            case SELECTED_TIMING_DURATION_DELAY_CHANGED: {
                logger.debug("Chose timing has changed. {} -> {}", evt.getOldValue(), evt.getNewValue());
                String newTimingDurationDelay = evt.getNewValue().toString();
                TimeManager timeManager = new TimeManager(newTimingDurationDelay);
                refreshTimingCountdowns(timeManager);
                break;
            }
            case TIMER_TICK: {
                controller.eventTimerTickReceived();
                break;
            }
            case LAST_SCHEDULED_TASK_CHANGED: {
                int id = (int) evt.getNewValue();
                updateLastScheduledTask(id);
                break;
            }
        }
    }

    private void refreshTimingCountdowns(@NotNull TimeManager durationDelay) {
        windowCreator.labelDurationDelay.setText(durationDelay.getRemainingDurationInHHMMSS_ifElapsedZeros());
        windowCreator.labelWhenElapsed.setText(durationDelay.getWhenElapsedInHHMM());
    }

    public void refreshLastScheduledTaskTimingCountdowns(@NotNull TimeManager durationDelay) {
        windowCreator.labelLastDurationDelay.setText(durationDelay.getRemainingDurationInHHMMSS_ifElapsedZeros());
        windowCreator.labelLastWhenElapsed.setText(durationDelay.getWhenElapsedInHHMM());
    }

    public void refreshViewFromModel(@NotNull StateModel stateModel) {
        windowCreator.setSelectedTaskName(stateModel.getSelectedTaskName());
        windowCreator.spinnerChooseTiming.getModel().setValue(stateModel.getTimingDurationDelay());
        refreshTimingCountdowns(new TimeManager(stateModel.getTimingDurationDelay()));
        updateLastScheduledTask(stateModel.getLastScheduledTaskId());
    }

    private void updateLastScheduledTask(int id) {
        windowCreator.labelLastId.setText(String.valueOf(id));
    }

    public void showInfoMessageToUser(@NotNull String message) {
        windowCreator.labelStatusbar.setText(message);
    }

    public void showErrorMessageToUser(@NotNull String errorMessage) {
        showInfoMessageToUser(errorMessage);
        windowCreator.showErrorPopup(errorMessage);
    }
}
