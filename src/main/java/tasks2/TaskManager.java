package tasks2;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private final Map<String, TaskTemplateImpl> taskTemplates;

    public TaskManager(@NotNull List<String> packageNameClassNames) {
        taskTemplates = new LinkedHashMap<>();

        initializeAllTasks(packageNameClassNames);
    }

    private void initializeAllTasks(@NotNull List<String> packageNameClassNames) {
        for(String packageNameClassName: packageNameClassNames) {
            try {
                TaskTemplateImpl taskTemplate = TaskClassLoader.loadTaskTemplateByTaskClassName(packageNameClassName);
                taskTemplates.put(taskTemplate.getName(), taskTemplate);
            } catch (ClassLoadingException e) {
                logger.error("Failed to recognize requested task: <{}>", packageNameClassName);
            }
        }
    }

    private Task loadTaskByTaskName(@NotNull String taskName) throws ClassLoadingException {
        if (isTaskSupported(taskName)) {
            TaskTemplateImpl taskTemplate = taskTemplates.get(taskName);
            Task task = TaskClassLoader.loadTaskByTaskClass(taskTemplate.getClazz());
            return task;
        } else {
            throw new ClassLoadingException("Task <" + taskName +"> is not supported.");
        }
    }

    private ParametrizedTask loadParametrizedTaskByTaskName(@NotNull String taskName, @NotNull String parameter) throws ClassLoadingException {
        if (isTaskSupported(taskName)) {
            TaskTemplateImpl taskTemplate = taskTemplates.get(taskName);
            ParametrizedTask task = TaskClassLoader.loadParametrizedTaskByTaskClass(taskTemplate.getClazz(), parameter);
            return task;
        } else {
            throw new ClassLoadingException("Parametrized Task <" + taskName +"> is not supported.");
        }
    }

    public boolean isTaskSupported(@NotNull String taskName) {
        return taskTemplates.containsKey(taskName);
    }

    public boolean acceptsTaskParameter(@NotNull String taskName) {
        if (isTaskSupported(taskName)) {
            TaskTemplateImpl taskTemplate = taskTemplates.get(taskName);
            return taskTemplate.acceptParameter();
        }
        return false;
    }

    public Task createTask(@NotNull String taskName) throws ClassLoadingException{
        return loadTaskByTaskName(taskName);
    }

    public ParametrizedTask createParametrizedTask(@NotNull String taskName, @NotNull String parameter) throws ClassLoadingException {
        return loadParametrizedTaskByTaskName(taskName, parameter);
    }
}
