package biz.africanbib.Models;

import android.graphics.Bitmap;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class ChooseFile {
    private String title;
    private String xmlTag;
    private String columnName;
    private String tableName;
    private int rowno = 0;

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public Bitmap getImageFile() {

        return imageFile;
    }

    public void setImageFile(Bitmap imageFile) {
        this.imageFile = imageFile;
    }

    private Bitmap imageFile;
    private String pdfFilePath;

    public ChooseFile(String title, String xmlTag, String columnName, String tableName, int rowno, Bitmap imageFile, String pdfFilePath) {
        this.title = title;
        this.xmlTag = xmlTag;
        this.columnName = columnName;
        this.tableName = tableName;
        this.rowno = rowno;
        this.imageFile = imageFile;
        this.pdfFilePath = pdfFilePath;
    }

    public ChooseFile() {
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
