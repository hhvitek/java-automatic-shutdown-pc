package controller;

import model.AbstractModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.AbstractView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    protected List<AbstractView> registeredViews;
    protected List<AbstractModel> registeredModels;

    protected AbstractController() {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();
    }

    public void addModel(@NotNull AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(@NotNull AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void addView(@NotNull AbstractView view) {
        registeredViews.add(view);
    }

    public void removeView(@NotNull AbstractView view) {
        registeredViews.remove(view);
    }


    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.


    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractView view : registeredViews) {
            view.modelPropertyChange(evt);
        }
    }


    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue     = An object that represents the new value
     *                     of the property.
     */
    protected void setModelProperty(@NotNull String propertyName, @Nullable Object newValue) {

        for (AbstractModel model : registeredModels) {
            try {
                Method method = model.getClass().
                        getMethod("set" + propertyName, new Class[]{
                                        newValue.getClass()
                                }
                        );
                method.invoke(model, newValue);

            } catch (Exception ex) {
                //  Handle exception.
                logger.error("Failed to invoke model's method <{}> using reflection API. Method not found.", propertyName, ex);
            }
        }
    }
}
