package view;

import controller.ControllerImpl;
import model.TaskModel;
import model.TaskTemplate;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WindowManager {

    private final TaskModel taskModel;
    private final ControllerImpl controller;
    private final WindowCreator windowCreator;

    public WindowManager(@NotNull TaskModel taskModel, @NotNull ControllerImpl controller) {
        this.taskModel = taskModel;
        this.controller = controller;

        List<TaskTemplate> tasks = taskModel.getTaskTemplates();
        windowCreator = createWindow(tasks);
        createAllListeners(windowCreator);
    }

    private WindowCreator createWindow(@NotNull List<TaskTemplate> tasks) {
        return new WindowCreator(tasks);
    }

    private void createAllListeners(@NotNull WindowCreator windowCreator) {
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
                controller.actionCancelScheduledTaskByUser();
            }
        });
    }

    void run() {
        refreshTimingCountdowns(new TimeManager(WindowCreator.TIMING_DEFAULT_VALUE));
        windowCreator.run();
    }

    void timerTick() {
        controller.eventTimerTickReceived();
    }

    void refreshTimingCountdowns(@NotNull TimeManager durationDelay) {
        windowCreator.labelDurationDelay.setText(durationDelay.getRemainingDurationInHHMMSS_ifElapsedZeros());
        windowCreator.labelWhenElapsed.setText(durationDelay.getWhenElapsedInHHMM());
    }

    void refreshLastScheduledTaskTimingCountdowns(@NotNull TimeManager durationDelay) {
        windowCreator.labelLastDurationDelay.setText(durationDelay.getRemainingDurationInHHMMSS_ifElapsedZeros());
        windowCreator.labelLastWhenElapsed.setText(durationDelay.getWhenElapsedInHHMM());
    }

    void updateLastScheduledTask(int id) {
        windowCreator.labelLastId.setText(String.valueOf(id));
    }

    void showInfoMessageToUser(@NotNull String message) {
        windowCreator.labelStatusbar.setText(message);
    }

    void showErrorMessageToUser(@NotNull String errorMessage) {
        showInfoMessageToUser(errorMessage);
        windowCreator.showErrorPopup(errorMessage);
    }
}
