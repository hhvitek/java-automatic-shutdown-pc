package tasks;

import model.ScheduledTask;
import model.ScheduledTaskStatus;
import model.TaskTemplate;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import static model.ScheduledTaskStatus.*;

public class ScheduledTaskImpl implements ScheduledTask {

    private final Task task;
    private final TimeManager whenElapsed;

    private String parameter = null;

    private ScheduledTaskStatus status;

    public ScheduledTaskImpl(@NotNull Task task, @NotNull TimeManager whenElapsed) {
        this.task = task;
        this.whenElapsed = whenElapsed;
        status = SCHEDULED;
    }

    public ScheduledTaskImpl(@NotNull Task task, @NotNull TimeManager whenElapsed, @NotNull String parameter) {
        this(task, whenElapsed);
        this.parameter = parameter;
    }

    @Override
    public TaskTemplate getTaskTemplate() {
        return task;
    }

    @Override
    public String getTaskParameter() {
        return parameter;
    }

    @Override
    public TimeManager getWhenElapsed() {
        return new TimeManager(whenElapsed);
    }

    @Override
    public String execute() {
        if (task.acceptParameter()) {
            return task.execute(parameter);
        } else {
            return task.execute();
        }
    }

    @Override
    public void setStatus(@NotNull ScheduledTaskStatus newStatus) {
        if (status.isLessThan(newStatus)) {
            status = newStatus;
        }
    }

    @Override
    public ScheduledTaskStatus getStatus() {
        if (status == SCHEDULED && whenElapsed.hasElapsed()) {
            setStatus(ELAPSED);
        }
        return status;
    }
}
