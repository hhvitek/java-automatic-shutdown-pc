package tasks2;

import org.jetbrains.annotations.NotNull;

public abstract class ParametrizedTask implements Task {

    protected final String parameter;

    protected ParametrizedTask(@NotNull String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getParameter() {
        return parameter;
    }
}
