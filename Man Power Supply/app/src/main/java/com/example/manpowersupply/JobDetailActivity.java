package com.example.manpowersupply;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class JobDetailActivity extends AppCompatActivity {

    private DatabaseHelperForJob dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        dbHelper = new DatabaseHelperForJob(this);

        String jobId = getIntent().getStringExtra("JOB_ID");

        Cursor cursor = dbHelper.getJob(jobId);

        if (cursor != null && cursor.moveToFirst()) {
            int jobTitleIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_TITLE);
            int jobDescriptionIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_DESCRIPTION);
            int jobSkillsIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_SKILLS);
            int minSalIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MIN_SAL);
            int maxSalIndex = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_JOB_MAX_SAL);
            int postedByUser = cursor.getColumnIndex(DatabaseHelperForJob.COLUMN_POSTED_BY_USER_ID);
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

            TextView skillsLabelView = findViewById(R.id.textViewSkillsLabel);
            ChipGroup chipGroupSkills = findViewById(R.id.chipGroupSkills);

            String jobSkills = (jobSkillsIndex != -1) ? cursor.getString(jobSkillsIndex) : "";
            skillsLabelView.setText("Experience");

            List<String> jobSkillsList = Arrays.asList(jobSkills.split(", "));

            for (String skill : jobSkillsList) {
                Chip chip = new Chip(this);
                chip.setText(skill);
                chip.setClickable(false);  // Use setClickable to achieve similar behavior
                chipGroupSkills.addView(chip);
            }

            TextView salaryView = findViewById(R.id.textViewSalary);

            if (minSalIndex != -1 && maxSalIndex != -1) {
                String minSalary = cursor.getString(minSalIndex);
                String maxSalary = cursor.getString(maxSalIndex);
                salaryView.setText("$"+minSalary+" - $"+maxSalary);
            }


            TextView deadlineView = findViewById(R.id.textViewDeadline);

            if (deadlineIndex != -1) {
                String deadline = cursor.getString(deadlineIndex);
                deadlineView.setText(deadline);
            }

        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void goBack(View view) {
        finish();
    }

    public void apply(View view) {
        showDialog("Applied Successfully with your profile");
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}
