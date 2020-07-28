package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.ExecutableTask;
import tasks.TaskTemplate;
import utilities.reflection.ReflectionApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskModelImpl implements TaskModel {
    private static final Logger logger = LoggerFactory.getLogger(TaskModelImpl.class);

    private final Map<String, ExecutableTask> taskMap = new LinkedHashMap<>();

    public TaskModelImpl(@NotNull List<String> tasksPackageAndClassName) {
        for (String taskPackageAndClassName : tasksPackageAndClassName) {
            try {
                ExecutableTask taskInstance = getTaskFromPackageAndClassName(taskPackageAndClassName);
                taskMap.put(taskInstance.getName(), taskInstance);
            } catch (TaskNotFoundException e) {
                logger.error(e.toString());
            }
        }
    }

    private @NotNull ExecutableTask getTaskFromPackageAndClassName(@NotNull String packageAndClassName) throws TaskNotFoundException {
        ReflectionApi reflectionApi = new ReflectionApi(false, false);
        Object instance = reflectionApi.instantiateFromStringPackageNameClassName(packageAndClassName);
        if (instance instanceof ExecutableTask) {
            return (ExecutableTask) instance;
        } else {
            throw new TaskNotFoundException(packageAndClassName + " is not ExecutableTask class.");
        }
    }

    public boolean contains(@NotNull String name) {
        return taskMap.containsKey(name);
    }

    public @NotNull ExecutableTask getTaskByName(@NotNull String name) throws TaskNotFoundException {
        ExecutableTask task = taskMap.get(name);
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
