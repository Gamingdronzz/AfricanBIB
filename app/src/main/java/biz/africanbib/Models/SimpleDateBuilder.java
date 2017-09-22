package biz.africanbib.Models;

import java.util.Date;

public class SimpleDateBuilder {
    private String title;
    private String value;
    private Date date;
    private String columnName;
    private String tableName;
    private int rowno;
    private String xmlTag;

    public SimpleDateBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleDateBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public SimpleDateBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public SimpleDateBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public SimpleDateBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SimpleDateBuilder setRowno(int rowno) {
        this.rowno = rowno;
        return this;
    }

    public SimpleDateBuilder setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
        return this;
    }

    public SimpleDate createSimpleDate() {
        return new SimpleDate(title, value, date, columnName, tableName, rowno,xmlTag);
    }
}