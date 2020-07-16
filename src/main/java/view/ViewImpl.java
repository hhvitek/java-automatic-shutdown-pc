package view;

import controller.ControllerImpl;
import model.ModelPropertiesEnum;
import model.TaskModel;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;

public class ViewImpl extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(ViewImpl.class);
    private WindowManager windowManager;

    public ViewImpl(@NotNull ControllerImpl controller, @NotNull TaskModel taskModel) {
        super(controller);
        windowManager = new WindowManager(taskModel, controller);
        controller.setView(this);
    }

    public void run() {
        windowManager.run();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        //TODO exception illegal argument
        switch (ModelPropertiesEnum.valueOf(propertyName)) {
            case SCHEDULED_TASK: {

                break;
            }
            case SELECTED_TASK: {

                break;
            }
            case TIME_DURATION_DELAY: {
                TimeManager newDurationDelay = (TimeManager) evt.getNewValue();
                windowManager.refreshTimingCountdowns(newDurationDelay);
                break;
            }
            default: {
                logger.error("Property change event not recognized. <{}>", propertyName);
            }
        }
    }

    public void refreshTimingCountdowns(@NotNull TimeManager timeManager) {
        windowManager.refreshTimingCountdowns(timeManager);
    }
}
