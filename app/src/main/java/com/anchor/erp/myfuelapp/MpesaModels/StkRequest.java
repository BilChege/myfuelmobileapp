package com.anchor.erp.myfuelapp.MpesaModels;

public class StkRequest {

    private String BusinessShortCode;
    private String Password;
    private String Timestamp;
    private String TransactionType;
    private String Amount;
    private String PartyA;
    private String PartyB;
    private String PhoneNumber;
    private String CallBackURL;
    private String AccountReference;
    private String TransactionDesc;

    public StkRequest(String BusinessShortCode, String Password, String Timestamp, String TransactionType, String Amount, String PartyA, String PartyB, String PhoneNumber, String CallBackURL, String AccountReference, String TransactionDesc) {
        this.BusinessShortCode = BusinessShortCode;
        this.Password = Password;
        this.Timestamp = Timestamp;
        this.TransactionType = TransactionType;
        this.Amount = Amount;
        this.PartyA = PartyA;
        this.PartyB = PartyB;
        this.PhoneNumber = PhoneNumber;
        this.CallBackURL = CallBackURL;
        this.AccountReference = AccountReference;
        this.TransactionDesc = TransactionDesc;
    }

    public StkRequest() {
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

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getPartyA() {
        return PartyA;
    }

    public void setPartyA(String PartyA) {
        this.PartyA = PartyA;
    }

    public String getPartyB() {
        return PartyB;
    }

    public void setPartyB(String PartyB) {
        this.PartyB = PartyB;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getCallBackURL() {
        return CallBackURL;
    }

    public void setCallBackURL(String CallBackURL) {
        this.CallBackURL = CallBackURL;
    }

    public String getAccountReference() {
        return AccountReference;
    }

    public void setAccountReference(String AccountReference) {
        this.AccountReference = AccountReference;
    }

    public String getTransactionDesc() {
        return TransactionDesc;
    }

    public void setTransactionDesc(String TransactionDesc) {
        this.TransactionDesc = TransactionDesc;
    }

    @Override
    public String toString() {
        return "StkRequest{\n" + "BusinessShortCode=" + BusinessShortCode + ",\n Password=" + Password + ",\n Timestamp=" + Timestamp + ",\n TransactionType=" + TransactionType + ",\n Amount=" + Amount + ",\n PartyA=" + PartyA + ",\n PartyB=" + PartyB + ",\n PhoneNumber=" + PhoneNumber + ",\n CallBackURL=" + CallBackURL + ",\n AccountReference=" + AccountReference + ",\n TransactionDesc=" + TransactionDesc + '}';
    }

}
