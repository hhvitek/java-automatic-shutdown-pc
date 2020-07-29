package view.main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tasks.TaskTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ChooseTaskUI {

    private final JPanel uiContainerPanel = new JPanel();

    private final ChooseTaskGroup taskGroup = new ChooseTaskGroup();

    public ChooseTaskUI() {
        uiContainerPanel.setLayout(
                new GridLayout(0, 1)
        );
    }

    public JPanel createUIChooseTask(@NotNull List<TaskTemplate> tasks) {
        for (TaskTemplate taskTemplate : tasks) {
            JPanel taskPanel = taskGroup.createUIForOneTask(taskTemplate);
            uiContainerPanel.add(taskPanel);
        }

        taskGroup.selectFirstRadioTask();
        return uiContainerPanel;
    }

    public void addActionListenerOnNewTaskSelected(@NotNull ActionListener listener) {
        taskGroup.addActionListenerOnNewTaskSelected(listener);
    }

    public void addActionListenerOnTaskParameterChanged(@NotNull ActionListener listener) {
        taskGroup.addActionListenerOnTaskParameterChanged(listener);
    }

    public String getSelectedTaskName() {
        return taskGroup.getSelectedTaskName();
    }

    public String getSelectedTaskParameter() {
        return taskGroup.getSelectedTaskParameter();
    }

    public void setSelectedTaskParameter(@Nullable String taskParameter) {
        taskGroup.setSelectedTaskParameter(taskParameter);
    }

    public void setSelectedTaskName(@NotNull String taskName) {
        taskGroup.selectTaskName(taskName);
    }
}
