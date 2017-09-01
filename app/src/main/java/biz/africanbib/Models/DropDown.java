package biz.africanbib.Models;

import android.util.Log;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class DropDown
{
    private String heading;
    private String[] list;
    private int selectedPosition = 0;

    private String columnName;
    private String tableName;

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    private int rowno = -1;

    public DropDown(String heading, String[] list, int selectedPosition, String columnName, String tableName, int rowno) {
        this.heading = heading;
        this.list = list;
        this.selectedPosition = selectedPosition;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        Log.v("DropDown","Selected position = " + selectedPosition);
        this.selectedPosition = selectedPosition;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public DropDown(String heading,String[] list) {

        this.heading = heading;
        this.list = list;
    }
}
