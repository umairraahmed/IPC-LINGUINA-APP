package com.example.ipc;

import com.google.firebase.firestore.DocumentReference;

public class Feedback {
    private String userUid;
    private String uxRating;
    private String communicationRating;
    private String recommend;
    private String additionalComments;

    public Feedback() {
        // Default constructor required for Firestore
    }

    public Feedback(String userUid, String uxRating, String communicationRating, String recommend, String additionalComments) {
        this.userUid = userUid;
        this.uxRating = uxRating;
        this.communicationRating = communicationRating;
        this.recommend = recommend;
        this.additionalComments = additionalComments;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUxRating() {
        return uxRating;
    }

    public void setUxRating(String uxRating) {
        this.uxRating = uxRating;
    }

    public String getCommunicationRating() {
        return communicationRating;
    }

    public void setCommunicationRating(String communicationRating) {
        this.communicationRating = communicationRating;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    // Create a method to convert the Feedback object to a Map for Firestore
    public java.util.Map<String, Object> toMap() {
        java.util.HashMap<String, Object> result = new java.util.HashMap<>();
        result.put("userUid", userUid);
        result.put("uxRating", uxRating);
        result.put("communicationRating", communicationRating);
        result.put("recommend", recommend);
        result.put("additionalComments", additionalComments);
        return result;
    }
}