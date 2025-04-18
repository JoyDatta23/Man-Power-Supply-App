package com.example.manpowersupply;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText userNameEditText, userEmailEditText, userPhoneEditText, userPasswordEditText, userRePasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        userNameEditText = findViewById(R.id.edit_userName);
        userEmailEditText = findViewById(R.id.edit_userEmail);
        userPhoneEditText = findViewById(R.id.edit_userPhone);
        userPasswordEditText = findViewById(R.id.edit_userPassword);
        userRePasswordEditText = findViewById(R.id.edit_userRePassword);

        Button signUpButton = findViewById(R.id.loginButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private boolean validateInputs() {
        if (userNameEditText.getText().toString().trim().isEmpty()) {
            userNameEditText.setError("Username is required");
            return false;
        }
        if (userEmailEditText.getText().toString().trim().isEmpty()) {
            userEmailEditText.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmailEditText.getText().toString()).matches()) {
            userEmailEditText.setError("Enter a valid email");
            return false;
        }
        if (userPhoneEditText.getText().toString().trim().isEmpty()) {
            userPhoneEditText.setError("Phone number is required");
            return false;
        }
        if (userPasswordEditText.getText().toString().trim().isEmpty()) {
            userPasswordEditText.setError("Password is required");
            return false;
        }
        if (userRePasswordEditText.getText().toString().trim().isEmpty()) {
            userRePasswordEditText.setError("Confirm your password");
            return false;
        }
        if (!userPasswordEditText.getText().toString().equals(userRePasswordEditText.getText().toString())) {
            userRePasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void registerUser() {
        if (!validateInputs()) {
            return;
        }

        String userName = userNameEditText.getText().toString();
        String userEmail = userEmailEditText.getText().toString();
        String userPhone = userPhoneEditText.getText().toString();
        String userPassword = userPasswordEditText.getText().toString();
        String userRePassword = userRePasswordEditText.getText().toString();


        if (!userPassword.equals(userRePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String userId = dbHelper.addUser(userName, userEmail, userPhone, userPassword);

        if (userId != null && !userId.isEmpty()) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToLogin(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
