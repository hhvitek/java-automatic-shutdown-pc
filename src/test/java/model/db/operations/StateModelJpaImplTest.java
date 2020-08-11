package model.db.operations;

import model.Observer;
import model.nodb.StateModelImpl;
import model.StateModelTest;

class StateModelJpaImplTest extends StateModelTest {

    StateModelJpaImplTest() {
        this.observer = new Observer();
        StateModelImpl stateModel = new StateModelImpl();
        stateModel.addPropertyChangeListener(observer);
        this.stateModel = stateModel;
    }

}
