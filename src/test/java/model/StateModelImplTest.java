package model;

import model.nodb.StateModelImpl;

class StateModelImplTest extends StateModelTest {

    StateModelImplTest() {
        this.observer = new Observer();
        StateModelImpl stateModel = new StateModelImpl();
        stateModel.addPropertyChangeListener(observer);
        this.stateModel = stateModel;
    }

}
