package com.example.ipc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipc.data.model.LoggedInUser;
import com.example.ipc.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class AccountDetails extends AppCompatActivity {

    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    TextView nameACC, EmailACC, numberACC;
    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        nameACC = findViewById(R.id.nameAC);
        EmailACC = findViewById(R.id.EmailAC);
        numberACC = findViewById(R.id.numberAC);
        logoutbtn = findViewById(R.id.logout_btn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (user != null) {
            String userID = user.getUid();

            // Fetch user data from Firestore
            firebaseFirestore.collection("User")
                    .document(userID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            LoggedInUser userProfile = documentSnapshot.toObject(LoggedInUser.class);

                            if (userProfile != null) {
                                String fullName = userProfile.getName();
                                String email = userProfile.getEmail();
                                String number = userProfile.getNumber();

                                nameACC.setText(fullName);
                                EmailACC.setText(email);
                                numberACC.setText(number);
                            }
                        } else {
                            // Handle the case where the document does not exist
                            Toast.makeText(AccountDetails.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AccountDetails.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        logoutbtn.setOnClickListener(v -> Logout());
    }

    void Logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(AccountDetails.this, LoginActivity.class));
        Toast.makeText(AccountDetails.this, "User Signed out", Toast.LENGTH_SHORT).show();
    }
}