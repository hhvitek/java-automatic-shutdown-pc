package model;

public class ScheduledTaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6571822367767193760L;

    private final int id;

    public ScheduledTaskNotFoundException(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Requested scheduled task <" + id + "> was not found.";
    }
}
