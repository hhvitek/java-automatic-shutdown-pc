package model.db.operations;

import model.AbstractObservableModel;
import model.StateModel;
import model.db.repo.StateModelEntity;
import model.db.repo.StateModelRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static model.ModelObservableEvents.*;

public class StateModelJpaImpl extends AbstractObservableModel implements StateModel {

    private static final String DEFAULT_TIMING_DURATION_DELAY = "01:00";

    private static final Logger logger = LoggerFactory.getLogger(StateModelJpaImpl.class);

    private final StateModelRepository stateModelRepository;
    private StateModelEntity stateModelEntity;

    public StateModelJpaImpl(@NotNull EntityManager entityManager) {
        this(entityManager, DEFAULT_TIMING_DURATION_DELAY, "");
    }

    public StateModelJpaImpl(@NotNull EntityManager entityManager, @NotNull String defaultDurationDelay, @NotNull String defaultSelectedTaskName) {
        stateModelRepository = new StateModelRepository(entityManager);

        restoreInitialState(defaultDurationDelay, defaultSelectedTaskName);
    }

    private void restoreInitialState(@NotNull String defaultDurationDelay, @NotNull String defaultSelectedTaskName) {
        logger.debug("Restoring initial StateModel state.");
        List<StateModelEntity> entities = stateModelRepository.findAll();
        if (entities.isEmpty()) {
            stateModelEntity = new StateModelEntity(defaultDurationDelay, defaultSelectedTaskName);
            stateModelRepository.create(stateModelEntity);
        } else {
            stateModelEntity = entities.get(0);
            stateModelRepository.update(stateModelEntity);
        }
    }

    @Override
    public void setSelectedTaskName(@NotNull String name) {
        String oldSelectedTaskName = stateModelEntity.getSelectedTaskName();
        stateModelEntity.setSelectedTaskName(name);

        stateModelRepository.update(stateModelEntity);

        logger.debug("The new selected task: <{}> -> <{}>", oldSelectedTaskName, name);

        firePropertyChange(SELECTED_TASK_CHANGED, oldSelectedTaskName, name);
    }

    @Override
    public @NotNull String getSelectedTaskName() {
        return stateModelEntity.getSelectedTaskName();
    }

    @Override
    public void setTimingDurationDelay(@NotNull String durationDelay) {
        String oldTimingDurationDelay = stateModelEntity.getTimingDurationDelay();
        stateModelEntity.setTimingDurationDelay(durationDelay);

        stateModelRepository.update(stateModelEntity);

        logger.debug("The new timingDurationDelay: <{}> -> <{}>", oldTimingDurationDelay, durationDelay);

        firePropertyChange(CHOSEN_TIMING_DURATION_DELAY_CHANGED, oldTimingDurationDelay, durationDelay);
    }

    @Override
    public @NotNull String getTimingDurationDelay() {
        return stateModelEntity.getTimingDurationDelay();
    }

    @Override
    public void setLastScheduledTaskId(int id) {
        int oldValue = stateModelEntity.getLastScheduledTaskId();
        stateModelEntity.setLastScheduledTaskId(id);

        stateModelRepository.update(stateModelEntity);

        firePropertyChange(LAST_SCHEDULED_TASK_CHANGED, oldValue, id);
    }

    @Override
    public int getLastScheduledTaskId() {
        return stateModelEntity.getLastScheduledTaskId();
    }
}
