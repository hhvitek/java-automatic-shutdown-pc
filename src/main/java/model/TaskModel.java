package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Task;
import tasks.TaskException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskModel {
    private static final Logger logger = LoggerFactory.getLogger(TaskModel.class);

    private final Map<String, Task> taskMap = new LinkedHashMap<>();

    public TaskModel(@NotNull List<String> tasksPackageAndClassName) {
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
        Object instance = ClassLoader.fromPackageNameClassName(packageAndClassName);
        if(instance instanceof Task) {
            return (Task) instance;
        } else {
            throw new ClassLoadingException(packageAndClassName + " is not Task class.");
        }
    }

    public List<Task> getTasks() {
        return taskMap.values().stream().collect(Collectors.toList());
    }

    public List<String> getTaskNames() {
        return taskMap.keySet().stream().collect(Collectors.toList());
    }

    public String executeTask(@NotNull String taskName) throws TaskException {
        if (taskMap.containsKey(taskName)) {
            Task task = taskMap.get(taskName);
            return task.execute();
        } else {
            String errorMessage = "Task " + taskName + " does not exist.";
            logger.error(errorMessage);
            throw new TaskException(errorMessage);
        }
    }
}
