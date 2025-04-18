package com.example.manpowersupply;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class JobPosting extends AppCompatActivity {
    private EditText jobTitleInput, jobDescriptionInput,emp_input, minSalaryInput, maxSalaryInput, deadlineInput;
    private ChipGroup chipGroupSkills;
    private DatabaseHelperForJob dbHelperForJob;
    private Button showChipsEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);

        jobTitleInput = findViewById(R.id.job_title_input);
        jobDescriptionInput = findViewById(R.id.job_description_input);
        emp_input = findViewById(R.id.emp_num_input);
        minSalaryInput = findViewById(R.id.min_salary_input);
        maxSalaryInput = findViewById(R.id.max_salary_input);
        deadlineInput = findViewById(R.id.date_picker_actions);
        chipGroupSkills = findViewById(R.id.chipGroupSkills);

        dbHelperForJob = new DatabaseHelperForJob(this);

        Button addJobButton = findViewById(R.id.add_job_button);
        addJobButton.setOnClickListener(v -> submitJob());

        Button cancelButton = findViewById(R.id.cancel_job_button);
        cancelButton.setOnClickListener(v -> {
            Intent jobView = new Intent(v.getContext(), JobListingsActivity.class);
            startActivity(jobView);
        });

        showChipsEditText = findViewById(R.id.showchips);
        showChipsEditText.setOnClickListener(v -> toggleChipVisibility());

        // No need to set an OnCheckedChangeListener if you're not performing any action on check change
    }

    private void submitJob() {
        String title = jobTitleInput.getText().toString();
        String description = jobDescriptionInput.getText().toString();
        String emp_need = emp_input.getText().toString();
        String minSalary = minSalaryInput.getText().toString();
        String maxSalary = maxSalaryInput.getText().toString();
        String deadline = deadlineInput.getText().toString();
        String skills = getSelectedSkills();

        if (title.isEmpty() || description.isEmpty() || emp_need.isEmpty() || minSalary.isEmpty() || maxSalary.isEmpty() || deadline.isEmpty() || skills.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Incomplete Information")
                    .setMessage("Please fill out all fields to post the job.")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("CURRENT_USER_ID", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            return;
        }

        String jobId = dbHelperForJob.addJob(title, description, emp_need, skills, minSalary, maxSalary, deadline, userId);
        if (jobId != null && !jobId.isEmpty()) {
            Toast.makeText(this, "Job posted successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, "Failed to post job", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        jobTitleInput.setText("");
        jobDescriptionInput.setText("");
        emp_input.setText("");
        minSalaryInput.setText("");
        maxSalaryInput.setText("");
        deadlineInput.setText("");
        for (int i = 0; i < chipGroupSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSkills.getChildAt(i);
            chip.setChecked(false);
        }
    }

    private String getSelectedSkills() {
        StringBuilder selectedSkills = new StringBuilder();
        for (int i = 0; i < chipGroupSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSkills.getChildAt(i);
            if (chip.isChecked()) {
                if (selectedSkills.length() > 0) selectedSkills.append(", ");
                selectedSkills.append(chip.getText());
            }
        }
        return selectedSkills.toString();
    }

    private void toggleChipVisibility() {
        if (chipGroupSkills.getVisibility() == View.VISIBLE) {
            showChipsEditText.setText("Click to show Experience");
            chipGroupSkills.setVisibility(View.GONE);
        } else {
            showChipsEditText.setText("Click to hide Experience");
            chipGroupSkills.setVisibility(View.VISIBLE);
        }
    }
}
