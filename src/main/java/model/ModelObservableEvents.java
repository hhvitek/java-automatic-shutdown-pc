package model;

/**
 * Any and All events supported by MODEL meaning view could received any of the following events
 */
public enum ModelObservableEvents {
    SCHEDULED_TASK_CREATED,
    SCHEDULED_TASK_STATUS_CHANGED,
    SCHEDULED_TASK_FINISHED,
    SCHEDULED_TASK_FINISHED_WITH_ERRORS,
    SELECTED_TASKNAME_CHANGED,
    SELECTED_TASKPARAMETER_CHANGED,
    LAST_SCHEDULED_TASK_CHANGED,
    SELECTED_TIMING_DURATION_DELAY_CHANGED,
    TIMER_TICK,

}
