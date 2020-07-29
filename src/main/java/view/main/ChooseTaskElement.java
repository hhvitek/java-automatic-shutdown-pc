package view.main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents UI's Radio elements to choose task
 */
public class ChooseTaskElement {

    private final JRadioButton radioButtonTaskName;
    private final JTextField textFieldTaskParameter;

    public ChooseTaskElement(@NotNull JRadioButton radioButtonTaskName, @Nullable JTextField textFieldTaskParameter) {
        this.radioButtonTaskName = radioButtonTaskName;
        this.textFieldTaskParameter = textFieldTaskParameter;

        if (textFieldTaskParameter != null) {
            textFieldTaskParameter.setToolTipText("Potvrďte stiskem tlačítka Enter");
            //textFieldTaskParameter.setPreferredSize(new Dimension(150, -1));
        }
    }


    public String getTaskParameter() {
        if (textFieldTaskParameter != null) {
            return textFieldTaskParameter.getText();
        } else {
            return "";
        }
    }

    public void addActionListenerOnNewTaskSelected(@NotNull ActionListener listener) {
        radioButtonTaskName.addActionListener(listener);
        if (textFieldTaskParameter != null) {
            textFieldTaskParameter.addActionListener(listener);
        }
    }

    public void addActionListenerOnTaskParameterChanged(@NotNull ActionListener listener) {
        if (textFieldTaskParameter != null) {
            textFieldTaskParameter.addActionListener(listener);
        }
    }

    public void setSelected() {
        radioButtonTaskName.setSelected(true);
    }

    public void setParameter(@Nullable String parameter) {
        if (textFieldTaskParameter != null) {
            textFieldTaskParameter.setText(parameter);
        }
    }


}
