package biz.africanbib.Models;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class SimpleText
{
    private String title;
    private String xmlTag;

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SimpleText(String title) {

        this.title = title;
    }
}
