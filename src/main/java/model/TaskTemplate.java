package model;

import org.jetbrains.annotations.NotNull;

public interface TaskTemplate {
    @NotNull String getName();

    @NotNull String getDescription();

    boolean acceptParameter();

    boolean doProduceResult();

    @NotNull Class<?> getClazz();
}
