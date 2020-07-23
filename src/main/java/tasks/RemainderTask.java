package tasks;

import org.jetbrains.annotations.Nullable;

public class RemainderTask extends ExecutableTask {

    private static final String NAME = "Remainder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public RemainderTask() {
        super(NAME, DESCRIPTION, true, false, RemainderTask.class);
    }


    @Override
    public @Nullable String execute(Object... parameters) throws TaskException {
        return "EXECUTABLE PRAMETRIZED TASK";
    }
}
