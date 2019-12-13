package com.codewizard.vaffi;

public class loanApplication {

    String loanID;
    String applicantName;
    String applicantContact;
    String applicantEmail;
    String addrStreet;
    String addrArea;
    String addrLandmark;
    String addrState;
    String addrCity;
    String addrPin;
    String bnkName;
    String accType;
    String accNo;
    String aadharNo;
    String panNo;
    String loanType;

    public loanApplication() {
    }

    public loanApplication(String loanID, String applicantName, String applicantContact, String applicantEmail, String addrStreet, String addrArea, String addrLandmark, String addrState, String addrCity, String addrPin, String bnkName, String accType, String accNo, String aadharNo, String panNo, String loanType) {
        this.loanID = loanID;
        this.applicantName = applicantName;
        this.applicantContact = applicantContact;
        this.applicantEmail = applicantEmail;
        this.addrStreet = addrStreet;
        this.addrArea = addrArea;
        this.addrLandmark = addrLandmark;
        this.addrState = addrState;
        this.addrCity = addrCity;
        this.addrPin = addrPin;
        this.bnkName = bnkName;
        this.accType = accType;
        this.accNo = accNo;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
        this.loanType = loanType;
    }

    public String getLoanID() {
        return loanID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantContact() {
        return applicantContact;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public String getAddrArea() {
        return addrArea;
    }

    public String getAddrLandmark() {
        return addrLandmark;
    }

    public String getAddrState() {
        return addrState;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public String getAddrPin() {
        return addrPin;
    }

    public String getBnkName() {
        return bnkName;
    }

    public String getAccType() {
        return accType;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public String getLoanType() {
        return loanType;
    }
}
