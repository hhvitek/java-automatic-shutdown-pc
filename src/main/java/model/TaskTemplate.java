package model;

public interface TaskTemplate {
    String getName();

    String getDescription();

    boolean acceptParameter();

    boolean doProduceResult();

    Class<?> getClazz();
}
