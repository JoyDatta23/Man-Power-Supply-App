package com.example.manpowersupply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private CheckBox rememberUsernameCheckbox, rememberPasswordCheckbox;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        usernameEditText = findViewById(R.id.user_name);
        passwordEditText = findViewById(R.id.user_pass);
        rememberUsernameCheckbox = findViewById(R.id.user_check1);
        rememberPasswordCheckbox = findViewById(R.id.user_check2);

        loadPreferences();

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> performLogin());

        TextView goToLogin = findViewById(R.id.GoToLogin);
        goToLogin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void loadPreferences() {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean isUsernameRemembered = !savedUsername.equals("");
        boolean isPasswordRemembered = !savedPassword.equals("");

        usernameEditText.setText(isUsernameRemembered ? savedUsername : "");
        passwordEditText.setText(isPasswordRemembered ? savedPassword : "");
        rememberUsernameCheckbox.setChecked(isUsernameRemembered);
        rememberPasswordCheckbox.setChecked(isPasswordRemembered);
    }


    private void performLogin() {
        String usernameOrEmail = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter username/email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = databaseHelper.checkUser(usernameOrEmail, password);

        savePreferences(usernameOrEmail,password);
        if (userId != null) {
            Log.d("LoginActivity", "User ID: " + userId);
            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CURRENT_USER_ID", userId);
            if (rememberUsernameCheckbox.isChecked()) {
                editor.putString("username", usernameOrEmail);
            } else {
                editor.remove("username");
            }
            if (rememberPasswordCheckbox.isChecked()) {
                editor.putString("password", password);
            } else {
                editor.remove("password");
            }
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, JobListingsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid username/email or password", Toast.LENGTH_SHORT).show();
        }
    }


    private void savePreferences(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberUsernameCheckbox.isChecked()) {
            editor.putString("username", username);
        } else {
            editor.remove("username");
        }
        if (rememberPasswordCheckbox.isChecked()) {
            editor.putString("password", password);
        } else {
            editor.remove("password");
        }
        editor.apply();
    }
}
