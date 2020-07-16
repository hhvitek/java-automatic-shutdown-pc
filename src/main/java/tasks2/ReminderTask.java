package tasks2;

import model.TaskTemplate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReminderTask extends ParametrizedTask{

    private static final Logger logger = LoggerFactory.getLogger(ReminderTask.class);

    private static final String NAME = "Reminder";
    private static final String DESCRIPTION = "This task will remind user of any message.";

    private static final TaskTemplate taskTemplate = new TaskTemplateImpl(NAME, DESCRIPTION,true, false, ReminderTask.class);

    public ReminderTask(@NotNull String message) {
        super(message);
    }

    public static TaskTemplate getTaskTemplateStatic() {
        return taskTemplate;
    }

    @Override
    public TaskTemplate getTaskTemplate() {
        return getTaskTemplateStatic();
    }

    @Override
    public String execute() throws TaskException {
        logger.info("Executing reminder");
        return "OK.";
    }
}
