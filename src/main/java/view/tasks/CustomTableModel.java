package view.tasks;

import model.TimeManager;
import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom JTable Model
 */
public class CustomTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 438291217388409669L;

    private static final Logger logger = LoggerFactory.getLogger(CustomTableModel.class);

    private static final int ID_COLUMN_INDEX = 0;
    private static final int STATUS_COLUMN_INDEX = 2;
    private static final int WHEN_ELAPSED_COLUMN_INDEX = 3;
    private static final int HIDDEN_TIME_MANAGER_COLUMN_INDEX = 5;


    private static final String[] COLUMN_NAMES = {
            "Id", "Name", "Status", "WhenElapse", "Result", "HiddenTimeManager"
    };
    private final List<Object[]> data;

    public CustomTableModel() {
        data = Collections.synchronizedList(new ArrayList<>());
    }

    public void addScheduledTaskRow(@NotNull Object[] rowValues) {
        if (rowValues.length != getColumnCount()) {
            logger.error("Expected column count: <{}>. Actual: <{}>. Ignoring", rowValues.length, getColumnCount());
        } else {
            int id = (int) rowValues[ID_COLUMN_INDEX];
            if (isIdAlreadyInTable(id)) {
                logger.error("Trying to insert a new row for already existing scheduled task id: <{}>! Ignoring.", id);
            } else {
                data.add(rowValues);
                int dataSize = getRowCount() - 1;
                fireTableRowsInserted(dataSize, dataSize);
            }
        }
    }

    public int getScheduledTaskIdByRow(int rowIndex) {
        return (int) getValueAt(rowIndex, ID_COLUMN_INDEX);
    }

    private boolean isIdAlreadyInTable(int id) {
        return getRowById(id) >= 0;
    }

    private int getRowById(int id) {
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
            if (id == (int) getValueAt(rowIndex, ID_COLUMN_INDEX)) {
                return rowIndex;
            }
        }
        return Integer.MIN_VALUE;
    }

    public void updateScheduledTaskRow(@NotNull Object[] newValues) {
        int id = (int) newValues[ID_COLUMN_INDEX];
        if (isIdAlreadyInTable(id)) {
            int rowIndex = getRowById(id);
            updateValuesIfDifferent(rowIndex, newValues);
        } else {
            logger.error("Trying to update task id: <{}> that is not in table yet. Ignoring", id);
        }
    }

    private void updateValuesIfDifferent(int rowIndex, @NotNull Object[] updated) {
        if (updated.length == getColumnCount() && rowIndex >= 0 && rowIndex <= getRowCount()) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (data.get(rowIndex)[i] != updated[i]) {
                    setValueAt(updated[i], rowIndex, i);
                }
            }
        }
    }

    public void updateWhenElapsedForTask(int id, @NotNull String newWhenElapsed) {
        if (isIdAlreadyInTable(id)) {
            int rowIndex = getRowById(id);
            setValueAt(newWhenElapsed, rowIndex, WHEN_ELAPSED_COLUMN_INDEX);
        } else {
            logger.error("Trying to update task id: <{}> that is not in table yet. Ignoring", id);
        }
    }

    public void refreshTimingCountDowns() {
        synchronized (data) {
            data.forEach(
                this::refreshTimingIfScheduledStatus
            );
        }
    }

    private void refreshTimingIfScheduledStatus(@NotNull Object[] row) {
        ScheduledTaskStatus status = (ScheduledTaskStatus) row[STATUS_COLUMN_INDEX];
        if (status == ScheduledTaskStatus.SCHEDULED) {
            refreshTiming(row);
        }
    }

    private void refreshTiming(@NotNull Object[] row) {
        int scheduledTaskId = (Integer) row[ID_COLUMN_INDEX];
        TimeManager timeManager = (TimeManager) row[HIDDEN_TIME_MANAGER_COLUMN_INDEX];
        String newWhenElapse = timeManager.getRemainingDurationInHHMMSS_ifElapsedZeros();
        setValueAt(newWhenElapse, getRowById(scheduledTaskId), WHEN_ELAPSED_COLUMN_INDEX);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public int findColumn(@NotNull String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(COLUMN_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
