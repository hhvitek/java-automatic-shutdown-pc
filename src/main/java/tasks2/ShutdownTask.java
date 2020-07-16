package tasks2;

import model.TaskTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskException;

public class ShutdownTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownTask.class);

    private static final String NAME = "Shutdown";
    private static final String DESCRIPTION = "This task will turn the computer off.";

    private static final TaskTemplate taskTemplate = new TaskTemplateImpl(NAME, DESCRIPTION,false, false, ShutdownTask.class)
            ;

    public ShutdownTask() {
    }

    public static TaskTemplate getTaskTemplateStatic() {
        return taskTemplate;
    }

    @Override
    public TaskTemplate getTaskTemplate() {
        return getTaskTemplateStatic();
    }

    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String execute() throws TaskException {
        logger.info("Executing shutdown");
        return "OK.";
    }
}
