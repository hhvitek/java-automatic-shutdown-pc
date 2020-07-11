package model;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.ShutdownTask;
import tasks.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderTest {
    @Test
    void shutdownTaskInstantiation_ok_Test() {
        String packageClassName = "tasks.ShutdownTask";
        try {
            Object task = ClassLoader.fromPackageNameClassName(packageClassName);
            Assertions.assertTrue(task instanceof ShutdownTask);
        } catch (ClassLoadingException e) {
            Assertions.fail("Failed to instantiate: " + packageClassName);
        }
    }

    @Test
    void unknownTaskInstantiation_throws_Test() {
        String packageClassName = "tasks.UnknownTask";

        Assertions.assertThrows(ClassLoadingException.class, () -> ClassLoader.fromPackageNameClassName(packageClassName));
    }

    @Test
    void onlyShutdownClassNameInstantiation_throws_Test() {
        String packageClassName = "UnknownTask";

        Assertions.assertThrows(ClassLoadingException.class, () -> ClassLoader.fromPackageNameClassName(packageClassName));
    }
}
