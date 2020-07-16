package model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTaskManager {

    private List<ScheduledTask> scheduledTasks;

    public ScheduledTaskManager() {
        scheduledTasks = new ArrayList<>();
    }

    public void addScheduledTask(@NotNull ScheduledTask newScheduledTask) {
        scheduledTasks.add(newScheduledTask);
    }
}
