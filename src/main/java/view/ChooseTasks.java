package view;

import model.TaskTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ChooseTasks {

    private final ButtonGroup buttonGroupChooseTask = new ButtonGroup();

    private final Map<String, ChooseTaskElement> taskMap = new HashMap<>();

    public JPanel createUIForOneTask(@NotNull TaskTemplate taskTemplate) {
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(
                new BoxLayout(taskPanel, BoxLayout.LINE_AXIS)
        );

        JRadioButton radioButton = createTaskRadioButton(taskPanel, taskTemplate);
        JTextField textField = createTaskParameterFieldIfTaskAcceptsParameter(taskPanel, taskTemplate);

        ChooseTaskElement element = new ChooseTaskElement(radioButton, textField);
        taskMap.put(taskTemplate.getName(), element);

        return taskPanel;
    }

    private JRadioButton createTaskRadioButton(@NotNull JPanel taskPanel, @NotNull TaskTemplate taskTemplate) {
        String taskName = taskTemplate.getName();

        JRadioButton radioButtonTask = new JRadioButton(taskName);
        radioButtonTask.setActionCommand(taskName);
        radioButtonTask.setToolTipText(taskTemplate.getDescription());

        taskPanel.add(radioButtonTask);
        buttonGroupChooseTask.add(radioButtonTask);
        return radioButtonTask;
    }

    private JTextField createTaskParameterFieldIfTaskAcceptsParameter(@NotNull JPanel taskPanel, @NotNull TaskTemplate taskTemplate) {
        if (taskTemplate.acceptParameter()) {
            JTextField textFieldParameter = new JTextField();
            taskPanel.add(textFieldParameter);
            return textFieldParameter;
        }
        return null;
    }

    public void selectFirstRadioTask() {
        Enumeration<AbstractButton> buttonEnumeration = buttonGroupChooseTask.getElements();
        if (buttonEnumeration.hasMoreElements()) {
            AbstractButton button = buttonEnumeration.nextElement();
            button.setSelected(true);
        }
    }

    public String getSelectedTaskName() {
        return buttonGroupChooseTask.getSelection().getActionCommand();
    }

    public String getSelectedTaskParameter() {
        String selectedTaskName = getSelectedTaskName();
        ChooseTaskElement element = taskMap.get(selectedTaskName);
        return element.getTaskParameter();
    }

    public void addActionListener(@NotNull ActionListener listener) {
        taskMap.values().forEach(
                chooseTaskElement -> chooseTaskElement.addActionListener(listener)
        );
    }
}
