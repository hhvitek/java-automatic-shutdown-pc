package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Observer implements PropertyChangeListener {

    public PropertyChangeEvent event;

    public PropertyChangeEvent getLastEvent() {
        return event;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        this.event = propertyChangeEvent;
    }
}
