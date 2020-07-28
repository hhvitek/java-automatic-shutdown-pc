package view;

import controller.AbstractController;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * View implements Observer pattern modelPropertyChange
 */
public abstract class AbstractWindow {

    protected final AbstractController controller;

    protected JFrame guiFrame;

    protected AbstractWindow(@NotNull AbstractController controller, @NotNull JFrame guiFrame) {
        this.controller = controller;
        this.guiFrame = guiFrame;
        controller.addView(this);
    }

    public abstract void modelPropertyChange(PropertyChangeEvent evt);

    public void run() {
        SwingViewUtils.runAndShowWindow(guiFrame);
    }

    protected void showErrorPopup(@NotNull String errorMessage) {
        JOptionPane.showMessageDialog(guiFrame, errorMessage, "Neočekávaná chyba.", JOptionPane.ERROR_MESSAGE);
    }
}
