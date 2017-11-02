package biz.africanbib.Models;

import android.graphics.Bitmap;

public class SimpleImageBuilder {
    private String title;
    private int rowno;
    private Bitmap image;
    private String columnName;
    private String tableName;
    private String xmlTag;

    public SimpleImageBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public SimpleImageBuilder setImage(Bitmap image) {
        this.image = image;
        return this;
    }

    public SimpleImageBuilder setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public SimpleImageBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SimpleImageBuilder setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
        return this;
    }

    public SimpleImageBuilder setRowNo(int rowNo) {
        this.rowno = rowNo;
        return this;
    }


    public SimpleImage createSimpleImage() {
        return new SimpleImage(title,rowno, image, columnName, tableName,xmlTag);
    }
}