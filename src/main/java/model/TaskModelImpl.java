package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Object instance = ClassLoader.fromPackageNameClassName(packageAndClassName);
        if(instance instanceof Task) {
            return (Task) instance;
        } else {
            throw new ClassLoadingException(packageAndClassName + " is not Task class.");
        }
    }

    public List<TaskTemplate> getTaskTemplates() {
        return taskMap.values().stream().collect(Collectors.toList());
    }
}
