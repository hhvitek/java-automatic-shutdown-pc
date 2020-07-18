package tasks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemainderTask extends ParametrizedTask{

    private static final String NAME = "Remainder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public RemainderTask() {
        super(NAME, DESCRIPTION, false, RemainderTask.class);
    }

    @Override
    public @Nullable String execute(@NotNull String parameter) {
        logger.info("Executing {}, with parameter {}", name, parameter);
        return "Remainder";
    }
}
