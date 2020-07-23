package utilities.reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Using Java Reflection API.
 * Helper class...
 *  * To instantiate object based on class.getName() "package.className" string representation.
 *  * To search and invoke instance's method based on method's string name.
 *
 *  Could be configured to throw ReflectionApiException on Error or/and to log errors
 */
public final class ReflectionApi {

    public static enum LEVEL {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    private static final Logger logger = LoggerFactory.getLogger(ReflectionApi.class);

    private boolean shouldLog;
    //logging facility such as DEBUG, INFO, WARN, ...
    private LEVEL logLevel;

    private boolean shouldThrowException;

    private ReflectionApi() {
        this(true, true, LEVEL.DEBUG);
    }

    public ReflectionApi(boolean shouldThrow, boolean shouldLog) {
        shouldLog(shouldLog);
        shouldThrowExceptionOnError(shouldThrow);
        logLevel(LEVEL.DEBUG);
    }

    public ReflectionApi(boolean shouldThrow, boolean shouldLog, @NotNull LEVEL logLevel) {
        shouldLog(shouldLog);
        shouldThrowExceptionOnError(shouldThrow);
        logLevel(logLevel);
    }

    public void shouldLog(boolean shouldLog) {
        this.shouldLog = shouldLog;
    }

    public void logLevel(@NotNull LEVEL logLevel) {
        this.logLevel = logLevel;
    }

    public void shouldThrowExceptionOnError(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    private void logIfShould(@NotNull String logMessage, Object... objects) {
        if (shouldLog) {
            switch (logLevel) {
                case TRACE:
                    logger.trace(logMessage, objects);
                    break;
                case DEBUG:
                    logger.debug(logMessage, objects);
                    break;
                case INFO:
                    logger.info(logMessage, objects);
                    break;
                case WARN:
                    logger.warn(logMessage, objects);
                    break;
                case ERROR:
                    logger.error(logMessage, objects);
                    break;
            }
        }
    }

    private void throwIfShould(@Nullable String message, @Nullable Throwable throwable) throws ReflectionApiException {
        if (shouldThrowException) {
            throw new ReflectionApiException(message, throwable);
        }
    }

    private void throwIfShould(@NotNull String message) throws ReflectionApiException {
        throwIfShould(message, null);
    }

    private void throwIfShould(@NotNull Throwable throwable) throws ReflectionApiException {
        throwIfShould(null, throwable);
    }

    //METHOD INVOKE#####################################################################################################

    public void invokeMethodWithoutParameters(@NotNull Object instance, @NotNull String methodName) throws ReflectionApiException {
        invokeMethodWithParameters(instance, methodName);
    }

    public void invokeMethodWithParameters(@NotNull Object instance, @NotNull String methodName, @Nullable Object... methodParameters) throws ReflectionApiException {
        Class<?> clazz = instance.getClass();
        try {
            Method method = clazz.getMethod(methodName, convertObjectArrayIntoClassArray(methodParameters));
            method.invoke(instance, methodParameters);
        } catch (Exception ex) {
            //  Handle exception.
            logIfShould("Failed to invoke model's method <{}>.<{}>({}) using utilities.reflection API. Method not found.", clazz, methodName, methodParameters, ex);
            throwIfShould(ex);
        }
    }

    public @Nullable Object invokeMethodWithParametersReturnValue(@NotNull Object instance, @NotNull String methodName, @Nullable Object... methodParameters) throws ReflectionApiException {
        Class<?> clazz = instance.getClass();
        try {
            Method method = clazz.getMethod(methodName, convertObjectArrayIntoClassArray(methodParameters));
            return method.invoke(instance, methodParameters);
        } catch (Exception ex) {
            //  Handle exception.
            logIfShould("Failed to invoke model's method <{}>.<{}>({}) using utilities.reflection API. Method not found.", clazz, methodName, methodParameters, ex);
            throwIfShould(ex);
            return null;
        }
    }

    private static Class<?>[] convertObjectArrayIntoClassArray(@Nullable Object... objects) {
        return Arrays.stream(objects)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }

    //CLASS LOADER##################################################################################################################

    /**
     * Parameter must be in the format package.className convention
     *
     * @throws ReflectionApiException
     */
    public @Nullable Object instantiateFromStringPackageNameClassName(@NotNull String packageNameClassName) throws ReflectionApiException {
        return instantiateWithParametersFromStringPackageNameClassName(packageNameClassName);
    }

    /**
     * Parameter must be in the format package.className convention
     *
     * @throws ReflectionApiException
     */
    public @Nullable Object instantiateWithParametersFromStringPackageNameClassName(@NotNull String packageNameClassName, @Nullable Object... objects) throws ReflectionApiException {
        try {
            Class<?> clazz = Class.forName(packageNameClassName);
            Constructor<?> constructor = clazz.getConstructor(convertObjectArrayIntoClassArray(objects));
            return constructor.newInstance(objects);
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                ClassNotFoundException |
                InstantiationException e) {
            String errorMessage = "Class <" + packageNameClassName + "> not found. Forgot package name \"package.className\"?";
            logIfShould(errorMessage);
            throwIfShould(errorMessage, e);
            return null;
        }
    }

    public @Nullable Object instantiateByClazz(@NotNull Class<?> clazz) throws ReflectionApiException {
        return instantiateFromStringPackageNameClassName(clazz.getName());
    }

    public @Nullable Object instantiateByClazzWithMultipleParameters(@NotNull Class<?> clazz, @NotNull Object... parameters) throws ReflectionApiException {
        return instantiateWithParametersFromStringPackageNameClassName(clazz.getName(), parameters);
    }

}
