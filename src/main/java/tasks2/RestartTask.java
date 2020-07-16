package tasks2;

import model.TaskTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.TaskException;

public class RestartTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(RestartTask.class);

    private static final String NAME = "Restart";
    private static final String DESCRIPTION = "This task will restart the computer.";

    private static final TaskTemplate taskTemplate = new TaskTemplateImpl(NAME, DESCRIPTION,false, false, RestartTask.class);

    public RestartTask() {
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
        logger.info("Executing restart");
        return "OK.";
    }
}
