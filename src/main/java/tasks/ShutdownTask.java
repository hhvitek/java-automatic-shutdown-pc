package tasks;

public class ShutdownTask extends Task {

    private static final String NAME = "Shutdown";
    private static final String DESCRIPTION = "This task will turn the computer off.";

    public ShutdownTask() {
        super(NAME, DESCRIPTION, false);
    }

    @Override
    public String execute() throws TaskException {
        logger.info("Executing {}", name);
        return "OK.";
    }
}
