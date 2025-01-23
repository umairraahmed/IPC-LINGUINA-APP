package com.example.ipc.ui.login;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipc.AccountDetails;
import com.example.ipc.AdminLogin;
import com.example.ipc.ForgotPass;
import com.example.ipc.MainDashboard;
import com.example.ipc.R;
import com.example.ipc.Signup;
import com.example.ipc.data.model.LoggedInUser;
import com.example.ipc.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    TextView EmailLg, passwordLg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailLg = findViewById(R.id.Email);
        passwordLg = findViewById(R.id.password);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct =  GoogleSignIn.getLastSignedInAccount(this);
        if (acct!= null){
            navigateToTheDashboard();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!ValidateEmail() | !ValidatePassword())
                {
                    return;
                }

                String emailLOG = binding.Email.getText().toString().trim();
                String passwordLOG = binding.password.getText().toString();

                progressDialog.setTitle("Just a moment..." +
                        "Verifying Credentials");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(emailLOG, passwordLOG)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.show();
                                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainDashboard.class));

                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPass.class));
            }
        });

        binding.goToSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Signup.class));
            }
        });

        binding.googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Checking Google Accounts");
                progressDialog.show();

                GsignIn();
            }
        });
        binding.adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AdminLogin activity
                startActivity(new Intent(LoginActivity.this, AdminLogin.class));
            }
        });
    }

    private boolean ValidateEmail(){

        String crval = binding.Email.getText().toString().trim();
        String Emailpattern = "[a-z A-Z 0-9._-]+ @[a-z]= \\. + [a-z] +";

        if (crval.isEmpty())
        {
            EmailLg.setError("Email cannot be empty");
        return false;
        }

        else {
            EmailLg.setError(null);
            return true;

        }

    }

    private boolean ValidatePassword () {

        String pasvalue = binding.password.getText().toString().trim();

        if (pasvalue.isEmpty())
        {
            passwordLg.setError("Password cannot be empty");
            return false;
        }

        else {
            passwordLg.setError(null);
            return true;
        }

    }

    void GsignIn() {
        Intent GsignInIntent = gsc.getSignInIntent();
        progressDialog.show();
        progressDialog.setTitle("Getting Google Accounts");

        startActivityForResult(GsignInIntent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            progressDialog.setTitle("Logging in with Google Account");
            progressDialog.show();
            try {
                task.getResult(ApiException.class);
                navigateToTheDashboard();
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToTheDashboard() {
        finish();
        Intent intent = new Intent(LoginActivity.this, MainDashboard.class);
        startActivity(intent);

    }

}