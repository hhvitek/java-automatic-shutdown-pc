package tasks;

import org.jetbrains.annotations.NotNull;

public abstract class ParametrizedTask extends Task {
    protected ParametrizedTask(@NotNull String name, @NotNull String description) {
        super(name, description, true);
    }
}
