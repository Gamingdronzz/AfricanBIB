package biz.africanbib.Models;

import android.util.Log;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class DropDown {
    private String heading;
    private String xmlTag;
    private String[] list;
    private int[] code;
    private int selectedPosition = 0;
    private int rowno = -1;
    private String columnName;
    private String tableName;

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }

    public DropDown(String heading, String[] list, int[] code,int selectedPosition, String columnName, String tableName, int rowno, String xmlTag) {
        this.heading = heading;
        this.list = list;
        this.code = code;
        this.selectedPosition = selectedPosition;
        this.columnName = columnName;
        this.tableName = tableName;
        this.rowno = rowno;
        this.xmlTag = xmlTag;
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
        Log.v("DropDown", "Selected position = " + selectedPosition);
        this.selectedPosition = selectedPosition;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public DropDown(String heading, String[] list) {

        this.heading = heading;
        this.list = list;
    }
}
