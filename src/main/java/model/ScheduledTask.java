package model;

import org.jetbrains.annotations.NotNull;
import tasks.Task;

public class ScheduledTask {

    private final Task task;
    private String taskParameter;

    private final TimeManager whenElapsed;

    public ScheduledTask(@NotNull Task task, @NotNull TimeManager whenElapsed) {
        this.task = task;
        this.whenElapsed = whenElapsed;
    }

    public void setTaskParameter(@NotNull String newParameter) {
        taskParameter = newParameter;
    }

    public void execute() {
        task.execute();
    }
}
