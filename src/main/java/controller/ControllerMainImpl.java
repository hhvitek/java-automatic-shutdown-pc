package controller;

import model.*;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.main.MainWindow;
import view.tasks.TasksWindow;

/**
 * Controller for user actions in the Main app window. Specifically to schedule any new ScheduledTask.
 * <p>
 * Upon user-request shows the a new window containing list of all ScheduledTasks.
 */
public class ControllerMainImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerMainImpl.class);

    private final StateModel stateModel;
    private final ScheduledTaskModelImpl scheduledTaskModel;

    private MainWindow mainView;
    private TasksWindow taskView;
    private final ControllerScheduledTasksImpl controllerScheduledTasks;

    public ControllerMainImpl(@NotNull StateModel stateModel, @NotNull ScheduledTaskModelImpl scheduledTaskModel, @NotNull ControllerScheduledTasksImpl controllerScheduledTasks) {
        this.stateModel = stateModel;
        this.scheduledTaskModel = scheduledTaskModel;

        this.controllerScheduledTasks = controllerScheduledTasks;

        addModel(stateModel);
        addModel(scheduledTaskModel);
    }

    public void setViews(@NotNull MainWindow mainView, @NotNull TasksWindow taskView) {
        this.mainView = mainView;
        this.taskView = taskView;
    }

    public void actionExitByUser() {
        logger.warn("Action EXIT requested by user");
        scheduledTaskModel.stopTimerAndCleanResources();
        System.exit(0);
    }

    public void actionNewTaskScheduledByUser(@NotNull String taskName, @NotNull String strTimingDelay, @Nullable String taskParameter) {
        try {
            int id = scheduledTaskModel.scheduleTask(taskName, taskParameter, strTimingDelay);
            stateModel.setLastScheduledTaskId(id);
            mainView.showInfoMessageToUser("Task created: " + id);
        } catch (TaskNotFoundException t) {
            mainView.showErrorMessageToUser(t.toString());
        }
    }

    public void actionCancelLastScheduledTask() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        controllerScheduledTasks.actionCancelScheduledTaskById(lastScheduledTaskId);
        mainView.showInfoMessageToUser("Task id: <" + lastScheduledTaskId + "> cancelled");
    }

    public void actionTimerTick_RefreshMainView() {
        int lastScheduledTaskId = stateModel.getLastScheduledTaskId();
        try {
            ScheduledTaskMessenger lastScheduledTask = scheduledTaskModel.getScheduledTaskByIdThrowOnError(lastScheduledTaskId);
            refreshMainViewIfTaskIsInScheduledStatus(lastScheduledTask);
        } catch (ScheduledTaskNotFoundException ex) {
            // task has simply been deleted and user hasn't created a new one.
            // or no task has ever been scheduled so far...
            // or database has changed since last run (scheduled task deleted but lastScheduledTaskId stayed)...
            String errorMessage = "Last eventId <{" + lastScheduledTaskId + "}> in database is old and incorrect one.";
            logger.debug(errorMessage);
            mainView.showInfoMessageToUser(errorMessage);
        }
    }

    private void refreshMainViewIfTaskIsInScheduledStatus(@NotNull ScheduledTaskMessenger lastScheduledTask) {
        if (lastScheduledTask.getStatus() == ScheduledTaskStatus.SCHEDULED) {
            mainView.refreshLastScheduledTaskTimingCountdowns(lastScheduledTask.getWhenElapsed());
        }
    }

    public void actionShowScheduledTasks() {
        taskView.runAndShowScheduledTasksOverviewWindow();
    }

    public void actionNewTaskSelectedByUser(@NotNull String newTaskName, @Nullable String newTaskParameter) {
        modelInvokeMethodWithParameters("setSelectedTaskNameAndParameter", newTaskName, newTaskParameter);

        String infoMessage = "The new task selected: <" + newTaskName + ">.<" + newTaskParameter + ">";
        viewInvokeMethod("showInfoMessageToUser", infoMessage);
    }

    public void changedTimingByUser(@NotNull String newValue) {
        modelInvokeMethodWithParameters("setTimingDurationDelay", newValue);
    }

    public void run() {
        mainView.refreshViewFromModel(stateModel);
        mainView.run();
    }
}
