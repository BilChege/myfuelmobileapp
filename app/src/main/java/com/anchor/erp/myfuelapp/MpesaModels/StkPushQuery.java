package com.anchor.erp.myfuelapp.MpesaModels;

public class StkPushQuery {

    private String BusinessShortCode;
    private String Password;
    private String Timestamp;
    private String CheckoutRequestID;

    public StkPushQuery(String BusinessShortCode, String Password, String Timestamp, String CheckoutRequestID) {
        this.BusinessShortCode = BusinessShortCode;
        this.Password = Password;
        this.Timestamp = Timestamp;
        this.CheckoutRequestID = CheckoutRequestID;
    }

    public StkPushQuery() {
    }

    public String getBusinessShortCode() {
        return BusinessShortCode;
    }

    public void setBusinessShortCode(String BusinessShortCode) {
        this.BusinessShortCode = BusinessShortCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String Timestamp) {
        this.Timestamp = Timestamp;
    }

    public String getCheckoutRequestID() {
        return CheckoutRequestID;
    }

    public void setCheckoutRequestID(String CheckoutRequestID) {
        this.CheckoutRequestID = CheckoutRequestID;
    }

    @Override
    public String toString() {
        return "StkPushQuery{\n" + "BusinessShortCode=" + BusinessShortCode + ",\n Password=" + Password + ",\n Timestamp=" + Timestamp + ",\n CheckoutRequestID=" + CheckoutRequestID + '}';
    }

}
