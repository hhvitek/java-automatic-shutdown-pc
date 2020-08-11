package model.db.repo;

import model.TimeManager;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskTemplate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskEntity {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskEntity.class);

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_task_template", nullable = false)
    private TaskTemplateEntity taskTemplate;

    @Column(name = "when_elapsed", nullable = false)
    private Instant instant = Instant.MIN;

    @Column(name = "parameter", nullable = false)
    private String parameter = "";

    @Column(name = "output", nullable = false)
    private String output = "";

    @Column(name = "error_message", nullable = false)
    private String errorMessage = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduledTaskStatus status = ScheduledTaskStatus.CREATED;

    protected ScheduledTaskEntity() {
    }

    public ScheduledTaskEntity(@NotNull TaskTemplateEntity entity, @NotNull TimeManager whenElapsed) {
        taskTemplate = entity;
        instant = whenElapsed.getWhenElapsedPointInTime();

        logger.debug("The new task has been created: <{}>.", this);
    }

    public ScheduledTaskEntity(@NotNull TaskTemplateEntity task, @NotNull TimeManager whenElapsed, @NotNull String parameter) {
        this(task, whenElapsed);
        this.parameter = parameter;
    }

    public @NotNull TaskTemplate getTaskTemplate() {
        return taskTemplate;
    }

    public @NotNull Integer getId() {
        return id;
    }

    public @NotNull TimeManager getWhenElapsed() {
        return new TimeManager(instant);
    }


    public @NotNull String getParameter() {
        return parameter;
    }

    public void setParameter(@NotNull String parameter) {
        this.parameter = parameter;
    }

    public @NotNull String getOutput() {
        return output;
    }

    public void setOutput(@NotNull String output) {
        this.output = output;
    }

    public @NotNull String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(@NotNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public @NotNull ScheduledTaskStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull ScheduledTaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ScheduledTaskEntity{" +
                "id=" + id +
                ", taskTemplate=" + taskTemplate +
                ", status=" + status +
                '}';
    }
}
