package model.db.operations;

import model.ScheduledTaskModel;
import model.TaskModel;
import model.TimeManager;
import model.scheduledtasks.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.ExecutableTask;

import javax.persistence.EntityManager;

public class ScheduledTaskModelJpaImpl extends ScheduledTaskModel {

    protected final EntityManager entityManager;

    public ScheduledTaskModelJpaImpl(@NotNull EntityManager entityManager, @NotNull TaskModel taskModel)  {
        super(new ManagerJpaImpl(entityManager), taskModel);
        this.entityManager = entityManager;
    }

    @Override
    protected ScheduledTask instantiateNewScheduleTask(@NotNull ExecutableTask task, @NotNull TimeManager durationDelay, @Nullable String parameter) {
        return new ScheduledTaskJpaImpl(entityManager, task, durationDelay, parameter);
    }
}
