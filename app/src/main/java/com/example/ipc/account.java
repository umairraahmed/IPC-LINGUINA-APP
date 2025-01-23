package com.example.ipc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class account extends Fragment {

    Activity context;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView Welcome_User;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getActivity();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        Welcome_User = root.findViewById(R.id.userwelcomename);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onStart(){
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
                        String displayName = document.getString("displayname");
                        Welcome_User.setText(displayName);
                    }
                } else {
                    // Handle errors
                    // For example, you can log the error or show a default message
                }
            });
        }

        Button Tacc = (Button) context.findViewById(R.id.accounttap);
        Tacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AccountDetails.class);
                startActivity(intent);
            }
        });

        Button Contact_Info = (Button) context.findViewById(R.id.contactUs);
        Contact_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ContactUs.class);
                startActivity(intent);
            }
        });

        Button About_Us = (Button) context.findViewById(R.id.aboutUs);
        About_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, AboutUs.class);
                startActivity(intent);
            }
        });

        Button Feedback = (Button) context.findViewById(R.id.feedback);
        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        Button Support = (Button) context.findViewById(R.id.Community_Support);
        Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, CS_Home.class);
                startActivity(intent);
            }
        });
    }

}