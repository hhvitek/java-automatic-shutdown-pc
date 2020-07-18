package tasks;

import org.jetbrains.annotations.NotNull;

public class TaskException extends RuntimeException {

    private static final long serialVersionUID = 7264092719543286973L;

    public TaskException(@NotNull String message) {
        super(message);
    }
}
