package tasks2;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TaskClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(TaskClassLoader.class);

    public static TaskTemplateImpl loadTaskTemplateByTaskClassName(@NotNull String packageNameClassName) throws tasks2.ClassLoadingException {
        try {
            Class<?> clazz = Class.forName(packageNameClassName);
            Method method = clazz.getMethod("getTaskTemplateStatic");
            TaskTemplateImpl taskTemplate = (TaskTemplateImpl) method.invoke(null);
            return taskTemplate;
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            logger.error("Requested class <{}> does not have relevant accessible <{}> method", packageNameClassName, "getTaskTemplateStatic");
            throw  new tasks2.ClassLoadingException(e);
        } catch (ClassNotFoundException e) {
            logger.error("Requested class <{}> not found", packageNameClassName);
            throw  new ClassLoadingException(e);
        }
    }

    public static Task loadTaskByTaskClass(@NotNull Class<?> clazz) throws ClassLoadingException {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            Task task = (Task) constructor.newInstance();
            return task;
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException|
                InstantiationException e) {
            logger.error("Requested class <{}> does not have relevant accessible no param constructor", clazz);
            throw  new ClassLoadingException(e);
        }
    }

    public static ParametrizedTask loadParametrizedTaskByTaskClass(@NotNull Class<?> clazz, @NotNull String parameter) throws ClassLoadingException {
        try {
            Constructor<?> constructor = clazz.getConstructor(String.class);
            ParametrizedTask task = (ParametrizedTask) constructor.newInstance(parameter);
            return task;
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException|
                InstantiationException e) {
            logger.error("Requested class <{}> does not have relevant accessible single param <{}> constructor", clazz, parameter);
            throw  new ClassLoadingException(e);
        }
    }
}
