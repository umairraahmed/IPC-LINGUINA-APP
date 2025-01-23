package com.example.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ipc.data.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class CS_Home extends AppCompatActivity {

    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private FirebaseFirestore db;
    private Button addPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_home);

        postRecyclerView = findViewById(R.id.postRecyclerView);
        addPostButton = findViewById(R.id.addPostButton);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);

        postAdapter.setOnPostClickListener(new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(Post post) {
                // Handle post click event
                // Start PostDetailActivity and pass the selected post details
                Intent intent = new Intent(CS_Home.this, PostDetailActivity.class);
                intent.putExtra("post", post);
                intent.putExtra("postId", post.getPostId());
                startActivity(intent);
            }
        });

        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecyclerView.setAdapter(postAdapter);

        db = FirebaseFirestore.getInstance();

        // Fetch posts from Firestore and update the adapter
        fetchPosts();

        // Handle the click event for the "Add Post" button
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the "Add Post" dialog
                AddPostDialog addPostDialog = new AddPostDialog(CS_Home.this);
                addPostDialog.show();
            }
        });
    }

    private void fetchPosts() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear the existing posts
                            postList.clear();

                            // Iterate through the Firestore documents and add them to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String description = document.getString("description");
                                String postId = document.getId();

                                // Create a Post object and add it to the list
                                Post post = new Post(title, description);
                                post.setPostId(postId);
                                postList.add(post);
                            }

                            // Notify the adapter that the data set has changed
                            postAdapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                            // Log a message or show a Toast
                        }
                    }
                });
    }
}
