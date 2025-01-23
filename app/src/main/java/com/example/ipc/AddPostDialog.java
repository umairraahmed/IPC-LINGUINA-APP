package com.example.ipc;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ipc.data.model.LoggedInUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddPostDialog {

    private Context context;
    private FirebaseFirestore db;
    private String userUid;

    public AddPostDialog(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.userUid = getLoggedInUserUidFromFirestore();
    }

    public void show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add New Post");

        // Inflate the custom layout for adding a post in a dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_post, null);
        builder.setView(view);

        // Find views in the dialog layout
        final EditText editTextPostTitle = view.findViewById(R.id.editTextPostTitle);
        final EditText editTextPostDescription = view.findViewById(R.id.editTextPostDescription);
        Button buttonAddPost = view.findViewById(R.id.buttonAddPost);

        // Set up the dialog views and actions
        final AlertDialog dialog = builder.create();

        // Handle the "Add Post" button click
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from the EditText fields
                String postTitle = editTextPostTitle.getText().toString().trim();
                String postDescription = editTextPostDescription.getText().toString().trim();

                // Validate data if needed
                // Check if title or description is missing
                if (postTitle.isEmpty() || postDescription.isEmpty()) {
                    // Show an error toast message
                    Toast.makeText(context, "Please enter both title and description", Toast.LENGTH_LONG).show();
                } else {
                    // Get the user UID from Firestore using LoggedInUser
                    String userUid = getUserUid();

                    // Create a new post map
                    Map<String, Object> post = new HashMap<>();
                    post.put("title", postTitle);
                    post.put("description", postDescription);
                    post.put("userUid", userUid);  // Store user UID in Firestore

                    // Add the post to Firebase Firestore
                    db.collection("posts")
                            .add(post)
                            .addOnSuccessListener(documentReference -> {
                                // Post added successfully
                                Toast.makeText(context, "Post added successfully!", Toast.LENGTH_LONG).show();
                                // Close the dialog
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Toast.makeText(context, "Failed to add post. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }
            }
        });
        dialog.show();
    }
    // Method to get user UID from Firestore using LoggedInUser
    private String getLoggedInUserUidFromFirestore() {
        // Assuming you have an instance of LoggedInUser
        LoggedInUser loggedInUser = LoggedInUser.getCurrentUser();
        if (loggedInUser != null) {
            return loggedInUser.getUid();  // Assuming you have a getUid() method in LoggedInUser
        } else {
            return "";
        }
    }
    private String getUserUid() {
        return userUid;
    }
}
