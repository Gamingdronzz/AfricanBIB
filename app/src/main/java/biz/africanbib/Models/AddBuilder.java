package biz.africanbib.Models;

public class AddBuilder {
    private int rows;
    private int type;
    private String[] columnNames;
    private String[] tableColumnNames;
    private String tableName;

    public AddBuilder setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public AddBuilder setType(int type) {
        this.type = type;
        return this;
    }

    public AddBuilder setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    public AddBuilder setTableColumnNames(String[] tableColumnNames) {
        this.tableColumnNames = tableColumnNames;
        return this;
    }

    public AddBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Add createAdd() {
        return new Add(rows, type, columnNames, tableColumnNames, tableName);
    }
}