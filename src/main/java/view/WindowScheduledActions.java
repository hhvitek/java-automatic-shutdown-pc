package view;

import model.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WindowScheduledActions {

    private final JTable table;
    private final ScheduledTasksTableModel tableModel;

    public WindowScheduledActions(@NotNull JTable table) {
        this.table = table;

        tableModel = new ScheduledTasksTableModel();
        table.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        centerRenderer.setHorizontalTextPosition(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(String.class, centerRenderer);

        setColumnPreferredWidth("Id", 50);
        setColumnPreferredWidth("Name", 100);
        setColumnPreferredWidth("Status", 100);
        setColumnPreferredWidth("WhenElapse", 100);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void setColumnPreferredWidth(@NotNull String columnName, int width) {
        TableColumn column = table.getColumn(columnName);
        column.setPreferredWidth(width);
        column.setMinWidth(width - 10);
        column.setMaxWidth(width + 10);
    }

    public void createUIForOneScheduledTask(@NotNull ScheduledTask scheduledTask) {
        Object[] cols = processScheduledTaskIntoUIObjects(scheduledTask);
        tableModel.addScheduledTaskRow(cols);
    }

    private Object[] processScheduledTaskIntoUIObjects(@NotNull ScheduledTask scheduledTask) {
        String output = scheduledTask.getOutput();
        String errorMessage = scheduledTask.getErrorMessage();
        if (!errorMessage.isBlank()) {
            output = errorMessage;
        }

        Object[] cols = {
                scheduledTask.getId(),
                scheduledTask.getTaskTemplate().getName(),
                scheduledTask.getStatus(),
                scheduledTask.getWhenElapsed().getRemainingDurationInHHMMSS_ifElapsedZeros(),
                output
        };
        return cols;
    }

    public void updateUIForOneScheduledTask(@NotNull ScheduledTask scheduledTask) {
        Object[] cols = processScheduledTaskIntoUIObjects(scheduledTask);
        tableModel.updateScheduledTaskRow(cols);
    }

    public void updateUIWhenElapsedForTasks(@NotNull List<ScheduledTask> scheduledTasks) {
        scheduledTasks.forEach(
                scheduledTask -> tableModel.updateWhenElapsedForTask(
                        scheduledTask.getId(),
                        scheduledTask.getWhenElapsed().getRemainingDurationInHHMMSS_ifElapsedZeros()
                )
        );
    }

    public @NotNull List<Integer> getSelectedTaskIds() {
        return Arrays.stream(table.getSelectedRows())
                .map(table::convertRowIndexToModel) // selected view indexes into model indexes
                .mapToObj(selectedRow -> tableModel.getScheduledTaskIdByRow(selectedRow)) // model row indexes into scheduled task ids
                .collect(Collectors.toUnmodifiableList());
    }
}
