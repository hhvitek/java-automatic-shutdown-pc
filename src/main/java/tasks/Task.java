package tasks;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * This class represents all STATIC data about any Task.
 * Every task is described/defined by those STATIC data.
 */
public final class Task implements TaskTemplate {

    private final String name;
    private final String description;
    private final boolean acceptParameter;
    private final boolean produceResult;
    private final Class<?> clazz;

    public Task(@NotNull String name, @NotNull String description, boolean acceptParameter, boolean produceResult, @NotNull Class<?> clazz) {
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
    public boolean acceptParameter() {
        return acceptParameter;
    }

    @Override
    public boolean doProduceResult() {
        return produceResult;
    }

    @Override
    public @NotNull Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        Task task = (Task) obj;
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
