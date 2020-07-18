package view;

import controller.ControllerImpl;
import model.ModelObservableEvents;
import model.TaskModel;
import model.TimeManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;

public class ViewImpl extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(ViewImpl.class);
    private final WindowManager windowManager;

    public ViewImpl(@NotNull ControllerImpl controller, @NotNull TaskModel taskModel) {
        super(controller);
        windowManager = new WindowManager(taskModel, controller);
        controller.setView(this);
        controller.addView(this);
    }

    public void run() {
        windowManager.run();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        //TODO exception illegal argument
        switch (ModelObservableEvents.valueOf(propertyName)) {
            case CHOSEN_TIMING_DURATION_DELAY_CHANGED: {
                logger.debug("Chose timing has changed. {} -> {}", evt.getOldValue(), evt.getNewValue());
                String newTimingDurationDelay = evt.getNewValue().toString();
                TimeManager timeManager = new TimeManager(newTimingDurationDelay);
                refreshTimingDurationDelay(timeManager);
                break;
            }
            case TIMER_TICK: {
                windowManager.timerTick();
                break;
            }
            case LAST_SCHEDULED_TASK_CHANGED: {
                int id = (int) evt.getNewValue();
                windowManager.updateLastScheduledTask(id);
            }
        }
    }

    public void showUserError(@NotNull String errorMessage) {
        windowManager.showErrorMessageToUser(errorMessage);
    }

    public void showUserInfo(@NotNull String infoMessage) {
        windowManager.showInfoMessageToUser(infoMessage);
    }

    public void refreshTimingDurationDelay(@NotNull TimeManager timeManager) {
        windowManager.refreshTimingCountdowns(timeManager);
    }

    public void refreshLastScheduledTaskTimingDurationDelay(@NotNull TimeManager timeManager) {
        windowManager.refreshLastScheduledTaskTimingCountdowns(timeManager);
    }


}
