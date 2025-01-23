package com.example.ipc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipc.data.model.Comment;
import com.example.ipc.data.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PostDetailActivity.java
public class PostDetailActivity extends AppCompatActivity {

    private TextView postTitleTextView, postDescriptionTextView;
    private EditText commentEditText;
    private Button addCommentButton;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private FirebaseFirestore db;
    private String postId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize views
        postTitleTextView = findViewById(R.id.postTitleTextView);
        postDescriptionTextView = findViewById(R.id.postDescriptionTextView);
        commentEditText = findViewById(R.id.commentEditText);
        addCommentButton = findViewById(R.id.addCommentButton);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);

        // Initialize RecyclerView and Adapter for comments
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get post details from the intent or wherever you store them
        Post post = getIntent().getParcelableExtra("post");
        postId = getIntent().getStringExtra("postId");

        // Set post details in the views
        postTitleTextView.setText(post.getTitle());
        postDescriptionTextView.setText(post.getDescription());
        fetchComments(postId);

        // Handle the click event for the "Add Comment" button
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    addComment(commentText);
                }
            }
        });
    }
    private void fetchComments(String postId) {
        // Query comments collection for the given postId
        db.collection("comments")
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        commentList.clear(); // Clear existing comments
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming you have a Comment class with appropriate getters
                            Comment comment = document.toObject(Comment.class);
                            commentList.add(comment);
                        }
                        commentAdapter.notifyDataSetChanged(); // Notify the adapter of changes
                    } else {
                        // Handle errors
                        // You might want to show a message to the user or log the error
                    }
                });
    }

    private void addComment(String commentText) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get the user UID
            String userUid = currentUser.getUid();

            // Create a new comment map
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("postId", postId);
            commentMap.put("userUid", userUid);
            commentMap.put("commentText", commentText);

            // Add the comment to Firestore
            db.collection("comments")
                    .add(commentMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                // Comment added successfully
                                showToast("Comment added successfully!");
                                // Clear the comment EditText
                                commentEditText.setText("");
                                // Fetch the updated comments
                                fetchComments(postId);
                            } else {
                                // Handle errors
                                showToast("Failed to add comment. Please try again.");                            }
                        }
                    });
        }
    }

    private void showToast(String message) {
        Toast.makeText(PostDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
