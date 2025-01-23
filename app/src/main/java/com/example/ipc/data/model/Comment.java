package com.example.ipc.data.model;

public class Comment {
    private String commentId;
    private String postId; // Post ID to which the comment belongs
    private String userId; // User ID of the comment creator
    private String commentText;
    private String userName;

    public Comment(String postId, String userId, String userName, String commentText) {
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.commentText = commentText;
    }
    public Comment() {
        // Default constructor required for Firebase Firestore to deserialize objects
    }

    // Getters and setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
