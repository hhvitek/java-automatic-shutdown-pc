package view;

import org.jetbrains.annotations.NotNull;
import tasks.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ChooseTaskUI {

    private JPanel uiContainerPanel = new JPanel();

    private ChooseTasks chooseTasks = new ChooseTasks();

    public ChooseTaskUI() {
        uiContainerPanel.setLayout(
                new GridLayout(0, 1)
        );
    }

    public JPanel createUIChooseTask(@NotNull List<Task> tasks) {
        for (Task task: tasks) {
            JPanel taskPanel = chooseTasks.createUIForOneTask(task);
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
