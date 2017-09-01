package biz.africanbib.Models;

import java.util.Date;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class SimpleDate
{
    private String title;
    private String value;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String columnName;
    private String tableName;
    private int rowno = 0;

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }


    public SimpleDate(String title, String value, Date date, String columnName, String tableName, int rowno) {
        this.title = title;
        this.value = value;
        this.date = date;
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

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
