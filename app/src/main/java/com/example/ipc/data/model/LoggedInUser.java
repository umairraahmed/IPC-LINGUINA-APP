package com.example.ipc.data.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {


    private String name,number,email,displayname;
    private String uid;

    public LoggedInUser() {
    }

    public LoggedInUser(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public static LoggedInUser getCurrentUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {
            // If a user is logged in, create a LoggedInUser instance
            String name = firebaseUser.getDisplayName();
            String number = firebaseUser.getPhoneNumber();
            String email = firebaseUser.getEmail();
            String uid = firebaseUser.getUid(); // Add user UID

            LoggedInUser loggedInUser = new LoggedInUser(name, number, email);
            loggedInUser.setUid(uid); // Set user UID
            return loggedInUser;
        } else {
            // If no user is logged in, return null
            return null;
        }
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getDisplayName() {
        return displayname;
    }

    public void setDisplayName(String displayname) {
        this.displayname = displayname;
    }
}