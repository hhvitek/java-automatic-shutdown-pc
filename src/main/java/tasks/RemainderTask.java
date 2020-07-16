package tasks;

import org.jetbrains.annotations.NotNull;
import tasks2.ReminderTask;

public class RemainderTask extends ParametrizedTask{

    private static final String NAME = "Remainder";
    private static final String DESCRIPTION = "This task will remind user any message.";

    public RemainderTask() {
        super(NAME, DESCRIPTION, false, ReminderTask.class);
    }

    @Override
    public String execute(@NotNull String parameter) {
        logger.info("Executing {}, with parameter {}", name, parameter);
        return "Remainder";
    }
}
