package model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2031613558536662709L;
    private final String taskName;
    private List<String> relevantTaskNames;

    public TaskNotFoundException(@NotNull String taskName) {
        this.taskName = taskName;
    }
    public TaskNotFoundException(@NotNull String taskName, @NotNull List<String> relevantTaskNames) {
        this(taskName);
        this.relevantTaskNames = relevantTaskNames;
    }

    @Override
    public String toString() {
        if (relevantTaskNames == null) {
            return "The task <" + taskName + "> was not found.";
        } else {
            return String.format(
                    "The task <%s> was not found. The following are allowed task names: <%s>.",
                    taskName,
                    String.join(", ", relevantTaskNames)
            );
        }
    }
}
