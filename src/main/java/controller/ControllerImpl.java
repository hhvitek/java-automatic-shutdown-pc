package controller;

import model.StateModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewImpl;

public class ControllerImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerImpl.class);

    private final StateModel stateModel;
    private ViewImpl view;

    public ControllerImpl(@NotNull StateModel stateModel) {
        this.stateModel = stateModel;
    }

    public void setView(@NotNull ViewImpl view) {
        this.view = view;
    }

    public void actionExitByUser() {
        logger.debug("Action EXIT requested by user");
    }

    public void actionNewTaskScheduledByUser(@NotNull String taskName, @NotNull String strTimingDelay, @Nullable String taskParameter) {
        stateModel.setScheduledTask(taskName, taskParameter, strTimingDelay);
    }

    public void actionCancelScheduledTaskByUser() {
        //stateModel.cancelScheduledTask(); is the same as the following reflection API call:
        invokeModelMethodWithoutParameters("cancelScheduledTask");
    }

    public void actionNewTaskSelectedByUser(@NotNull String newTaskName) {
        setModelProperty("SelectedTaskName", newTaskName);
    }

    public void changedTimingByUser(@NotNull String newValue) {
        setModelProperty("TimingDurationDelay", newValue);
    }
}
