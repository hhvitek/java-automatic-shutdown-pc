package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Task;
import utilities.reflection.ClassLoadingException;
import utilities.reflection.ReflectionApi;

import java.util.*;

public class TaskModelImpl implements TaskModel {
    private static final Logger logger = LoggerFactory.getLogger(TaskModelImpl.class);

    private final Map<String, Task> taskMap = new LinkedHashMap<>();

    public TaskModelImpl(@NotNull List<String> tasksPackageAndClassName) {
        for(String taskPackageAndClassName: tasksPackageAndClassName) {
            try {
                Task taskInstance = getTaskFromPackageAndClassName(taskPackageAndClassName);
                taskMap.put(taskInstance.getName(), taskInstance);
            } catch (ClassLoadingException e) {
                logger.error(e.toString());
            }
        }
    }

    private Task getTaskFromPackageAndClassName(@NotNull String packageAndClassName) throws ClassLoadingException {
        Object instance = ReflectionApi.instantiateFromStringPackageNameClassName(packageAndClassName);
        if(instance instanceof Task) {
            return (Task) instance;
        } else {
            throw new ClassLoadingException(packageAndClassName + " is not Task class.");
        }
    }

    public boolean contains(@NotNull String name) {
        return taskMap.containsKey(name);
    }

    /**
     * Return null if no task exists.
     */
    public @NotNull Task getTaskByName(@NotNull String name) throws TaskNotFoundException{
        Task task = taskMap.get(name);
        if (task != null) {
            return task;
        } else {
            throw new TaskNotFoundException(name);
        }
    }

    public @NotNull List<TaskTemplate> getTaskTemplates() {
        return new ArrayList<>(taskMap.values());
    }
}
