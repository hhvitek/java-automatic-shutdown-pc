package model;

import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import tasks.TaskTemplate;

import java.util.Optional;

public class ScheduledTaskMessenger {
    public Integer id;
    public TaskTemplate taskTemplate;
    public String taskParameter;
    public TimeManager whenElapsed;
    public String errorMessage;
    public String output;
    public ScheduledTaskStatus status;

    private ScheduledTaskMessenger() {
    }

    public static ScheduledTaskMessenger createFromScheduledTask(@NotNull ScheduledTask scheduledTask) {
        ScheduledTaskMessenger messenger = new ScheduledTaskMessenger();

        messenger.id = scheduledTask.getId();
        messenger.taskParameter = scheduledTask.getTaskParameter().orElse("");
        messenger.taskTemplate = scheduledTask.getTaskTemplate();
        messenger.whenElapsed = scheduledTask.getWhenElapsed();
        messenger.errorMessage = scheduledTask.getErrorMessage();
        messenger.output = scheduledTask.getOutput();
        messenger.status = scheduledTask.getStatus();

        return messenger;
    }

    public Integer getId() {
        return id;
    }

    public TaskTemplate getTaskTemplate() {
        return taskTemplate;
    }

    public String getTaskParameter() {
        return taskParameter;
    }

    public TimeManager getWhenElapsed() {
        return whenElapsed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    public ScheduledTaskStatus getStatus() {
        return status;
    }
}
