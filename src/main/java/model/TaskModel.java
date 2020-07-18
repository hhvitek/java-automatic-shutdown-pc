package model;

import org.jetbrains.annotations.NotNull;
import tasks.Task;

import java.util.List;

public interface TaskModel {

    boolean contains(@NotNull String name);

    /**
     * Return null if no task exists.
     */
    @NotNull Task getTaskByName(@NotNull String name) throws TaskNotFoundException;

    @NotNull List<TaskTemplate> getTaskTemplates();
}
