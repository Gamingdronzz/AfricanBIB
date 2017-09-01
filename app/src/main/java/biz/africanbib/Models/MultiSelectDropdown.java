package biz.africanbib.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class MultiSelectDropdown {
    String title;
    String[] items;
    List<Integer> selectedIndices;

    private String columnName;
    private String tableName;

    public List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public void setSelectedIndices(List<Integer> selectedIndices) {
        this.selectedIndices = selectedIndices;
    }

    private int rowno = 1;

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public MultiSelectDropdown(String title, String[] items, List<Integer> selectedIndices, String columnName, String tableName, int rowno) {
        this.title = title;
        this.items = items;
        if (selectedIndices == null) {
            this.selectedIndices = new ArrayList<>();
        } else {
            this.selectedIndices = selectedIndices;
        }
        this.columnName = columnName;
        this.tableName = tableName;
        this.rowno = rowno;
    }

    public String getColumnName() {

        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
