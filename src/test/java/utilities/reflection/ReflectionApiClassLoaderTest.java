package utilities.reflection;

import org.junit.jupiter.api.*;
import tasks.ShutdownTask;

class ReflectionApiClassLoaderTest {


    ReflectionApi reflectionApi;
    static TestObjectWithMethods testObj;
    static String packageClassName;

    @BeforeEach
    void init() {
        reflectionApi = new ReflectionApi(true, false);
    }

    @BeforeAll
    static void classInit() {
        testObj = new TestObjectWithMethods();
        packageClassName = TestObjectWithMethods.class.getName();
    }


    @Test
    void shutdownTaskInstantiation_ok_Test() {
        String packageClassName = "tasks.ShutdownTask";
        try {
            Object task = reflectionApi.instantiateFromStringPackageNameClassName(packageClassName);
            Assertions.assertTrue(task instanceof ShutdownTask);
        } catch (ReflectionApiException e) {
            Assertions.fail("Failed to instantiate: " + packageClassName);
        }
    }

    @Test
    void unknownTaskInstantiation_throws_Test() {
        String packageClassName = "tasks.UnknownTask";

        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }

    @Test
    void unknownTaskClassNameInstantiation_throws_Test() {
        String packageClassName = "UnknownTask";

        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }

    @Tag("MANUAL")
    @Test
    void manualTest_doLog_unknownTaskClassNameInstantiation_throws_Test() {
        System.out.println("1] There should be a debug log here:");
        reflectionApi.shouldLog(true);

        String packageClassName = "UnknownTask";

        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }

    @Tag("MANUAL")
    @Test
    void manualTest_logWithDifferentLogFacility_Test() {
        reflectionApi.shouldThrowExceptionOnError(false);
        reflectionApi.shouldLog(true);
        reflectionApi.logLevel(ReflectionApi.LogLevel.INFO);
        System.out.println("2] There should be <INFO> facility:");

        String packageClassName = "UnknownTask";

        Assertions.assertDoesNotThrow(() -> reflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
    }

    @Test
    void doNotThrow_onlyShutdownClassNameInstantiation_throws_Test() {
        reflectionApi.shouldThrowExceptionOnError(false);

        String packageClassName = "UnknownTask";

        Assertions.assertDoesNotThrow(() -> reflectionApi.instantiateFromStringPackageNameClassName(packageClassName));
        reflectionApi.shouldThrowExceptionOnError(true);
    }

    @Test
    void initializeDefaultConstructorTest() {
        try {
            Object instance = reflectionApi.instantiateFromStringPackageNameClassName(packageClassName);
            Assertions.assertTrue(instance instanceof TestObjectWithMethods);
        } catch (ReflectionApiException e) {
            Assertions.fail("Failed to instantiate: " + packageClassName);
        }
    }

    @Test
    void initializeWithParametersTest() {
        int firstParameter = 1;
        int secondParameter = 2;

        try {
            Object instance = reflectionApi.instantiateWithParametersFromStringPackageNameClassName(packageClassName, firstParameter, secondParameter);
            Assertions.assertTrue(instance instanceof TestObjectWithMethods);

            TestObjectWithMethods testObject = (TestObjectWithMethods) instance;

            int expectedSum = firstParameter + secondParameter;
            Assertions.assertEquals(expectedSum, testObject.sum());

        } catch (ReflectionApiException e) {
            Assertions.fail("Failed to instantiate: " + packageClassName);
        }
    }

    //METHODS###########################################################################################################

    @Test
    void invokingMethodWithoutParameters_noReturn_Test() {
        String methodName = "methodWithoutParametersNoReturn";

        Assertions.assertDoesNotThrow(() -> reflectionApi.invokeMethodWithoutParameters(testObj, methodName));

        String nonExistentMethod = "nonExistentMethod";

        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.invokeMethodWithoutParameters(testObj, nonExistentMethod));
    }

    @Test
    void invokingMethodWithTwoParameters_noReturn_Test() {
        String methodName = "methodWithTwoParametersNoReturn";
        int firstParameter = 1;
        int secondParameter = 2;

        Assertions.assertDoesNotThrow(() -> reflectionApi.invokeMethodWithParameters(testObj, methodName, firstParameter, secondParameter));

        String nonExistentMethod = "nonExistentMethod";
        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.invokeMethodWithParameters(testObj, nonExistentMethod, firstParameter, secondParameter));
    }

    @Test
    void invokingMethodWithTwoParameters_ReturnSum_Test() {
        String methodName = "methodWithTwoParametersReturnSum";
        int firstParameter = 1;
        int secondParameter = 2;
        int expectedSum = firstParameter + secondParameter;


        Assertions.assertDoesNotThrow(
                () -> reflectionApi.invokeMethodWithParametersReturnValue(testObj, methodName, firstParameter, secondParameter)
        );

        int actualSum = (int) reflectionApi.invokeMethodWithParametersReturnValue(testObj, methodName, firstParameter, secondParameter);
        Assertions.assertEquals(expectedSum, actualSum);

        String nonExistentMethod = "nonExistentMethod";
        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.invokeMethodWithParameters(testObj, nonExistentMethod, firstParameter, secondParameter));
    }

    @Test
    void invokingMethodWithoutParameter_NoReturn_Test() {
        String methodName = "methodWithoutParametersNoReturn";

        Assertions.assertDoesNotThrow(() -> reflectionApi.invokeMethodWithParameters(testObj, methodName));

        String nonExistentMethod = "nonExistentMethod";

        Assertions.assertThrows(ReflectionApiException.class, () -> reflectionApi.invokeMethodWithParameters(testObj, nonExistentMethod));
    }
}
