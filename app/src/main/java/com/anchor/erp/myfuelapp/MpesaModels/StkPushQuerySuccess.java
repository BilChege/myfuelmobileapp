package com.anchor.erp.myfuelapp.MpesaModels;

public class StkPushQuerySuccess {

    private String ResponseCode;
    private String ResponseDescription;
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResultCode;
    private String ResultDesc;

    public StkPushQuerySuccess(String ResponseCode, String ResponseDescription, String MerchantRequestID, String CheckoutRequestID, String ResultCode, String ResultDesc) {
        this.ResponseCode = ResponseCode;
        this.ResponseDescription = ResponseDescription;
        this.MerchantRequestID = MerchantRequestID;
        this.CheckoutRequestID = CheckoutRequestID;
        this.ResultCode = ResultCode;
        this.ResultDesc = ResultDesc;
    }

    public StkPushQuerySuccess() {
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String ResponseDescription) {
        this.ResponseDescription = ResponseDescription;
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

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public void setResultDesc(String ResultDesc) {
        this.ResultDesc = ResultDesc;
    }

    @Override
    public String toString() {
        return "StkPushQuerySuccess{\n" + "ResponseCode=" + ResponseCode + ",\n ResponseDescription=" + ResponseDescription + ",\n MerchantRequestID=" + MerchantRequestID + ",\n CheckoutRequestID=" + CheckoutRequestID + ",\n ResultCode=" + ResultCode + ",\n ResultDesc=" + ResultDesc + '}';
    }

}
