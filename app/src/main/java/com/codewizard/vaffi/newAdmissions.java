package com.codewizard.vaffi;

public class newAdmissions {

    String studentID;
    String applicantName;
    String applicantGender;
    String applicantContact;
    String applicantEmail;
    String addr;
    String addrState;
    String addrCity;
    String addrPin;
    String appCategory;
    String aadharNo;
    String instName;

    public newAdmissions() {}

    public newAdmissions(String studentID, String applicantName, String applicantGender, String applicantContact, String applicantEmail, String addr, String addrState, String addrCity, String addrPin, String appCategory, String aadharNo, String instName) {
        this.studentID = studentID;
        this.applicantName = applicantName;
        this.applicantGender = applicantGender;
        this.applicantContact = applicantContact;
        this.applicantEmail = applicantEmail;
        this.addr = addr;
        this.addrState = addrState;
        this.addrCity = addrCity;
        this.addrPin = addrPin;
        this.appCategory = appCategory;
        this.aadharNo = aadharNo;
        this.instName = instName;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantGender() {
        return applicantGender;
    }

    public String getApplicantContact() {
        return applicantContact;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getAddr() {
        return addr;
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

    public String getAppCategory() {
        return appCategory;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public String getInstName() {
        return instName;
    }
}
