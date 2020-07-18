package view;

import model.TaskTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ChooseTaskUI {

    private final JPanel uiContainerPanel = new JPanel();

    private final ChooseTasks chooseTasks = new ChooseTasks();

    public ChooseTaskUI() {
        uiContainerPanel.setLayout(
                new GridLayout(0, 1)
        );
    }

    public JPanel createUIChooseTask(@NotNull List<TaskTemplate> tasks) {
        for (TaskTemplate taskTemplate: tasks) {
            JPanel taskPanel = chooseTasks.createUIForOneTask(taskTemplate);
            uiContainerPanel.add(taskPanel);
        }

        chooseTasks.selectFirstRadioTask();
        return uiContainerPanel;
    }

    public void addActionListener(@NotNull ActionListener listener) {
        chooseTasks.addActionListener(listener);
    }

    public String getSelectedTaskName() {
        return chooseTasks.getSelectedTaskName();
    }

    public String getSelectedTaskParameter() {
        return chooseTasks.getSelectedTaskParameter();
    }
}
