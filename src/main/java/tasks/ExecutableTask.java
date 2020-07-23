package tasks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExecutableTask implements TaskTemplate {

    protected static final Logger logger = LoggerFactory.getLogger(Task.class);

    protected Task task;

    protected ExecutableTask(@NotNull String name, @NotNull String description, boolean acceptParameter, boolean produceResult, @NotNull Class<?> clazz) {
        task = new Task(name, description, acceptParameter, produceResult, clazz);
    }

    public abstract @Nullable String execute(Object... parameters) throws TaskException;

    @Override
    public @NotNull String getName() {
        return task.getName();
    }

    @Override
    public @NotNull String getDescription() {
        return task.getDescription();
    }

    @Override
    public boolean acceptParameter() {
        return task.acceptParameter();
    }

    @Override
    public boolean doProduceResult() {
        return task.doProduceResult();
    }

    @Override
    public @NotNull Class<?> getClazz() {
        return task.getClazz();
    }
}
