package com.example.manpowersupply;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class MyJobDetailActivity extends AppCompatActivity {

    private DatabaseHelperForJob dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_detail);

        dbHelper = new DatabaseHelperForJob(this);

        String jobId = getIntent().getStringExtra("JOB_ID");

        Cursor cursor = dbHelper.getJob(jobId);

        if (cursor != null && cursor.moveToFirst()) {
            populateJobDetails(cursor);
        }

        if (cursor != null) {
            cursor.close();
        }

        findViewById(R.id.btnGoBack).setOnClickListener(this::goBack);
    }

    private void populateJobDetails(Cursor cursor) {
        int jobTitleIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_TITLE);
        int jobDescriptionIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_DESCRIPTION);
        int jobSkillsIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_SKILLS);
        int minSalIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MIN_SAL);
        int maxSalIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MAX_SAL);
        int deadlineIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_DEADLINE);

        TextView titleView = findViewById(R.id.textViewJobTitle);
        if (jobTitleIndex != -1) {
            String jobTitle = cursor.getString(jobTitleIndex);
            titleView.setText(jobTitle);
        }

        TextView descriptionView = findViewById(R.id.textViewJobDescription);
        if (jobDescriptionIndex != -1) {
            String jobDescription = cursor.getString(jobDescriptionIndex);
            descriptionView.setText(jobDescription);
        }

        ChipGroup chipGroupSkills = findViewById(R.id.chipGroupSkills);
        chipGroupSkills.removeAllViews();
        if (jobSkillsIndex != -1) {
            String jobSkills = cursor.getString(jobSkillsIndex);
            List<String> jobSkillsList = Arrays.asList(jobSkills.split(", "));
            for (String skill : jobSkillsList) {
                Chip chip = new Chip(this);
                chip.setText(skill);
                chip.setClickable(false);
                chipGroupSkills.addView(chip);
            }
        }

        // Setting the salary range
        TextView salaryView = findViewById(R.id.textViewSalary);
        if (minSalIndex != -1 && maxSalIndex != -1) {
            String minSalary = cursor.getString(minSalIndex);
            String maxSalary = cursor.getString(maxSalIndex);
            salaryView.setText("$" + minSalary + " - $" + maxSalary);
        }

        TextView deadlineView = findViewById(R.id.textViewDeadline);
        if (deadlineIndex != -1) {
            String deadline = cursor.getString(deadlineIndex);
            deadlineView.setText(deadline);
        }
    }

    public void goBack(View view) {
        finish();
    }

}
