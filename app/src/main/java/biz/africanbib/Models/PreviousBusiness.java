package biz.africanbib.Models;

/**
 * Created by Balpreet on 16-Aug-17.
 */

public class PreviousBusiness
{
    int businessID;
    String businessName;
    String dateOfAddition;
    String timeOfAddition;
    String dateOfUploading;
    String timeOfUploading;

    public String getDateOfUploading() {
        return dateOfUploading;
    }

    public void setDateOfUploading(String dateOfUploading) {
        this.dateOfUploading = dateOfUploading;
    }

    public String getTimeOfUploading() {
        return timeOfUploading;
    }

    public void setTimeOfUploading(String timeOfUploading) {
        this.timeOfUploading = timeOfUploading;
    }

    public String getTimeOfAddition() {
        return timeOfAddition;
    }

    public void setTimeOfAddition(String timeOfAddition) {
        this.timeOfAddition = timeOfAddition;
    }

    public String getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(String dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public PreviousBusiness(int businessID, String businessName, String dateOfAddition, boolean uploadStatus) {

        this.businessID = businessID;
        this.businessName = businessName;
        this.dateOfAddition = dateOfAddition;
        this.uploadStatus = uploadStatus;
    }

    boolean uploadStatus;

    public boolean isUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(boolean uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

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
