package view;

import controller.AbstractController;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;

/**
 * View implements Observer pattern modelPropertyChange
 */
public abstract class AbstractView {

    protected AbstractController controller;

    protected AbstractView(@NotNull AbstractController controller) {
        this.controller = controller;
        controller.addView(this); //if done here it starts immediately receiving events even though the view is not fully instantiated by constructor.
    }

    public abstract void modelPropertyChange(final PropertyChangeEvent evt);
}
