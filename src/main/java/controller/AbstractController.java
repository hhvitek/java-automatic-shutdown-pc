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

public abstract class AbstractController implements PropertyChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    protected final List<AbstractView> registeredViews;
    protected final List<AbstractObservableModel> registeredModels;

    protected AbstractController() {
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

    /**
     * This is a convenience method that subclasses can call to invoke model's method by it's string representation.
     * This method uses utilities.reflection to inspect each of the model classes to determine whether it is the owner of
     * the method in question. If it isn't, a NoSuchMethodException is thrown, which the method ignores.
     *
     * @param methodName      = The name of the method.
     * @param methodParameter = An object that represents is passed as a method parameter
     */
    protected void invokeModelMethodWithOneParameter(@NotNull String methodName, @NotNull Object methodParameter) {
        for (AbstractObservableModel model : registeredModels) {
            ReflectionApi.invokeModelMethodWithOneParameter(model, methodName, methodParameter);
        }
    }

    /**
     * This is a convenience method that subclasses can call to invoke model's method by it's string representation.
     * This method uses utilities.reflection to inspect each of the model classes to determine whether it is the owner of
     * the method in question. If it isn't, a NoSuchMethodException is thrown, which the method ignores.
     *
     * @param methodName      = The name of the method.
     */
    protected void invokeModelMethodWithoutParameters(@NotNull String methodName) {
        for (AbstractObservableModel model : registeredModels) {
            ReflectionApi.invokeModelMethodWithoutParameters(model, methodName);
        }
    }

    /**
     * This is a convenience method that subclasses can call to invoke model's method by it's string representation.
     * This method uses utilities.reflection to inspect each of the model classes to determine whether it is the owner of
     * the method in question. If it isn't, a NoSuchMethodException is thrown, which the method ignores.
     *
     * @param methodName      = The name of the method.
     * @param methodParameters = An object that represents is passed as a method parameter
     */
    protected void invokeModelMethodWitMultipleParameters(@NotNull String methodName, @NotNull Object[] methodParameters) {
        for (AbstractObservableModel model : registeredModels) {
            ReflectionApi.invokeModelMethodWitMultipleParameters(model, methodName, methodParameters);
        }
    }


}
