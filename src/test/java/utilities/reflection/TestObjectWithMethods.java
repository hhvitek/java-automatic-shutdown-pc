package utilities.reflection;

public class TestObjectWithMethods {

    int firstArgument;
    int secondArgument;

    public TestObjectWithMethods(){

    }

    public TestObjectWithMethods(Integer firstArgument, Integer secondArgument) {
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    public int sum() {
        return firstArgument + secondArgument;
    }

    public void methodWithoutParametersNoReturn() {

    }

    public void methodWithTwoParametersNoReturn(Integer firstParameter, Integer secondParameter) {

    }

    public int methodWithoutParametersReturnOne() {
        return 1;
    }

    public int methodWithTwoParametersReturnSum(Integer firstParameter, Integer secondParameter) {
        return firstParameter + secondParameter;
    }
}
