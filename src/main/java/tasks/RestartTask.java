package tasks;

public class RestartTask extends Task {

    private static final String NAME = "Restart";
    private static final String DESCRIPTION = "This task will restart the computer.";

    public RestartTask() {
        super(NAME, DESCRIPTION, false);
    }

    @Override
    public String execute() throws TaskException {
        logger.info("Executing {}", name);
        throw new TaskException("Test error exception");
    }
}
