package biz.africanbib.Models;

/**
 * Created by Balpreet on 01-Aug-17.
 */

public class Add {
    private int rows;
    private int type = -1;
    private String[] xmlTags;

    public String[] getXmlTags() {
        return xmlTags;
    }

    public void setXmlTags(String[] xmlTags) {
        this.xmlTags = xmlTags;
    }

    public static int INDUSTRY = 1;
    private String[] columnNames = null;

    private String[] tableColumnNames = null;

    public String[] getColumnNames() {
        return tableColumnNames;
    }

    public void setTableColumnNames(String[] tableColumnNames) {
        this.tableColumnNames = tableColumnNames;
    }

    private String tableName;

    public Add(int rows, int type, String[] columnNames, String[] tableColumnNames, String tableName,String[] xmlTags) {
        this.rows = rows;
        this.type = type;
        this.columnNames = columnNames;
        this.tableColumnNames = tableColumnNames;
        this.tableName = tableName;
        this.xmlTags = xmlTags;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }



    public Add(int rows, int type) {
        this.rows = rows;
        this.type = type;
    }

    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public Add(int rows, String[] columnNames) {
        this.rows = rows;
        this.columnNames = columnNames;
    }

    public String[] getTitles() {

        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Add(int rows) {

        this.rows = rows;
    }
}
