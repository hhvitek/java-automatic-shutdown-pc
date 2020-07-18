package tasks;

import model.TaskTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Task implements TaskTemplate {

    protected static final Logger logger = LoggerFactory.getLogger(Task.class);

    protected final String name;
    protected final String description;
    protected final boolean acceptParameter;
    protected final boolean produceResult;
    protected final Class<?> clazz;

    protected Task(@NotNull String name, @NotNull String description, boolean acceptParameter, boolean produceResult, @NotNull Class<?> clazz) {
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

    public abstract @Nullable String execute() throws TaskException;

    public @Nullable String execute(@NotNull String parameter) throws TaskException {
        throw new UnsupportedOperationException("Parametrized execution not supported.");
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
