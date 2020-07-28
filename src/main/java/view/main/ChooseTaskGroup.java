package view.main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.TaskTemplate;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents whole RadioGroup to choose tasks from.
 */
public class ChooseTaskGroup {

    private final ButtonGroup buttonGroupChooseTask = new ButtonGroup();

    // maps unique task name to radio element
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

    private @NotNull JRadioButton createTaskRadioButton(@NotNull JPanel taskPanel, @NotNull TaskTemplate taskTemplate) {
        String taskName = taskTemplate.getName();

        JRadioButton radioButtonTask = new JRadioButton(taskName);
        radioButtonTask.setActionCommand(taskName);
        radioButtonTask.setToolTipText(taskTemplate.getDescription());

        taskPanel.add(radioButtonTask);
        buttonGroupChooseTask.add(radioButtonTask);
        return radioButtonTask;
    }

    private @Nullable JTextField createTaskParameterFieldIfTaskAcceptsParameter(@NotNull JPanel taskPanel, @NotNull TaskTemplate taskTemplate) {
        if (taskTemplate.canAcceptParameter()) {
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

    public void selectTaskName(@NotNull String taskName) {
        if (taskMap.containsKey(taskName)) {
            ChooseTaskElement element = taskMap.get(taskName);
            element.setSelected();
        }
    }

    public @Nullable String getSelectedTaskName() {
        return buttonGroupChooseTask.getSelection().getActionCommand();
    }

    public @Nullable String getSelectedTaskParameter() {
        String selectedTaskName = getSelectedTaskName();
        if (selectedTaskName != null) {
            ChooseTaskElement element = taskMap.get(selectedTaskName);
            return element.getTaskParameter();
        }
        return null;
    }

    public void addActionListener(@NotNull ActionListener listener) {
        taskMap.values().forEach(
                taskElement -> taskElement.addActionListener(listener)
        );
    }
}
