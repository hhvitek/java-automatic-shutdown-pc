package controller;

import model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ScheduledTasksWindow;
import view.ViewImpl;

import java.util.List;

public class ControllerImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

    private final StateModel stateModel;
    private final ScheduledTaskModel scheduledTaskModel;

    private ScheduledTasksWindow scheduledTasksWindow;
    private ViewImpl view;

    public ControllerImpl(@NotNull StateModel stateModel, @NotNull ScheduledTaskModel scheduledTaskModel) {
        this.stateModel = stateModel;
        this.scheduledTaskModel = scheduledTaskModel;
    }

    public void setView(@NotNull ViewImpl view) {
        this.view = view;
    }

    public void actionExitByUser() {
        logger.debug("Action EXIT requested by user");
    }

    public void actionNewTaskScheduledByUser(@NotNull String taskName, @NotNull String strTimingDelay, @Nullable String taskParameter) {
        try {
            int id = scheduledTaskModel.scheduleTask(taskName, taskParameter, strTimingDelay);
            stateModel.setLastScheduledTaskId(id);
            view.showUserInfo("Scheduled task created: " + id);
        } catch (TaskNotFoundException t) {
            view.showUserError(t.toString());
        }
        //invokeModelMethodWitMultipleParameters("scheduleTask", new Object[]{taskName, strTimingDelay, taskParameter});
    }

    public void actionCancelScheduledTaskByUser() {
        //stateModel.cancelScheduledTask(); is the same as the following utilities.reflection API call:
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        scheduledTaskModel.cancelScheduledTask(lastScheduledTaskId);
    }

    public void actionRemoveScheduledTaskByUser() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        invokeModelMethodWithOneParameter("cancelScheduledTask", lastScheduledTaskId);
    }

    public void eventTimerTickReceived() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        try {
            ScheduledTask lastScheduledTask = scheduledTaskModel.getScheduledTask(lastScheduledTaskId);
            refreshViewIfTaskIsInScheduledStatus(lastScheduledTask);
        } catch (ScheduledTaskNotFoundException ex) {
            // task has simply been deleted and user hasn't created a new one.
            // or no task has ever been scheduled so far...
        }
    }

    private void refreshViewIfTaskIsInScheduledStatus(@NotNull ScheduledTask lastScheduledTask) {
        if (lastScheduledTask.getStatus() == ScheduledTaskStatus.SCHEDULED) {
            view.refreshLastScheduledTaskTimingDurationDelay(lastScheduledTask.getWhenElapsed());
        }
    }

    public void actionNewTaskSelectedByUser(@NotNull String newTaskName) {
        //setModelProperty("SelectedTaskName", newTaskName);
        invokeModelMethodWithOneParameter("setSelectedTaskName", newTaskName);
    }

    public void changedTimingByUser(@NotNull String newValue) {
        invokeModelMethodWithOneParameter("setTimingDurationDelay", newValue);
    }

    //##################################################################################################################

    public void actionShowScheduledTasks() {
        List<ScheduledTask> scheduledTasks = scheduledTaskModel.getAllScheduledTask();
        scheduledTasksWindow = new ScheduledTasksWindow(this, scheduledTasks);

        scheduledTasksWindow.run();
    }

    public void actionRemoveFinishedTasks() {
        scheduledTaskModel.removeAllFinishedTasks();
    }

    public void actionRemoveAllTasks() {
        scheduledTaskModel.removeAllTasks();
    }

    public void actionCancelSelectedTasks(@NotNull List<Integer> selectedTasks) {
        selectedTasks.forEach(
                scheduledTaskModel::cancelScheduledTask
        );
    }
}
