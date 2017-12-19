package biz.africanbib.Models;

import android.graphics.Bitmap;

public class ChooseFileBuilder {
    private String title;
    private String xmlTag;
    private String columnName;
    private String tableName;
    private int rowno;
    private Bitmap imageFile;
    private String pdfFilePath;

    public ChooseFileBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ChooseFileBuilder setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
        return this;
    }

    public ChooseFileBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public ChooseFileBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public ChooseFileBuilder setRowno(int rowno) {
        this.rowno = rowno;
        return this;
    }

    public ChooseFileBuilder setImageFile(Bitmap imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public ChooseFileBuilder setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
        return this;
    }

    public ChooseFile createChooseFile() {
        return new ChooseFile(title, xmlTag, columnName, tableName, rowno, imageFile, pdfFilePath);
    }
}