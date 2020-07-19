package model.db.repo;

import model.ScheduledTaskStatus;
import model.TimeManager;
import tasks.Task;

import javax.persistence.*;

@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Transient
    private Task task;

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

}
