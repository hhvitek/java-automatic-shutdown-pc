package tasks2;

import model.TaskTemplate;

interface Task {

    TaskTemplate getTaskTemplate();

    String getParameter();

    String execute() throws TaskException;
}
