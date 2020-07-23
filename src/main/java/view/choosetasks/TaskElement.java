package view.choosetasks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Represents UI's Radio elements to choose task
 */
public class TaskElement {

    private final JRadioButton radioButtonTaskName;
    private final JTextField textFieldTaskParameter;

    public TaskElement(@NotNull JRadioButton radioButtonTaskName, @Nullable JTextField textFieldTaskParameter) {
        this.radioButtonTaskName = radioButtonTaskName;
        this.textFieldTaskParameter = textFieldTaskParameter;
    }


    public String getTaskParameter() {
        if (textFieldTaskParameter != null) {
            return textFieldTaskParameter.getText();
        } else {
            return "";
        }
    }

    public void addActionListener(@NotNull ActionListener listener) {
        radioButtonTaskName.addActionListener(listener);
    }

    public void setSelected() {
        radioButtonTaskName.setSelected(true);
    }
}
