package view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ChooseTaskElement {

    private final JRadioButton radioButtonTaskName;
    private final JTextField textFieldTaskParameter;

    public ChooseTaskElement(@NotNull JRadioButton radioButtonTaskName, @Nullable JTextField textFieldTaskParameter) {
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
}
