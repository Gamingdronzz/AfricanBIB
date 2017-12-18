package biz.africanbib.Models;

/**
 * Created by hp on 18-12-2017.
 */

public class ChooseFileBuilder {
    private String title;
    private String xmlTag;
    private String columnName;
    private String tableName;
    private int rowno = 0;

    public ChooseFileBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ChooseFileBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public ChooseFileBuilder setTabeName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public ChooseFileBuilder setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
        return this;
    }

    public ChooseFileBuilder setRowNo(int rowNo) {
        this.rowno = rowNo;
        return this;
    }

    public ChooseFile createChooseFile() {
        return new ChooseFile(title, columnName, tableName, rowno, xmlTag);
    }
}
