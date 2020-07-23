package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static model.ModelObservableEvents.SELECTED_TASKNAME_CHANGED;
import static model.ModelObservableEvents.SELECTED_TASKPARAMETER_CHANGED;
import static model.ModelObservableEvents.SELECTED_TIMING_DURATION_DELAY_CHANGED;
import static model.ModelObservableEvents.LAST_SCHEDULED_TASK_CHANGED;

public abstract class StateModelTest {

    protected StateModel stateModel;
    protected Observer observer;

    @Test
    void changeSelectedScheduledTask_producesEventTest() {
        String expectedName = "Shutdown";
        stateModel.setSelectedTaskName(expectedName);

        String expectedLastEvent = SELECTED_TASKNAME_CHANGED.toString();

        Assertions.assertEquals(expectedLastEvent, observer.getLastEvent().getPropertyName());
        Assertions.assertEquals(expectedName, observer.getLastEvent().getNewValue());
    }

    @Test
    void changeSelectedScheduledTaskParameter_producesEventTest() {
        String expectedParameter = "Shutdown parameter1";
        stateModel.setSelectedTaskParameter(expectedParameter);

        String expectedLastEvent = SELECTED_TASKPARAMETER_CHANGED.toString();

        Assertions.assertEquals(expectedLastEvent, observer.getLastEvent().getPropertyName());
        Assertions.assertEquals(expectedParameter, observer.getLastEvent().getNewValue());
    }

    @Test
    void changeSelectedTiming_producesEventTest() {
        String expectedTiming = "01:30";
        stateModel.setTimingDurationDelay(expectedTiming);

        String expectedLastEvent = SELECTED_TIMING_DURATION_DELAY_CHANGED.toString();

        Assertions.assertEquals(expectedLastEvent, observer.getLastEvent().getPropertyName());
        Assertions.assertEquals(expectedTiming, observer.getLastEvent().getNewValue());
    }

    @Test
    void changeLastSelectedId_producesEventTest() {
        int lastId = 1001;
        stateModel.setLastScheduledTaskId(lastId);

        String expectedLastEvent = LAST_SCHEDULED_TASK_CHANGED.toString();

        Assertions.assertEquals(expectedLastEvent, observer.getLastEvent().getPropertyName());
        Assertions.assertEquals(lastId, observer.getLastEvent().getNewValue());
    }

}
