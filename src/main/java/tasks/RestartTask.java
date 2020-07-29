package tasks;

import org.jetbrains.annotations.Nullable;

public class RestartTask extends ExecutableTask {

    private static final String NAME = "Restart";
    private static final String DESCRIPTION = "This task will restart the computer.";

    public RestartTask() {
        super(NAME, DESCRIPTION, false, false, RestartTask.class);
    }

    @Override
    public @Nullable String execute(Object... parameters) throws TaskException {
        logger.info("Executing {}", getName());
        throw new TaskException("Error message");
    }
}
