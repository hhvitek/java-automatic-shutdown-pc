package tasks;

import org.jetbrains.annotations.Nullable;

public class ShutdownTask extends Task {

    private static final String NAME = "Shutdown";
    private static final String DESCRIPTION = "This task will turn the computer off.";

    public ShutdownTask() {
        super(NAME, DESCRIPTION, false, false, ShutdownTask.class);
    }

    @Override
    public @Nullable String execute() throws TaskException {
        logger.info("Executing {}", name);
        return "OK.";
    }
}
