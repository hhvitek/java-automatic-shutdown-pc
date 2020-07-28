package model.db.repo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "state_model")
public class StateModelEntity {

    @Id
    @GeneratedValue
    @SequenceGenerator(name = "sequence_generator", allocationSize = 1)
    private Integer id;

    @Column(name = "default_timing_duration_delay", nullable = false)
    private String defaultTimingDurationDelay = "01:00";

    @Column(name = "default_selected_task_name", nullable = false)
    private String defaultSelectedTaskName = "Shutdown";

    // data in ui
    @Column(name = "selected_task_name", nullable = false)
    private String selectedTaskName = "";

    @Column(name = "selected_task_parameter", nullable = false)
    private String selectedTaskParameter = "";

    @Column(name = "timing_duration_delay", nullable = false)
    private String timingDurationDelay = "00:00";

    @Column(name = "last_scheduled_task_id", nullable = false)
    private Integer lastScheduledTaskId = Integer.MIN_VALUE;

    protected StateModelEntity() {
    }

    public StateModelEntity(@NotNull String defaultDurationDelay, @NotNull String defaultSelectedTaskName) {
        defaultTimingDurationDelay = defaultDurationDelay;
        this.defaultSelectedTaskName = defaultSelectedTaskName;

        setTimingDurationDelay(defaultTimingDurationDelay);
        setSelectedTaskName(defaultSelectedTaskName);

        setLastScheduledTaskId(Integer.MIN_VALUE);
    }

    public Integer getId() {
        return id;
    }

    public @NotNull String getDefaultTimingDurationDelay() {
        return defaultTimingDurationDelay;
    }

    public void setDefaultTimingDurationDelay(@NotNull String defaultTimingDurationDelay) {
        this.defaultTimingDurationDelay = defaultTimingDurationDelay;
    }

    public String getDefaultSelectedTaskName() {
        return defaultSelectedTaskName;
    }

    public void setDefaultSelectedTaskName(@NotNull String defaultSelectedTaskName) {
        this.defaultSelectedTaskName = defaultSelectedTaskName;
    }

    public String getSelectedTaskName() {
        return selectedTaskName;
    }

    public void setSelectedTaskName(@NotNull String selectedTaskName) {
        this.selectedTaskName = selectedTaskName;
    }

    public String getSelectedTaskParameter() {
        return selectedTaskParameter;
    }

    public void setSelectedTaskParameter(@NotNull String selectedTaskParameter) {
        this.selectedTaskParameter = selectedTaskParameter;
    }

    public String getTimingDurationDelay() {
        return timingDurationDelay;
    }

    public void setTimingDurationDelay(@NotNull String timingDurationDelay) {
        this.timingDurationDelay = timingDurationDelay;
    }

    public Integer getLastScheduledTaskId() {
        return lastScheduledTaskId;
    }

    public void setLastScheduledTaskId(@NotNull Integer lastScheduledTaskId) {
        this.lastScheduledTaskId = lastScheduledTaskId;
    }
}
