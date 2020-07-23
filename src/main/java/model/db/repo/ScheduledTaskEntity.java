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
import java.util.Objects;

@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskEntity {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskEntity.class);

    @Id
    @GeneratedValue
    private Integer id;

    @Transient
    private ExecutableTask task;

    @Column(name = "task_name")
    private String taskName;

    @Transient
    private TimeManager whenElapsed;

    @Column(name = "when_elapsed")
    private Long instant;

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
        this.task = task;
        this.whenElapsed = whenElapsed;
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

    public Long getInstant() {
        return instant;
    }

    public void setInstant(Long instant) {
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

    public TimeManager getWhenElapsed() {
        return whenElapsed;
    }

    public ExecutableTask getExecutableTask() {
        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledTaskEntity that = (ScheduledTaskEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(task, that.task) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(whenElapsed, that.whenElapsed) &&
                Objects.equals(instant, that.instant) &&
                Objects.equals(parameter, that.parameter) &&
                Objects.equals(output, that.output) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, taskName, whenElapsed, instant, parameter, output, errorMessage, status);
    }

    public static ScheduledTaskEntity createFromScheduledTask(@NotNull ScheduledTask task) {
        ScheduledTaskEntity entity = new ScheduledTaskEntity();
        entity.id = task.getId();
        entity.parameter = task.getTaskParameter().get();
        entity.status = task.getStatus();
        entity.whenElapsed = task.getWhenElapsed();
        entity.taskName = task.getTaskTemplate().getName();
        entity.errorMessage = task.getErrorMessage();
        entity.output = task.getOutput();
        return null;
    }

}
