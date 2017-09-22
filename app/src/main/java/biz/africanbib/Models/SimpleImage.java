package biz.africanbib.Models;

import android.graphics.Bitmap;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class SimpleImage
{
    private String title;
    private Bitmap image;
    private String columnName;
    private String tableName;
    private String xmlTag;

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public SimpleImage(String title, Bitmap image, String columnName, String tableName,String xmlTag) {
        this.title = title;
        this.image = image;
        this.columnName = columnName;
        this.tableName = tableName;
        this.xmlTag = xmlTag;
    }
}
