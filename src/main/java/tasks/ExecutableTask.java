package tasks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExecutableTask extends TaskTemplateImpl {

    protected static final Logger logger = LoggerFactory.getLogger(ExecutableTask.class);

    protected ExecutableTask(
            @NotNull String name,
            @NotNull String description,
            boolean acceptParameter,
            boolean produceResult,
            @NotNull Class<?> clazz)
    {
        super(name, description, acceptParameter, produceResult, clazz);
    }

    public abstract @Nullable String execute(Object... parameters) throws TaskException;
}
