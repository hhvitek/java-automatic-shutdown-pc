package controller;

import model.ScheduledTaskModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ControllerScheduledTasksImpl extends AbstractController{

    private static final Logger logger = LoggerFactory.getLogger(ControllerScheduledTasksImpl.class);

    private final ScheduledTaskModel scheduledTaskModel;

    public ControllerScheduledTasksImpl(@NotNull ScheduledTaskModel scheduledTaskModel) {
        this.scheduledTaskModel = scheduledTaskModel;
    }

    public void actionCancelScheduledTaskById(int id) {
        scheduledTaskModel.cancelScheduledTask(id);
    }

    public void actionCancelSelectedTasks(@NotNull List<Integer> selectedTasks) {
        selectedTasks.forEach(
                this::actionCancelScheduledTaskById
        );
    }

    public void actionRemoveAllFinishedTasks() {
        scheduledTaskModel.removeAllFinishedTasks();
    }

    public void actionRemoveAllTasks() {
        scheduledTaskModel.removeAllTasks();
    }




}
