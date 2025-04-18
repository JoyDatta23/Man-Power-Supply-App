package com.example.manpowersupply;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class UpdateJobActivity extends AppCompatActivity {


    private EditText jobTitleInput, jobDescriptionInput, empNeedInput, minSalaryInput, maxSalaryInput, deadlineInput;
    private ChipGroup chipGroupSkills;
    private DatabaseHelperForJob dbHelperForJob;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job);

        // Initialize database helper
        dbHelperForJob = new DatabaseHelperForJob(this);

        // Retrieve job ID from intent
        jobId = getIntent().getStringExtra("JOB_ID");
        if (jobId == null) {
            Toast.makeText(this, "Job not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        jobTitleInput = findViewById(R.id.job_title_input);
        jobDescriptionInput = findViewById(R.id.job_description_input);
        empNeedInput = findViewById(R.id.emp_num_input);
        minSalaryInput = findViewById(R.id.min_salary_input);
        maxSalaryInput = findViewById(R.id.max_salary_input);
        deadlineInput = findViewById(R.id.date_picker_actions);
        chipGroupSkills = findViewById(R.id.chipGroupSkills);

        Button updateJobButton = findViewById(R.id.update_job_button);
        updateJobButton.setOnClickListener(v -> updateJob());

        Button cancelButton = findViewById(R.id.cancel_update_button);
        cancelButton.setOnClickListener(v -> finish());

        loadJobData();
    }

    private void loadJobData() {
        Cursor cursor = dbHelperForJob.getJob(jobId);
        if (cursor != null && cursor.moveToFirst()) {


            int jobTitleIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_TITLE);
            int jobDescriptionIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_DESCRIPTION);
            int empNeedIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_EMP_NEED);
            int minSalaryIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MIN_SAL);
            int maxSalaryIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MAX_SAL);
            int deadLineIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_DEADLINE);
            int skillsIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_SKILLS);

            if(jobTitleIndex !=1){
                String jobTitle = cursor.getString(jobTitleIndex);
                jobTitleInput.setText(jobTitle);
            }

            if(jobDescriptionIndex !=1){
                String jobDes = cursor.getString(jobDescriptionIndex);
                jobDescriptionInput.setText(jobDes);
            }

            if(empNeedIndex !=1){
                String empNeed = cursor.getString(empNeedIndex);
                empNeedInput.setText(empNeed);
            }


            if(minSalaryIndex !=1){
                String minSal = cursor.getString(minSalaryIndex);
                minSalaryInput.setText(minSal);
            }

            if(maxSalaryIndex !=1){
                String maxSal = cursor.getString(maxSalaryIndex);
                maxSalaryInput.setText(maxSal);
            }

            if(deadLineIndex !=1){
                String deadLine = cursor.getString(deadLineIndex);
                deadlineInput.setText(deadLine);
            }

            if(skillsIndex !=1){
                String skills = cursor.getString(skillsIndex);
                setSkillsChips(skills);
            }
        }
        cursor.close();
    }

    private void setSkillsChips(String skills) {
        List<String> skillList = Arrays.asList(skills.split(", "));
        for (int i = 0; i < chipGroupSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSkills.getChildAt(i);
            if (skillList.contains(chip.getText().toString())) {
                chip.setChecked(true);
            }
        }
    }


    private void updateJob() {
        String title = jobTitleInput.getText().toString();
        String description = jobDescriptionInput.getText().toString();
        String empNeed = empNeedInput.getText().toString();
        String minSalary = minSalaryInput.getText().toString();
        String maxSalary = maxSalaryInput.getText().toString();
        String deadline = deadlineInput.getText().toString();
        String skills = getSelectedSkills();

        if (title.isEmpty() || description.isEmpty() || empNeed.isEmpty() || minSalary.isEmpty() || maxSalary.isEmpty() || deadline.isEmpty() || skills.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelperForJob.updateJob(jobId, title, description, empNeed, skills, minSalary, maxSalary, deadline)) {
            Toast.makeText(this, "Job updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update job", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedSkills() {
        StringBuilder selectedSkills = new StringBuilder();
        for (int i = 0; i < chipGroupSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSkills.getChildAt(i);
            if (chip.isChecked()) {
                if (selectedSkills.length() > 0) {
                    selectedSkills.append(", ");
                }
                selectedSkills.append(chip.getText());
            }
        }
        return selectedSkills.toString();
    }




}
