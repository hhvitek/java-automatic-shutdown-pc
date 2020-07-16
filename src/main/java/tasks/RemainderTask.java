package tasks;

public class RemainderTask extends ParametrizedTask {

    private static final String NAME = "Remainder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public RemainderTask() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public String execute() throws TaskException {
        return null;
    }
}
