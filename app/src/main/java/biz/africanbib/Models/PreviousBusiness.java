package biz.africanbib.Models;

/**
 * Created by Balpreet on 16-Aug-17.
 */

public class PreviousBusiness
{
    int businessID;
    String businessName;

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public PreviousBusiness() {
    }

    public PreviousBusiness(int businessID, String businessName) {

        this.businessID = businessID;
        this.businessName = businessName;

    }

    public PreviousBusiness(String businessName) {

        this.businessName = businessName;
    }

    public PreviousBusiness(int businessID) {

        this.businessID = businessID;
    }
}
