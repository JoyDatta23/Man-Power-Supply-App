package com.example.manpowersupply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHelperForJob extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JobsDatabase";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_JOBS = "jobs";
    public static final String COLUMN_JOB_ID = "job_id";
    public static final String COLUMN_JOB_TITLE = "title";
    public static final String COLUMN_JOB_DESCRIPTION = "description";
    public static final String COLUMN_JOB_SKILLS = "skills";
    public static final String COLUMN_JOB_MIN_SAL = "min_sal";
    public static final String COLUMN_JOB_MAX_SAL = "max_sal";

    public static final String COLUMN_JOB_DEADLINE = "deadline";
    public static final String COLUMN_POSTED_BY_USER_ID = "posted_by_user_id";
    public static final String COLUMN_EMP_NEED = "emp_need";

    public DatabaseHelperForJob(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOBS_TABLE = "CREATE TABLE " + TABLE_JOBS + "("
                + COLUMN_JOB_ID + " TEXT PRIMARY KEY,"
                + COLUMN_JOB_TITLE + " TEXT,"
                + COLUMN_JOB_DESCRIPTION + " TEXT,"
                + COLUMN_EMP_NEED + " TEXT,"
                + COLUMN_JOB_SKILLS + " TEXT,"
                + COLUMN_JOB_MIN_SAL+ " TEXT,"
                + COLUMN_JOB_MAX_SAL+ " TEXT,"
                + COLUMN_JOB_DEADLINE + " TEXT,"
                + COLUMN_POSTED_BY_USER_ID + " TEXT" + ")";
        db.execSQL(CREATE_JOBS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        onCreate(db);
    }

    public String addJob(String title, String description,String emp_need, String skills, String minSalary, String maxSalary, String deadline, String postedByUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String jobId = UUID.randomUUID().toString();

        ContentValues values = new ContentValues();
        values.put(COLUMN_JOB_ID, jobId);
        values.put(COLUMN_JOB_TITLE, title);
        values.put(COLUMN_JOB_DESCRIPTION, description);
        values.put(COLUMN_EMP_NEED, emp_need);
        values.put(COLUMN_JOB_SKILLS, skills);
        values.put(COLUMN_JOB_MIN_SAL, minSalary);
        values.put(COLUMN_JOB_MAX_SAL, maxSalary);
        values.put(COLUMN_JOB_DEADLINE, deadline);
        values.put(COLUMN_POSTED_BY_USER_ID, postedByUserId);

        db.insert(TABLE_JOBS, null, values);
        db.close();
        return jobId;
    }

    public boolean updateJob(String jobId, String title, String description,String emp_need, String skills, String minSalary, String maxSalary, String deadline) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JOB_TITLE, title);
        values.put(COLUMN_JOB_DESCRIPTION, description);
        values.put(COLUMN_EMP_NEED, emp_need);
        values.put(COLUMN_JOB_SKILLS, skills);
        values.put(COLUMN_JOB_MIN_SAL, minSalary);
        values.put(COLUMN_JOB_MAX_SAL, maxSalary);
        values.put(COLUMN_JOB_DEADLINE, deadline);

        int rowsAffected = db.update(TABLE_JOBS, values, COLUMN_JOB_ID + " = ?", new String[]{jobId});
        db.close();
        return rowsAffected > 0;
    }

    public Cursor getJob(String jobId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_JOBS, null, COLUMN_JOB_ID + " = ?", new String[]{jobId}, null, null, null);
    }

    public List<Job> getAllJobs() {
        List<Job> jobList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_JOBS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_JOB_ID);
            int titleIndex = cursor.getColumnIndex(COLUMN_JOB_TITLE);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_JOB_DESCRIPTION);
            int empNeedIndex = cursor.getColumnIndex(COLUMN_EMP_NEED);
            int skillsIndex = cursor.getColumnIndex(COLUMN_JOB_SKILLS);
            int minSalIndex = cursor.getColumnIndex(COLUMN_JOB_MIN_SAL);
            int maxSalIndex = cursor.getColumnIndex(COLUMN_JOB_MAX_SAL);
            int deadlineIndex = cursor.getColumnIndex(COLUMN_JOB_DEADLINE);
            int jobPostedById = cursor.getColumnIndex(COLUMN_POSTED_BY_USER_ID);

            do {
                String jobId = idIndex != -1 ? cursor.getString(idIndex) : "";
                String title = titleIndex != -1 ? cursor.getString(titleIndex) : "";
                String description = descriptionIndex != -1 ? cursor.getString(descriptionIndex) : "";
                String empNeed = empNeedIndex != -1 ? cursor.getString(empNeedIndex) : "";
                String skills = skillsIndex != -1 ? cursor.getString(skillsIndex) : "";
                String minSalary = minSalIndex != -1 ? cursor.getString(minSalIndex) : "";
                String maxSalary = maxSalIndex != -1 ? cursor.getString(maxSalIndex) : "";
                String deadline = deadlineIndex != -1 ? cursor.getString(deadlineIndex) : "";
                String jobPosted = jobPostedById != -1 ? cursor.getString(jobPostedById) : "";

                Job job = new Job(jobId, title, description, empNeed, skills, minSalary, maxSalary, deadline,jobPosted);
                jobList.add(job);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return jobList;
    }

    public List<Job> getJobsByUser(String userId) {
        List<Job> jobList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOBS, null, COLUMN_POSTED_BY_USER_ID + " = ?", new String[]{userId}, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_JOB_ID);
            int titleIndex = cursor.getColumnIndex(COLUMN_JOB_TITLE);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_JOB_DESCRIPTION);
            int empNeedIndex = cursor.getColumnIndex(COLUMN_EMP_NEED);
            int skillsIndex = cursor.getColumnIndex(COLUMN_JOB_SKILLS);
            int minSalIndex = cursor.getColumnIndex(COLUMN_JOB_MIN_SAL);
            int maxSalIndex = cursor.getColumnIndex(COLUMN_JOB_MAX_SAL);
            int deadlineIndex = cursor.getColumnIndex(COLUMN_JOB_DEADLINE);
            int jobPostedById = cursor.getColumnIndex(COLUMN_POSTED_BY_USER_ID);

            do {
                String jobId = idIndex != -1 ? cursor.getString(idIndex) : "";
                String title = titleIndex != -1 ? cursor.getString(titleIndex) : "";
                String description = descriptionIndex != -1 ? cursor.getString(descriptionIndex) : "";
                String empNeed = empNeedIndex != -1 ? cursor.getString(empNeedIndex) : "";
                String skills = skillsIndex != -1 ? cursor.getString(skillsIndex) : "";
                String minSalary = minSalIndex != -1 ? cursor.getString(minSalIndex) : "";
                String maxSalary = maxSalIndex != -1 ? cursor.getString(maxSalIndex) : "";
                String deadline = deadlineIndex != -1 ? cursor.getString(deadlineIndex) : "";
                String jobPosted = jobPostedById != -1 ? cursor.getString(jobPostedById) : "";

                Job job = new Job(jobId, title, description, empNeed, skills, minSalary, maxSalary, deadline, jobPosted);
                jobList.add(job);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return jobList;
    }



    public boolean deleteJob(String jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_JOBS, COLUMN_JOB_ID + " = ?", new String[]{jobId});
        db.close();
        return rowsAffected > 0;
    }

}
