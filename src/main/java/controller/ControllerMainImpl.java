package controller;

import model.*;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewMainImpl;
import view.scheduledtasks.ViewScheduledTasksImpl;

/**
 * Controller for user actions in the Main app window. Specifically to schedule any new ScheduledTask.
 *
 * Upon user-request shows the a new window containing list of all ScheduledTasks.
 */
public class ControllerMainImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMainImpl.class);

    private final StateModel stateModel;
    private final ScheduledTaskModelImpl scheduledTaskModel;

    private ViewMainImpl view;
    private ViewScheduledTasksImpl taskView;
    private ControllerScheduledTasksImpl controllerScheduledTasks;

    public ControllerMainImpl(@NotNull StateModel stateModel, @NotNull ScheduledTaskModelImpl scheduledTaskModel, @NotNull ControllerScheduledTasksImpl controllerScheduledTasks) {
        this.stateModel = stateModel;
        this.scheduledTaskModel = scheduledTaskModel;

        this.controllerScheduledTasks = controllerScheduledTasks;

        addModel(stateModel);
        addModel(scheduledTaskModel);
    }

    public void setViews(@NotNull ViewMainImpl mainView, @NotNull ViewScheduledTasksImpl taskView) {
        this.view = mainView;
        this.taskView = taskView;
    }

    public void actionExitByUser() {
        logger.warn("Action EXIT requested by user");
    }

    public void actionNewTaskScheduledByUser(@NotNull String taskName, @NotNull String strTimingDelay, @Nullable String taskParameter) {
        try {
            int id = scheduledTaskModel.scheduleTask(taskName, taskParameter, strTimingDelay);
            stateModel.setLastScheduledTaskId(id);
            view.showInfoMessageToUser("Scheduled task created: " + id);
        } catch (TaskNotFoundException t) {
            view.showErrorMessageToUser(t.toString());
        }
    }

    public void actionCancelLastScheduledTask() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        controllerScheduledTasks.actionCancelScheduledTaskById(lastScheduledTaskId);
    }

    public void eventTimerTickReceived() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        try {
            ScheduledTaskMessenger lastScheduledTask = scheduledTaskModel.getScheduledTask(lastScheduledTaskId);
            refreshViewIfTaskIsInScheduledStatus(lastScheduledTask);
        } catch (ScheduledTaskNotFoundException ex) {
            // task has simply been deleted and user hasn't created a new one.
            // or no task has ever been scheduled so far...
            // or database has changed since last run (scheduled task deleted but lastScheduledTaskId stayed)...
            String errorMessage = "Last eventId <{" + lastScheduledTaskId + "}> in database is old and incorrect one.";
            logger.debug(errorMessage);
            view.showInfoMessageToUser(errorMessage);
        }
    }

    public void actionShowScheduledTasks() {
        taskView.runAndShowScheduledTasksOverviewWindow();
    }

    private void refreshViewIfTaskIsInScheduledStatus(@NotNull ScheduledTaskMessenger lastScheduledTask) {
        if (lastScheduledTask.getStatus() == ScheduledTaskStatus.SCHEDULED) {
            view.refreshLastScheduledTaskTimingCountdowns(lastScheduledTask.getWhenElapsed());
        }
    }

    public void actionNewTaskSelectedByUser(@NotNull String newTaskName) {
        modelInvokeMethodWithParameters("setSelectedTaskName", newTaskName);
    }

    public void changedTimingByUser(@NotNull String newValue) {
        modelInvokeMethodWithParameters("setTimingDurationDelay", newValue);
    }

    public void run() {
        view.refreshViewFromModel(stateModel);
        view.run();
    }
}
