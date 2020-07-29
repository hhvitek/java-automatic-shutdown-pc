package model;

/**
 * Any and All events supported by MODEL meaning view could received any of the following events
 */
public enum ModelObservableEvents {
    SCHEDULED_TASK_CREATED, // id, scheduledTask
    SCHEDULED_TASK_STATUS_CHANGED, // id, scheduledTask
    SCHEDULED_TASK_FINISHED, // id, scheduledTaskMessenger
    SCHEDULED_TASK_FINISHED_WITH_ERRORS, // id, scheduledTask
    SELECTED_TASKNAME_CHANGED, // oldName, newName
    SELECTED_TASKPARAMETER_CHANGED, // oldParameter, newParameter
    LAST_SCHEDULED_TASK_CHANGED, // oldId, newId
    SELECTED_TIMING_DURATION_DELAY_CHANGED, // oldTiming, newTiming
}
