package tasks2;

import model.TaskTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TaskTemplateImpl implements TaskTemplate {

    private final String name;
    private final String description;
    private final boolean acceptParameter;
    private final boolean produceResult;
    private final Class<?> clazz;

    public TaskTemplateImpl(@NotNull String name, @NotNull String description, boolean acceptParameter, boolean produceResult, @NotNull Class<?> clazz) {
        this.name = name;
        this.description = description;
        this.acceptParameter = acceptParameter;
        this.produceResult = produceResult;
        this.clazz = clazz;
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

    public boolean doProduceResult() {
        return produceResult;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof tasks.Task)) return false;
        TaskTemplateImpl task = (TaskTemplateImpl) o;
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
