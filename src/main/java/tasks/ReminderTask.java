package tasks;

import org.jetbrains.annotations.Nullable;

public class ReminderTask extends ExecutableTask {

    private static final String NAME = "Reminder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public ReminderTask() {
        super(NAME, DESCRIPTION, true, true, ReminderTask.class);
    }

    @Override
    public @Nullable String execute(Object... parameters) throws TaskException {
        logger.info("Executing {}.{}", getName(), parameters);
        if (parameters != null && parameters.length > 0) {
            return "Reminder output with parameter: " + parameters[0];
        } else {
            throw new TaskException("No parameter.");
        }
    }
}
