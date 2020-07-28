package tasks;

import org.jetbrains.annotations.NotNull;

public interface TaskTemplate {
    @NotNull String getName();

    @NotNull String getDescription();

    boolean canAcceptParameter();

    boolean canProduceResult();

    @NotNull Class<?> getClazz();
}
