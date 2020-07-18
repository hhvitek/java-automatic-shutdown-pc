package utilities.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.ShutdownTask;

class ReflectionApiClassLoaderTest {
    @Test
    void shutdownTaskInstantiation_ok_Test() {
        String packageClassName = "tasks.ShutdownTask";
        try {
            Object task = ReflectionApi.instantiateFromStringPackageNameClassName(packageClassName);
            Assertions.assertTrue(task instanceof ShutdownTask);
        } catch (ClassLoadingException e) {
            Assertions.fail("Failed to instantiate: " + packageClassName);
        }
    }

    @Test
    void unknownTaskInstantiation_throws_Test() {
        String packageClassName = "tasks.UnknownTask";

        Assertions.assertThrows(ClassLoadingException.class, () -> ReflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }

    @Test
    void onlyShutdownClassNameInstantiation_throws_Test() {
        String packageClassName = "UnknownTask";

        Assertions.assertThrows(ClassLoadingException.class, () -> ReflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }
}
