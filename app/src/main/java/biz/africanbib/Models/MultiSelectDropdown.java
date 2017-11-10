package biz.africanbib.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class MultiSelectDropdown {
    String title;
    String[] items;
    int[] itemUid;
    List<Integer> selectedIndices;
    private String columnName;
    private String tableName;
    private String xmlTag;
    private int rowno = 1;


    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public void setSelectedIndices(List<Integer> selectedIndices) {
        this.selectedIndices = selectedIndices;
    }

    public int[] getItemUid() {
        return itemUid;
    }

    public void setItemUid(int[] itemUid) {
        this.itemUid = itemUid;
    }


    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public MultiSelectDropdown(String title, String[] items, int[] uid, List<Integer> selectedIndices, String columnName, String tableName, int rowno, String xmlTag) {
        this.title = title;
        this.items = items;
        this.itemUid = uid;
        if (selectedIndices == null) {
            this.selectedIndices = new ArrayList<>();
        } else {
            this.selectedIndices = selectedIndices;
        }
        this.columnName = columnName;
        this.tableName = tableName;
        this.rowno = rowno;
        this.xmlTag = xmlTag;
    }

    public String getColumnName() {

        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
