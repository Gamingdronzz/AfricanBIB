package biz.africanbib.Models;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class ChooseFile {
    private String title;
    private String xmlTag;
    private String columnName;
    private String tableName;
    private int rowno = 0;

    public ChooseFile() {
    }

    public ChooseFile(String title, String columnName, String tableName, int rowno, String xmlTag) {
        this.title = title;
        this.columnName = columnName;
        this.tableName = tableName;
        this.rowno = rowno;
        this.xmlTag = xmlTag;
    }

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
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

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }
}
