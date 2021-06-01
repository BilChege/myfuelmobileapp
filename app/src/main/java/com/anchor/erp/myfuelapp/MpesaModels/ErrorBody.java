package com.anchor.erp.myfuelapp.MpesaModels;

public class ErrorBody {

    private String requestId;
    private String errorCode;
    private String errorMessage;

    public ErrorBody(String requestId, String errorCode, String errorMessage) {
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorBody() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
