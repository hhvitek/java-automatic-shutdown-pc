package utilities.reflection;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Using Java Reflection API.
 * Utility class, only static methods.
 */
public final class ReflectionApi {

    private static Logger logger = LoggerFactory.getLogger(ReflectionApi.class);

    private static boolean shouldLog = true;

    private ReflectionApi() {
    }

    public static void shouldLog(boolean shouldLog) {
        ReflectionApi.shouldLog = shouldLog;
        if (shouldLog) {
            logger = LoggerFactory.getLogger(ReflectionApi.class);
        } else {
            logger = LoggerFactory.getLogger("NoLogger");
        }

    }

    //METHOD INVOKE#####################################################################################################

    public static void invokeModelMethodWithOneParameter(@NotNull Object instance, @NotNull String methodName, @NotNull Object methodParameter) {
        Class<?> clazz = instance.getClass();
        try {
            Method method = clazz.getMethod(methodName, methodParameter.getClass());
            method.invoke(instance, methodParameter);
        } catch (Exception ex) {
            //  Handle exception.
            logger.warn("Failed to invoke model's method <{}>.<{}>({}) using utilities.reflection API. Method not found.", clazz, methodName, methodParameter, ex);
        }
    }

    public static void invokeModelMethodWithoutParameters(@NotNull Object instance, @NotNull String methodName) {
        Class<?> clazz = instance.getClass();
        try {
            Method method = clazz.getMethod(methodName);
            method.invoke(instance);
        } catch (Exception ex) {
            //  Handle exception.
            logger.warn("Failed to invoke model's method <{}>.<{}> using utilities.reflection API. Method not found.", clazz, methodName, ex);
        }
    }

    public static void invokeModelMethodWitMultipleParameters(@NotNull Object instance, @NotNull String methodName, @NotNull Object[] methodParameters) {
        Class<?> clazz = instance.getClass();
        try {
            Method method = clazz.getMethod(methodName, convertObjectArrayIntoClassArray(methodParameters));
            method.invoke(instance, methodParameters);
        } catch (Exception ex) {
            //  Handle exception.
            logger.warn("Failed to invoke model's method <{}>.<{}>({}) using utilities.reflection API. Method not found.", clazz, methodName, methodParameters, ex);
        }
    }

    private static Class<?>[] convertObjectArrayIntoClassArray(@NotNull Object[] objects) {
        return Arrays.stream(objects)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }

    //CLASS LOADER##################################################################################################################

    /**
     * Parameter must be in the format package.className convention
     *
     * @throws ClassLoadingException
     */
    public static Object instantiateFromStringPackageNameClassName(@NotNull String packageNameClassName) throws ClassLoadingException {
        try {
            Class<?> clazz = Class.forName(packageNameClassName);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                ClassNotFoundException |
                InstantiationException e) {
            String errorMessage = "Class <" + packageNameClassName + "> not found. Forgot package name \"package.className\"?";
            logger.warn(errorMessage);
            throw new ClassLoadingException(errorMessage, e);
        }
    }

    /**
     * Parameter must be in the format package.className convention
     *
     * @throws ClassLoadingException
     */
    public static Object instantiateFromStringPackageNameClassName(@NotNull Class<?> clazzToReturn, @NotNull String packageNameClassName) throws ClassLoadingException {
        Object instance = instantiateFromStringPackageNameClassName(packageNameClassName);
        if (clazzToReturn.isInstance(instance)) {
            return instance;
        } else {
            String errorMessage = String.format(
                    "String packageNameClassName: <%s> is not class: <%s>.",
                    packageNameClassName,
                    clazzToReturn
            );
            logger.warn(errorMessage);
            throw new ClassLoadingException(errorMessage);
        }
    }

    public static Object instantiateByClazz(@NotNull Class<?> clazz) throws ClassLoadingException {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                InstantiationException e) {
            String errorMessage = "Requested class <" + clazz + "> does not have relevant accessible no param constructor.";
            logger.warn(errorMessage);
            throw new ClassLoadingException(errorMessage, e);
        }
    }

    public static Object instantiateByClazzWithOneParameter(@NotNull Class<?> clazz, @NotNull Object parameter) throws ClassLoadingException {
        try {
            Constructor<?> constructor = clazz.getConstructor(parameter.getClass());
            return constructor.newInstance(parameter);
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                InstantiationException e) {
            String errorMessage = String.format(
                    "Requested class <%s> does not have relevant accessible single parameter <%s> constructor.",
                    clazz,
                    parameter.getClass()
            );
            logger.warn(errorMessage);
            throw new ClassLoadingException(errorMessage, e);
        }
    }

    public static Object instantiateByClazzWithMultipleParameters(@NotNull Class<?> clazz, @NotNull Object[] parameters) throws ClassLoadingException {
        Class<?>[] parametersClazzes = convertObjectArrayIntoClassArray(parameters);
        try {
            Constructor<?> constructor = clazz.getConstructor(parametersClazzes);
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                InstantiationException e) {
            String errorMessage = String.format(
                    "Requested class <%s> does not have relevant accessible multiple parameter <%s> constructor.",
                    clazz,
                    parametersClazzes
            );
            logger.warn(errorMessage);
            throw new ClassLoadingException(errorMessage, e);
        }
    }

}
