package biz.africanbib.Models;

/**
 * Created by Balpreet on 30-Jul-17.
 */

public class Heading
{
    private String heading;
    private String xmlTag;

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public Heading(String heading,String xmlTag) {

        this.heading = heading;
        this.xmlTag = xmlTag;
    }
}
