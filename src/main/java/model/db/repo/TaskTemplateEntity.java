package model.db.repo;

import org.jetbrains.annotations.NotNull;
import tasks.TaskTemplate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_template")
public class TaskTemplateEntity implements TaskTemplate {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "accept_parameter", nullable = false)
    private Boolean acceptParameter = false;

    @Column(name = "produce_result", nullable = false)
    private Boolean produceResult = false;

    @Column(name = "clazz", nullable = false)
    private Class<?> clazz = TaskTemplateEntity.class;

    @OneToMany(
            mappedBy = "taskTemplate"
    )
    private List<ScheduledTaskEntity> scheduledTasks = new ArrayList<>();

    protected TaskTemplateEntity() {

    }

    public static TaskTemplateEntity fromTaskTemplate(@NotNull TaskTemplate taskTemplate) {
        TaskTemplateEntity entity = new TaskTemplateEntity();
        entity.name = taskTemplate.getName();
        entity.description = taskTemplate.getDescription();
        entity.produceResult = taskTemplate.doProduceResult();
        entity.acceptParameter = taskTemplate.acceptParameter();
        entity.clazz = taskTemplate.getClazz();
        return entity;
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
}
