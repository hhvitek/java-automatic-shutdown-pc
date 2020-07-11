package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(ClassLoader.class);

    /**
     * Parameter must be in the format package.className convention
     * @throws ClassLoadingException
     */
    public static Object fromPackageNameClassName(@NotNull String packageNameClassName) throws ClassLoadingException {
        try {
            Class<?> clazz = Class.forName(packageNameClassName);
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            return instance;
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                ClassNotFoundException |
                InstantiationException e) {
            logger.warn("Class \"{}\" not found. Forgot package name \"package.className\"? What about static method getInstance()?", packageNameClassName);
            throw new ClassLoadingException(e);
        }
    }
}
