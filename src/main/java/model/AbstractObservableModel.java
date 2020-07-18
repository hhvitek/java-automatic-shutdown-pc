package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Observer pattern
 */
public abstract class AbstractObservableModel {
    protected final PropertyChangeSupport propertyChangeSupport;

    protected AbstractObservableModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(@NotNull ModelObservableEvents propertyName, @Nullable Object oldValue, @Nullable Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName.toString(), oldValue, newValue);
    }


}
