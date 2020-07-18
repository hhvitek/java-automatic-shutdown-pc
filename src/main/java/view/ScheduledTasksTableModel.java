package view;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class ScheduledTasksTableModel extends AbstractTableModel {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasksTableModel.class);

    private static final int ID_COLUMN_INDEX = 0;
    private static final int WHEN_ELAPSED_COLUMN_INDEX = 3;

    private static String[] columnNames = {
            "Id", "Name", "Status", "WhenElapse", "Result"
    };
    private final List<Object[]> data;

    public ScheduledTasksTableModel() {
        data = Collections.synchronizedList(new ArrayList<>());
    }

    public void addScheduledTaskRow(@NotNull Object[] rowValues) {
        if (rowValues.length != getColumnCount()) {
            logger.error("Expected column count: <{}>. Actual: <{}>. Ignoring", rowValues.length, getColumnCount());
        } else {
            data.add(rowValues);
            int dataSize = getRowCount() - 1;
            fireTableRowsInserted(dataSize, dataSize);
        }
    }

    public int getScheduledTaskIdByRow(int rowIndex) {
        return (int) getValueAt(rowIndex, ID_COLUMN_INDEX);
    }

    private int getRowById(int id) {
        for(int rowIndex =0; rowIndex<getRowCount(); rowIndex++) {
            if (id == (int)getValueAt(rowIndex, ID_COLUMN_INDEX)) {
                return rowIndex;
            }
        }
        return Integer.MIN_VALUE;
    }

    public void updateScheduledTaskRow(@NotNull Object[] newValues) {
        int id = (int) newValues[ID_COLUMN_INDEX];
        int rowIndex = getRowById(id);
        if (rowIndex >= 0) {
            updateValuesIfDifferent(rowIndex, newValues);
        }
    }

    private void updateValuesIfDifferent(int rowIndex, @NotNull Object[] updated) {
        if (updated.length == getColumnCount() && rowIndex >= 0 && rowIndex <= getRowCount()) {
            for(int i=0; i<getColumnCount(); i++) {
                if (data.get(rowIndex)[i] != updated[i]) {
                    setValueAt(updated[i], rowIndex, i);
                }
            }
        }
    }

    public void updateWhenElapsedForTask(int id, @NotNull String newWhenElapsed) {
        int rowIndex = getRowById(id);
        if(rowIndex >=0) {
            setValueAt(newWhenElapsed, rowIndex, WHEN_ELAPSED_COLUMN_INDEX);
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
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
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public int findColumn(@NotNull String columnName) {
        for(int i=0; i<columnNames.length; i++) {
            if (columnName.equalsIgnoreCase(columnNames[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
