package model;

public enum ScheduledTaskStatus {

    CREATED, // just been created
    SCHEDULED, // created and sleeping
    ELAPSED, // now > task whenElapsed, waiting to be executed
    RUNNING, // executed and still running
    EXECUTED_SUCCESS, // finished successfully
    EXECUTED_ERROR, // finished with errors
    CANCELLED, // cancelled by user
    DELETED; // marked as read

    public boolean isLessThan(ScheduledTaskStatus other) {
        return compareTo(other) < 0;
    }

    public boolean isBiggerThan(ScheduledTaskStatus other) {
        return compareTo(other) > 0;
    }
}
