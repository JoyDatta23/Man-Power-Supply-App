package com.example.manpowersupply;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    private TextView profileUsername;
    private TextView profilePhone;
    private TextView profileEmail;

    private DatabaseHelper dbHelper;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("USER_ID");
        Log.d("UserProfile", "Current user ID: " + currentUserId);


        profileUsername = findViewById(R.id.profile_username);
        profilePhone = findViewById(R.id.profile_phone);
        profileEmail = findViewById(R.id.profile_email);

        Button deleteButton = findViewById(R.id.profile_del_btn);
        deleteButton.setOnClickListener(v -> confirmDelete());

        Button updateButton = findViewById(R.id.profile_up_btn);
        updateButton.setOnClickListener(v -> navigateToUpdateProfile());

        Button myJobsButton = findViewById(R.id.profile_myJob_btn);
        myJobsButton.setOnClickListener(v -> {
            Intent intents = new Intent(UserProfile.this, MyJobsListingActivity.class);
            startActivity(intents);
        });


        loadUserData();
    }


    private void loadUserData() {
        Log.d("UserProfile", "Loading user data for ID: " + currentUserId);

        if (currentUserId.isEmpty()) {
            Log.d("UserProfile", "Current user ID is empty");
            return;
        }

        Cursor cursor = dbHelper.getCurrentUser(this,currentUserId);

        if (cursor == null) {
            Log.d("UserProfile", "Cursor is null");
            return;
        }

        if (cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            int userEmailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            int userPhoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);

            String userName = cursor.getString(userNameIndex);
            String userEmail = cursor.getString(userEmailIndex);
            String userPhone = cursor.getString(userPhoneIndex);

            profileUsername.setText(userName);
            profileEmail.setText(userEmail);
            profilePhone.setText(userPhone);

            Log.d("UserProfile", "Username: " + userName);
            Log.d("UserProfile", "Email: " + userEmail);
            Log.d("UserProfile", "Phone: " + userPhone);

            cursor.close();
        } else {
            Log.d("UserProfile", "Cursor is empty");
            Toast.makeText(this, "User data not found", Toast.LENGTH_LONG).show();
        }
    }



    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUserAccount())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUserAccount() {
        if (dbHelper.deleteUser(currentUserId)) {
            Intent intent = new Intent(UserProfile.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUpdateProfile() {
        Intent intent = new Intent(UserProfile.this, UpdateProfileActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }


    public void goBack(View view){
        finish();
    }

}
