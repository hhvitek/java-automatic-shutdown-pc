package model.db.repo;

import model.TimeManager;
import model.scheduledtasks.ScheduledTask;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.ExecutableTask;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskEntity {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskEntity.class);

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "task_name")
    private String taskName;

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

    public ScheduledTaskEntity(@NotNull ExecutableTask task, @NotNull TimeManager whenElapsed) {
        taskName = task.getName();
        instant = whenElapsed.getWhenElapsedPointInTime();
        status = ScheduledTaskStatus.CREATED;

        logger.info("The new task has been created: <{}>.", this);
    }

    public ScheduledTaskEntity(@NotNull ExecutableTask task, @NotNull TimeManager whenElapsed, @Nullable String parameter) {
        this(task, whenElapsed);
        this.parameter = parameter;
    }

    public Integer getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledTaskEntity that = (ScheduledTaskEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(instant, that.instant) &&
                Objects.equals(parameter, that.parameter) &&
                Objects.equals(output, that.output) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, instant, parameter, output, errorMessage, status);
    }

    public static @NotNull ScheduledTaskEntity createFromScheduledTask(@NotNull ScheduledTask task) {
        ScheduledTaskEntity entity = new ScheduledTaskEntity();
        entity.id = task.getId();
        entity.parameter = task.getTaskParameter().orElse("");
        entity.status = task.getStatus();
        entity.taskName = task.getTaskTemplate().getName();
        entity.errorMessage = task.getErrorMessage();
        entity.output = task.getOutput();
        entity.instant = task.getWhenElapsed().getWhenElapsedPointInTime();
        return entity;
    }

}
