package controller;

import model.ScheduledTaskModelImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for user actions in a window containing all scheduled tasks.
 */
public class ControllerScheduledTasksImpl extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScheduledTasksImpl.class);

    private final ScheduledTaskModelImpl scheduledTaskModel;

    public ControllerScheduledTasksImpl(@NotNull ScheduledTaskModelImpl scheduledTaskModel) {
        this.scheduledTaskModel = scheduledTaskModel;
        addModel(scheduledTaskModel);
    }

    public void actionCancelScheduledTaskById(int id) {
        scheduledTaskModel.cancelScheduledTask(id);
    }

    public void actionCancelSelectedTasks(@NotNull List<Integer> selectedTasks) {
        selectedTasks.forEach(
                this::actionCancelScheduledTaskById
        );
    }

    public void actionDeleteAllFinishedScheduledTasks() {
        scheduledTaskModel.deleteAllFinishedScheduledTasks();
    }

    public void actionDeleteAllScheduledTasks() {
        scheduledTaskModel.deleteAllScheduledTasks();
    }
}
