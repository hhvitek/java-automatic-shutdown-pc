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
     * @param name Task's name
     * @return Found Task
     *
     * @throws TaskNotFoundException if the given name does not represent any existing task.
     */
    @NotNull ExecutableTask getTaskByName(@NotNull String name) throws TaskNotFoundException;

    @NotNull List<TaskTemplate> getTaskTemplates();
}
