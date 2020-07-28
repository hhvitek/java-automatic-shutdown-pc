package tasks;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class represents all STATIC data about any Task.
 * Every task is described/defined by those STATIC data.
 */
public class TaskTemplateImpl implements TaskTemplate {

    private final String name;
    private final String description;
    private final boolean acceptParameter;
    private final boolean produceResult;
    private final Class<?> clazz;

    public TaskTemplateImpl(
            @NotNull String name,
            @NotNull String description,
            boolean acceptParameter,
            boolean produceResult,
            @NotNull Class<?> clazz)
    {
        this.name = name;
        this.description = description;
        this.acceptParameter = acceptParameter;
        this.produceResult = produceResult;
        this.clazz = clazz;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public boolean canAcceptParameter() {
        return acceptParameter;
    }

    @Override
    public boolean canProduceResult() {
        return produceResult;
    }

    @Override
    public @NotNull Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TaskTemplateImpl)) return false;
        TaskTemplateImpl task = (TaskTemplateImpl) obj;
        return name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
