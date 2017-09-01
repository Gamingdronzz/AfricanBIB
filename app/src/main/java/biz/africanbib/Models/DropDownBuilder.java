package biz.africanbib.Models;

public class DropDownBuilder {
    private String heading;
    private String[] list;
    private int selectedPosition;
    private String columnName;
    private String tableName;
    private int rowno;

    public DropDownBuilder setHeading(String heading) {
        this.heading = heading;
        return this;
    }

    public DropDownBuilder setList(String[] list) {
        this.list = list;
        return this;
    }

    public DropDownBuilder setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        return this;
    }

    public DropDownBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public DropDownBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public DropDownBuilder setRowno(int rowno) {
        this.rowno = rowno;
        return this;
    }

    public DropDown createDropDown() {
        return new DropDown(heading, list, selectedPosition, columnName, tableName, rowno);
    }
}