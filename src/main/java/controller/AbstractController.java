package controller;

import model.AbstractObservableModel;
import org.jetbrains.annotations.NotNull;
import utilities.reflection.ReflectionApi;
import view.AbstractWindow;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a listener for any events in Models. Propagate those events to Views.
 */
public abstract class AbstractController implements PropertyChangeListener {

    private final ReflectionApi reflectionApi;

    private final List<AbstractWindow> registeredViews;
    private final List<AbstractObservableModel> registeredModels;

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

    public void addView(@NotNull AbstractWindow view) {
        registeredViews.add(view);
    }

    public void removeView(@NotNull AbstractWindow view) {
        registeredViews.remove(view);
    }

    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        for (AbstractWindow view : registeredViews) {
            view.modelPropertyChange(propertyChangeEvent);
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
        for (AbstractWindow view : registeredViews) {
            reflectionApi.invokeMethodWithParameters(view, methodName, optionalMethodParameters);
        }
    }
}
