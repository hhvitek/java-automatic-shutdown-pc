package model;

import org.jetbrains.annotations.NotNull;
import tasks.ExecutableTask;
import tasks.TaskTemplate;

import java.util.List;

/**
 * Represents what tasks can the model actually perform?
 */
public interface TaskModel {

    boolean contains(@NotNull String name);

    /**
     * @throws TaskNotFoundException
     */
    @NotNull ExecutableTask getTaskByName(@NotNull String name) throws TaskNotFoundException;

    /**
     * Returns just description of executable tasks
     */
    @NotNull List<TaskTemplate> getTaskTemplates();
}
