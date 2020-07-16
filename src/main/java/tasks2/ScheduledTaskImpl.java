package tasks2;

import model.ScheduledTask;
import model.ScheduledTaskStatus;
import model.TaskTemplate;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import static model.ScheduledTaskStatus.*;

public class ScheduledTaskImpl implements ScheduledTask {

    private final Task task;
    private final TimeManager whenElapsed;

    private ScheduledTaskStatus status;

    public ScheduledTaskImpl(@NotNull Task task, @NotNull TimeManager whenElapsed) {
        this.task = task;
        this.whenElapsed = whenElapsed;
        status = SCHEDULED;
    }

    @Override
    public TaskTemplate getTaskTemplate() {
        return task.getTaskTemplate();
    }

    @Override
    public String getTaskParameter() {
        return task.getParameter();
    }

    @Override
    public TimeManager getWhenElapsed() {
        return whenElapsed.clone();
    }

    @Override
    public String execute() {
        return task.execute();
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
