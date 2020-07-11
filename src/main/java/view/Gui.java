package view;

import model.StateModel;
import model.TaskModel;
import org.jetbrains.annotations.NotNull;

public class Gui {

    private final TaskModel taskModel;
    private final StateModel stateModel;
    private final WindowManager windowManager;

    public Gui(@NotNull TaskModel taskModel, @NotNull StateModel stateModel) {
        this.taskModel = taskModel;
        this.stateModel = stateModel;

        windowManager = new WindowManager();
    }

    public void run() {
        windowManager.run();
    }
}
