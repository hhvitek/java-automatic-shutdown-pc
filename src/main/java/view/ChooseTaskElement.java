package view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ChooseTaskElement {

    private String taskName;
    private JRadioButton radioButtonTaskName;
    private JTextField textFieldTaskParameter;

    public ChooseTaskElement(@NotNull String taskName, @NotNull JRadioButton radioButtonTaskName) {
        this.taskName = taskName;
        this.radioButtonTaskName = radioButtonTaskName;
    }

    public ChooseTaskElement(@NotNull String taskName, @NotNull JRadioButton radioButtonTaskName, @Nullable JTextField textFieldTaskParameter) {
        this.taskName = taskName;
        this.radioButtonTaskName = radioButtonTaskName;
        this.textFieldTaskParameter = textFieldTaskParameter;
    }

    public void setTextFieldTaskParameter(@NotNull JTextField textFieldTaskParameter) {
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
}
