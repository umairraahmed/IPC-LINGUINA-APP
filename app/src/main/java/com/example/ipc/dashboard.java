package com.example.ipc;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dashboard extends Fragment {
    TextView Welcome_User;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Activity context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Welcome_User = root.findViewById(R.id.userwelcomename);
        return root;
    }
    public void onStart() {
        super.onStart();

        // Check if the user is authenticated
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, fetch and display user data
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("User").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the username from Firestore and set it to the TextView
                        String username = document.getString("name");
                        Welcome_User.setText(username);
                    }
                } else {
                }
            });
        }
        Button TandC = (Button) context.findViewById(R.id.TandC);
        TandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TandC.class);
                startActivity(intent);
            }
        });

        Button capture_gesture = (Button) context.findViewById(R.id.capture_gesture);
        capture_gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, gesturedetect.class);
                startActivity(intent);
            }
        });

    }
}