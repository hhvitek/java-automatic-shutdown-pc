package view;

import controller.AbstractController;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;

public abstract class AbstractView {

    protected AbstractController controller;

    protected AbstractView(@NotNull AbstractController controller) {
        this.controller = controller;
        controller.addView(this);
    }

    public abstract void modelPropertyChange(final PropertyChangeEvent evt);
}
