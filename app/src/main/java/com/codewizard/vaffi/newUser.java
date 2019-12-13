package com.codewizard.vaffi;

public class newUser {
    String userID;
    String userName;
    String userEmail;
    String userContact;
    String pass;

    public newUser() {
    }

    public newUser(String userID, String userName, String userEmail, String userContact, String pass) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.pass = pass;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getPass() {
        return pass;
    }
}
