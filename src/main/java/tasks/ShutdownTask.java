package tasks;

import org.jetbrains.annotations.Nullable;

public class ShutdownTask extends ExecutableTask {

    private static final String NAME = "Shutdown";
    private static final String DESCRIPTION = "This task will turn the computer off.";

    public ShutdownTask() {
        super(NAME, DESCRIPTION, false, false, ShutdownTask.class);
    }

    @Override
    public @Nullable String execute(Object... parameters) throws TaskException {
        logger.info("Executing {}", getName());
        return "OK.";
    }
}
