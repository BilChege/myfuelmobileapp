package com.anchor.erp.myfuelapp.MpesaModels;

public class StkRequestSuccess {

    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResponseDescription;
    private String ResponseCode;
    private String CustomerMessage;

    public StkRequestSuccess(String MerchantRequestID, String CheckoutRequestID, String ResponseDescription, String ResponseCode, String CustomerMessage) {
        this.MerchantRequestID = MerchantRequestID;
        this.CheckoutRequestID = CheckoutRequestID;
        this.ResponseDescription = ResponseDescription;
        this.ResponseCode = ResponseCode;
        this.CustomerMessage = CustomerMessage;
    }

    public StkRequestSuccess() {
    }

    public String getMerchantRequestID() {
        return MerchantRequestID;
    }

    public void setMerchantRequestID(String MerchantRequestID) {
        this.MerchantRequestID = MerchantRequestID;
    }

    public String getCheckoutRequestID() {
        return CheckoutRequestID;
    }

    public void setCheckoutRequestID(String CheckoutRequestID) {
        this.CheckoutRequestID = CheckoutRequestID;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String ResponseDescription) {
        this.ResponseDescription = ResponseDescription;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getCustomerMessage() {
        return CustomerMessage;
    }

    public void setCustomerMessage(String CustomerMessage) {
        this.CustomerMessage = CustomerMessage;
    }

    @Override
    public String toString() {
        return "StkRequestSuccess{\n" + "MerchantRequestID=" + MerchantRequestID + ",\n CheckoutRequestID=" + CheckoutRequestID + ",\n ResponseDescription=" + ResponseDescription + ",\n ResponseCode=" + ResponseCode + ",\n CustomerMessage=" + CustomerMessage + '}';
    }

}
