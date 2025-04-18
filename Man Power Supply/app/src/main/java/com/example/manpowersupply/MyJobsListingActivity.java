package com.example.manpowersupply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyJobsListingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyJobAdapter adapter;
    private List<Job> jobList;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs_listing);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("CURRENT_USER_ID", "");

        recyclerView = findViewById(R.id.recyclerViewJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();

        adapter = new MyJobAdapter(jobList, this::onItemClick, this::onItemLongClick);
        recyclerView.setAdapter(adapter);

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logout());

        Button buttonExit = findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(v -> exitApp());

        Button buttonAddJobs = findViewById(R.id.buttonAddJobs);
        buttonAddJobs.setOnClickListener(v -> navigateToAddJob());

        Button buttonProfile = findViewById(R.id.buttonHome);
        buttonProfile.setOnClickListener(v-> navigateToHome() );

        loadMyJobsFromDatabase();
    }

    private void onItemClick(Job job) {
        Intent intent = new Intent(MyJobsListingActivity.this, MyJobDetailActivity.class);
        intent.putExtra("JOB_ID",job.getJobId());
        intent.putExtra("JOB_TITLE", job.getTitle());
        startActivity(intent);
    }

    private void onItemLongClick(Job job) {
        new AlertDialog.Builder(this)
                .setTitle("Job Options")
                .setMessage("Choose an action for this job:")
                .setPositiveButton("Delete", (dialog, which) -> deleteJob(job))
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Update", (dialog, which) -> navigateToUpdateJobActivity(job))
                .show();
    }

    private void navigateToUpdateJobActivity(Job job) {
        Intent intent = new Intent(MyJobsListingActivity.this, UpdateJobActivity.class);
        intent.putExtra("JOB_ID", job.getJobId());
        startActivity(intent);
    }

    private void navigateToHome() {
        Intent intent = new Intent(MyJobsListingActivity.this, JobListingsActivity.class);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }



    private void deleteJob(Job job) {
        DatabaseHelperForJob dbHelper = new DatabaseHelperForJob(this);
        if (dbHelper.deleteJob(job.getJobId())) {
            loadMyJobsFromDatabase();
            Toast.makeText(this, "Job deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete job", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void exitApp() {
        finishAffinity();
    }

    private void navigateToAddJob() {
        Intent intent = new Intent(this, JobPosting.class);
        startActivity(intent);
    }


    private void loadMyJobsFromDatabase() {
        DatabaseHelperForJob dbHelperForJob = new DatabaseHelperForJob(this);
        jobList = dbHelperForJob.getJobsByUser(currentUserId);

        Log.d("MyJobsListingActivity", "Number of my jobs loaded: " + jobList.size());
        adapter.setJobList(jobList);
        adapter.notifyDataSetChanged();
    }
}