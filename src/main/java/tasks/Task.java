package tasks;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class Task {

    protected static final Logger logger = LoggerFactory.getLogger(Task.class);

    protected final String name;
    protected final String description;
    protected final boolean acceptParameter;

    protected Task(@NotNull String name, @NotNull String description, boolean acceptParameter) {
        this.name = name;
        this.description = description;
        this.acceptParameter = acceptParameter;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean acceptParameter() {
        return acceptParameter;
    }

    public abstract String execute() throws TaskException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
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
