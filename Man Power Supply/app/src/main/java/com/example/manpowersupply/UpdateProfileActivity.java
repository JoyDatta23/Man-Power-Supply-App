package com.example.manpowersupply;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText userNameEditText, userEmailEditText, userPhoneEditText, userPasswordEditText;
    private DatabaseHelper dbHelper;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Initialize EditTexts and other views...
        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPhoneEditText = findViewById(R.id.userPhoneEditText);
        userPasswordEditText = findViewById(R.id.userPasswordEditText);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("USER_ID");

        // Check if currentUserId is null before proceeding
        if (currentUserId == null) {
            // Handle the case where currentUserId is null (e.g., show an error message)
            Log.e("UpdateProfileActivity", "currentUserId is null");
            finish(); // Close the activity
            return;
        }

        loadCurrentUserDetails();
    }

    private void loadCurrentUserDetails() {
        Cursor cursor = dbHelper.getCurrentUser(this,currentUserId);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int userNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            int userEmailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            int userPhoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);


            if (idIndex != -1) currentUserId = cursor.getString(idIndex);
            if (userNameIndex != -1) {
                String userName = cursor.getString(userNameIndex);
                userNameEditText.setText(userName);
            }
            if (userEmailIndex != -1) {
                String userEmail = cursor.getString(userEmailIndex);
                userEmailEditText.setText(userEmail);
            }
            if (userPhoneIndex != -1) {
                String userPhone = cursor.getString(userPhoneIndex);
                userPhoneEditText.setText(userPhone);
            }
            // Be cautious with handling passwords, it's not typical to display them

            cursor.close();
        } else {
            // Handle the case where no current user is found
        }
    }

    public void onSaveButtonClicked(View view) {
        // Assuming you have already initialized these EditText fields and they contain the updated values
        String updatedUserName = userNameEditText.getText().toString();
        String updatedUserEmail = userEmailEditText.getText().toString();
        String updatedUserPhone = userPhoneEditText.getText().toString();
        String updatedUserPassword = userPasswordEditText.getText().toString();

        if (updatedUserName.isEmpty() || updatedUserPassword.isEmpty() ||updatedUserPhone.isEmpty() || updatedUserEmail.isEmpty() ) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter username/email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // You can add validation here to check if the fields are not empty, etc.

        boolean isUpdated = dbHelper.updateUser(currentUserId, updatedUserName, updatedUserEmail, updatedUserPhone, updatedUserPassword);
        if (isUpdated) {
            // Update successful. You can show a success message or take any other necessary action
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Update failed. Handle the error, maybe show a toast message or a dialog
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    public void onGoBack(View view){
        finishAffinity();
        Toast.makeText(this,"User Profile", Toast.LENGTH_SHORT).show();
    }

}
