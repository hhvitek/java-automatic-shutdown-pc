package controller;

import model.AbstractObservableModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.reflection.ReflectionApi;
import view.AbstractView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * It's listener for any events in Models. Propagate those events to Views.
 */
public abstract class AbstractController implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);
    private final ReflectionApi reflectionApi;

    protected final List<AbstractView> registeredViews;
    protected final List<AbstractObservableModel> registeredModels;

    protected AbstractController() {
        reflectionApi = new ReflectionApi(false, false);

        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();
    }

    public void addModel(@NotNull AbstractObservableModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(@NotNull AbstractObservableModel model) {
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

    protected void modelInvokeMethodWithoutParameters(@NotNull String methodName) {
        for (AbstractObservableModel model : registeredModels) {
            reflectionApi.invokeMethodWithoutParameters(model, methodName);
        }
    }

    protected void modelInvokeMethodWithParameters(@NotNull String methodName, Object... methodParameters) {
        for (AbstractObservableModel model : registeredModels) {
            reflectionApi.invokeMethodWithParameters(model, methodName, methodParameters);
        }
    }

    protected void viewInvokeMethod(@NotNull String methodName, Object... optionalMethodParameters) {
        for (AbstractView view: registeredViews) {
            reflectionApi.invokeMethodWithParameters(view, methodName, optionalMethodParameters);
        }
    }


}
