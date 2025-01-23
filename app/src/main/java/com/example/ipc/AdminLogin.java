package com.example.ipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    private EditText AdmUsername;
    private EditText AdmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        AdmUsername = findViewById(R.id.Admin_Username);
        AdmPassword = findViewById(R.id.Admin_Password);

        Button btnLogin = findViewById(R.id.Admin_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the username and password match the desired values
                if (AdmUsername.getText().toString().equals("admin") &&
                        AdmPassword.getText().toString().equals("admin")) {

                    // If the login is successful, start the admin_panel activity
                    Intent intent = new Intent(AdminLogin.this, AdminPanel.class);
                    startActivity(intent);

                } else {
                    // If the login fails, show a toast message
                    Toast.makeText(AdminLogin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}