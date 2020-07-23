package view.choosetasks;

import tasks.TaskTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class TaskUI {

    private final JPanel uiContainerPanel = new JPanel();

    private final TaskGroup taskGroup = new TaskGroup();

    public TaskUI() {
        uiContainerPanel.setLayout(
                new GridLayout(0, 1)
        );
    }

    public JPanel createUIChooseTask(@NotNull List<TaskTemplate> tasks) {
        for (TaskTemplate taskTemplate: tasks) {
            JPanel taskPanel = taskGroup.createUIForOneTask(taskTemplate);
            uiContainerPanel.add(taskPanel);
        }

        taskGroup.selectFirstRadioTask();
        return uiContainerPanel;
    }

    public void addActionListener(@NotNull ActionListener listener) {
        taskGroup.addActionListener(listener);
    }

    public String getSelectedTaskName() {
        return taskGroup.getSelectedTaskName();
    }

    public String getSelectedTaskParameter() {
        return taskGroup.getSelectedTaskParameter();
    }

    public void setSelectedTaskName(@NotNull String taskName) {
        taskGroup.selectTaskName(taskName);
    }
}
