package bindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class DataObjectBase implements Serializable {

    private List<PropertyChangedListener> propertyChangeListeners = new ArrayList<>();

    public void addOnPropertyChangedListener(PropertyChangedListener propertyChangeListener) {
        ensureRegisterListCreated();
        propertyChangeListeners.add(propertyChangeListener);
    }

    public void removeOnPropertyChangedListener(PropertyChangedListener propertyChangeListener) {
        ensureRegisterListCreated();
        propertyChangeListeners.remove(propertyChangeListener);
    }

    public DataObjectBase() {
        ensureRegisterListCreated();
    }

    public void ensureRegisterListCreated() {
        if (propertyChangeListeners == null)
            propertyChangeListeners = new ArrayList<>();
    }

    protected void onPropertyChanged(Object source, String propertyName, Object oldValue, Object newValue) {
        if (propertyChangeListeners.size() <= 0)
            return;
        for (PropertyChangedListener listener : propertyChangeListeners) {
            listener.onPropertyChanged(source, propertyName, oldValue, newValue);
        }
    }

}
