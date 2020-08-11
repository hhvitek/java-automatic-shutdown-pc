package view.tasks;

import model.scheduledtasks.ScheduledTaskMessenger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom JTable
 */
public class CustomTableController {

    private static final Logger logger = LoggerFactory.getLogger(CustomTableController.class);

    private final JTable table;
    private final CustomTableModel tableModel;

    public CustomTableController(@NotNull JTable table, @NotNull List<ScheduledTaskMessenger> alreadyExistingScheduledTasks) {
        this.table = table;

        tableModel = new CustomTableModel();
        initializeWithInitialScheduledTasks(alreadyExistingScheduledTasks);
        table.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setHorizontalTextPosition(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(String.class, centerRenderer);

        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        setColumnPreferredWidth("Id", 50);
        setColumnPreferredWidth("Name", 100);
        setColumnPreferredWidth("Status", 200);
        setColumnPreferredWidth("WhenElapse", 100);

        // hide TimeManager column
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.removeColumn(columnModel.getColumn(columnModel.getColumnCount() - 1));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void setColumnPreferredWidth(@NotNull String columnName, int width) {
        TableColumn column = table.getColumn(columnName);
        column.setPreferredWidth(width);
        column.setMinWidth(width - 10);
        column.setMaxWidth(width + 10);
    }

    private void initializeWithInitialScheduledTasks(@NotNull List<ScheduledTaskMessenger> alreadyExistingScheduledTasks) {
        alreadyExistingScheduledTasks.forEach(
                this::createUIForOneScheduledTask
        );
    }

    public void createUIForOneScheduledTask(@NotNull ScheduledTaskMessenger scheduledTask) {
        Object[] cols = processScheduledTaskIntoUIObjects(scheduledTask);
        tableModel.addScheduledTaskRow(cols);
    }

    private Object[] processScheduledTaskIntoUIObjects(@NotNull ScheduledTaskMessenger scheduledTask) {
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
                output,
                scheduledTask.getWhenElapsed()
        };
        return cols;
    }

    public void updateUIForOneScheduledTask(@NotNull ScheduledTaskMessenger scheduledTask) {
        Object[] cols = processScheduledTaskIntoUIObjects(scheduledTask);
        tableModel.updateScheduledTaskRow(cols);
    }

    public void updateUIWhenElapsedForTasks(@NotNull List<ScheduledTaskMessenger> scheduledTasks) {
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
                .mapToObj(tableModel::getScheduledTaskIdByRow) // model row indexes into scheduled task ids
                .collect(Collectors.toUnmodifiableList());
    }

    public void refreshTimingCountDowns() {
        tableModel.refreshTimingCountDowns();
    }
}
