package model;

public enum ScheduledTaskStatus {

    SCHEDULED, // created and running
    ELAPSED, // now > task whenElapsed
    CANCELLED, // cancelled by user
    DELETED; // marked as read

    public boolean isLessThan(ScheduledTaskStatus other) {
        return this.compareTo(other) < 0;
    }

    public boolean isBiggerThan(ScheduledTaskStatus other) {
        return this.compareTo(other) > 0;
    }
}
