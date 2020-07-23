package model.db.repo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "state_model")
public class StateModelEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "default_timing_duration_delay")
    private String defaultTimingDurationDelay;

    @Column(name = "default_selected_task_name")
    private String defaultSelectedTaskName;

    // data in ui
    @Column(name = "selected_task_name")
    private String selectedTaskName = "";

    @Column(name = "selected_task_parameter")
    private String selectedTaskParameter = "";

    @Column(name = "timing_duration_delay")
    private String timingDurationDelay;

    @Column(name = "last_scheduled_task_id")
    private Integer lastScheduledTaskId;

    StateModelEntity() {
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

    public String getDefaultTimingDurationDelay() {
        return defaultTimingDurationDelay;
    }

    public void setDefaultTimingDurationDelay(String defaultTimingDurationDelay) {
        this.defaultTimingDurationDelay = defaultTimingDurationDelay;
    }

    public String getDefaultSelectedTaskName() {
        return defaultSelectedTaskName;
    }

    public void setDefaultSelectedTaskName(String defaultSelectedTaskName) {
        this.defaultSelectedTaskName = defaultSelectedTaskName;
    }

    public String getSelectedTaskName() {
        return selectedTaskName;
    }

    public void setSelectedTaskName(String selectedTaskName) {
        this.selectedTaskName = selectedTaskName;
    }

    public String getSelectedTaskParameter() {
        return selectedTaskName;
    }

    public void setSelectedTaskParameter(String selectedTaskParameter) {
        this.selectedTaskParameter = selectedTaskParameter;
    }

    public String getTimingDurationDelay() {
        return timingDurationDelay;
    }

    public void setTimingDurationDelay(String timingDurationDelay) {
        this.timingDurationDelay = timingDurationDelay;
    }

    public Integer getLastScheduledTaskId() {
        return lastScheduledTaskId;
    }

    public void setLastScheduledTaskId(Integer lastScheduledTaskId) {
        this.lastScheduledTaskId = lastScheduledTaskId;
    }
}
