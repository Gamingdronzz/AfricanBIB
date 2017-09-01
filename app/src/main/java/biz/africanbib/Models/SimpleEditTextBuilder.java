package biz.africanbib.Models;

public class SimpleEditTextBuilder {
    private String title;
    private String value;
    private int type = SimpleEditText.TEXT;
    private String columnName;
    private String tableName;
    private int rowno = 0;

    public SimpleEditTextBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleEditTextBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public SimpleEditTextBuilder setType(int type) {
        this.type = type;
        return this;
    }

    public SimpleEditTextBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public SimpleEditTextBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SimpleEditTextBuilder setRowno(int rowno) {
        this.rowno = rowno;
        return this;
    }

    public SimpleEditText createSimpleEditText() {
        return new SimpleEditText(title, value, type, columnName, tableName, rowno);
    }
}