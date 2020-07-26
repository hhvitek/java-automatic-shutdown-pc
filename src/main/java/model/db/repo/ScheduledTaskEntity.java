package model.db.repo;

import model.TimeManager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.ExecutableTask;
import tasks.TaskTemplate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskEntity {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskEntity.class);

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "fk_task_template", nullable = false)
    private TaskTemplateEntity taskTemplate;

    @Column(name = "when_elapsed")
    private Instant instant;

    @Column(name = "parameter")
    private String parameter;

    @Column(name = "output")
    private String output;

    @Column(name = "error_message")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ScheduledTaskStatus status;

    ScheduledTaskEntity() {
    }

    public ScheduledTaskEntity(@NotNull TaskTemplateEntity entity, @NotNull TimeManager whenElapsed) {
        this.taskTemplate = entity;
        instant = whenElapsed.getWhenElapsedPointInTime();
        status = ScheduledTaskStatus.CREATED;

        logger.info("The new task has been created: <{}>.", this);
    }

    public ScheduledTaskEntity(@NotNull TaskTemplateEntity task, @NotNull TimeManager whenElapsed, @Nullable String parameter) {
        this(task, whenElapsed);
        this.parameter = parameter;
    }

    public TaskTemplate getTaskTemplate() {
        return taskTemplate;
    }

    public Integer getId() {
        return id;
    }

    public TimeManager getWhenElapsed() {
        return new TimeManager(instant);
    }


    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ScheduledTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduledTaskStatus status) {
        this.status = status;
    }

}
