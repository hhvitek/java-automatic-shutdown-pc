package controller;

import model.ScheduledTaskModel;
import model.ScheduledTaskNotFoundException;
import model.StateModel;
import model.TaskNotFoundException;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewMainImpl;

public class ControllerMainImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMainImpl.class);

    private final StateModel stateModel;
    private final ScheduledTaskModel scheduledTaskModel;

    private ViewMainImpl view;
    private ControllerScheduledTasksImpl controllerScheduledTasks;

    public ControllerMainImpl(@NotNull StateModel stateModel, @NotNull ScheduledTaskModel scheduledTaskModel) {
        this.stateModel = stateModel;
        this.scheduledTaskModel = scheduledTaskModel;

        controllerScheduledTasks = new ControllerScheduledTasksImpl(scheduledTaskModel);
    }

    public void setView(@NotNull ViewMainImpl view) {
        this.view = view;
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
            ScheduledTask lastScheduledTask = scheduledTaskModel.getScheduledTask(lastScheduledTaskId);
            refreshViewIfTaskIsInScheduledStatus(lastScheduledTask);
        } catch (ScheduledTaskNotFoundException ex) {
            // task has simply been deleted and user hasn't created a new one.
            // or no task has ever been scheduled so far...
        }
    }

    public void actionShowScheduledTasks() {
        viewInvokeMethod("runAndShowScheduledTasksOverviewWindow");
    }

    private void refreshViewIfTaskIsInScheduledStatus(@NotNull ScheduledTask lastScheduledTask) {
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
