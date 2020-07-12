package bindings;

import java.util.EventListener;

public interface PropertyChangedListener extends EventListener {

    void onPropertyChanged(Object source, String propertyName, Object oldValue, Object newValue);
}
