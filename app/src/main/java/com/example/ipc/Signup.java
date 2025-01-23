package com.example.ipc;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipc.data.model.LoggedInUser;
import com.example.ipc.databinding.ActivitySignupBinding;
import com.example.ipc.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding binding;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    TextInputEditText NameSU;
    TextView EmailSU, passwordSU, PhnumSU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        NameSU = findViewById(R.id.nameS);
        EmailSU = findViewById(R.id.EmailS);
        passwordSU = findViewById(R.id.passwordS);
        PhnumSU = findViewById(R.id.numberS);

        binding=ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nameSUP=binding.nameS.getEditableText().toString().trim();
                String numberSUP=binding.numberS.getText().toString();
                String emailSUP=binding.EmailS.getText().toString().trim();
                String password=binding.passwordS.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(emailSUP,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(Signup.this, LoginActivity.class));
                                progressDialog.show();
                                progressDialog.setTitle("Creating your account" +
                                        "Please Wait");

                                firebaseFirestore.collection("User")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new LoggedInUser(nameSUP,numberSUP,emailSUP));

                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
            }

        });

        binding.goToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, LoginActivity.class));
            }
        });


    }

    private boolean ValidateName(){

        String nameval = binding.nameS.getEditableText().toString().trim();

        if (nameval.isEmpty())
        {
            NameSU.setError("Name cannot be empty");
            return false;
        }

        else {
            NameSU.setError(null);
            return true;

        }

    }

    private boolean ValidateEmail(){

        String crval = binding.EmailS.getText().toString().trim();
        String Emailpattern = "[a-z A-Z 0-9._-]+ @[a-z]= \\. + [a-z] +";


        if (crval.isEmpty())
        {
            EmailSU.setError("Email cannot be empty");
            return false;

        }

        else if(!crval.matches(Emailpattern)) {
            EmailSU.setError("Invalid Email address");
            return false;
        }
        else {
            EmailSU.setError(null);
            return true;

        }

    }

    private boolean ValidatePassword () {

        String pasvalue = binding.passwordS.getText().toString().trim();
        String PasswordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";

        if (pasvalue.isEmpty())
        {
            passwordSU.setError("Password cannot be empty");
            return false;
        }

        else if(!pasvalue.matches(PasswordPattern)) {
            passwordSU.setError("Password is weak. At least 1 UC and LC and SC");
            return false;
        }

        else {
            passwordSU.setError(null);
            return true;
        }

    }

    private boolean ValidateNumber(){

        String numval = binding.numberS.getText().toString().trim();

        if (numval.isEmpty())
        {
            PhnumSU.setError("Phone Number cannot be empty");
            return false;
        }

        else {
            PhnumSU.setError(null);
            return true;

        }

    }

}
