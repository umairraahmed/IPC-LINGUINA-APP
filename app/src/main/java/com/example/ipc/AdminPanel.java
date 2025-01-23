package com.example.ipc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ipc.data.model.LoggedInUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends AppCompatActivity {
    private TextView textTotalUsers;
    private RecyclerView recyclerViewUsers;
    private List<LoggedInUser> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        textTotalUsers = findViewById(R.id.textTotalUsers);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        // Initialize RecyclerView
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        // Fetch total users and user information from Firestore
        fetchUserData();
    }
    private void fetchUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalUsers = task.getResult().size();
                        textTotalUsers.setText("Total Registered Users: " + totalUsers);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            LoggedInUser user = document.toObject(LoggedInUser.class);
                            userList.add(user);
                        }

                        userAdapter.notifyDataSetChanged();
                    } else {
                        // Handle errors
                    }
                });
    }

}