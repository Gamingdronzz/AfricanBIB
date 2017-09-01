package biz.africanbib.Models;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class SimpleEditText
{
    private String title;
    private String value;
    private int type = TEXT;

    private String columnName;
    private String tableName;
    public static final int TEXT = 1;
    public static final int NUMBER = 2;
    public static final int DATE = 3;
    public static final int EMAIL = 4;

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    private int rowno = 0;

    public SimpleEditText(String title, String value, int type, String columnName, String tableName, int rowno) {
        this.title = title;
        this.value = value;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
