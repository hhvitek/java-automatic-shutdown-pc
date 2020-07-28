package tasks;

import org.jetbrains.annotations.Nullable;

public class ReminderTask extends ExecutableTask {

    private static final String NAME = "Reminder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public ReminderTask() {
        super(NAME, DESCRIPTION, true, false, ReminderTask.class);
    }

    @Override
    public @Nullable String execute(Object... parameters) throws TaskException {
        return "EXECUTABLE PARAMETRIZED TASK";
    }
}
