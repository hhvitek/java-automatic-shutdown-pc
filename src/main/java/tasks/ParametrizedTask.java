package tasks;

import org.jetbrains.annotations.NotNull;

public abstract class ParametrizedTask extends Task {

    protected ParametrizedTask(@NotNull String name, @NotNull String description, boolean produceResult, Class<?> clazz) {
        super(name, description, true, produceResult, clazz);
    }

    @Override
    public String execute() {
        throw new UnsupportedOperationException("No parameterized execution not supported.");
    }

    @Override
    public abstract String execute(@NotNull String parameter);
}
