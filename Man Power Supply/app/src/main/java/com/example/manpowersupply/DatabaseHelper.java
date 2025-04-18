package com.example.manpowersupply;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }



    public Cursor getCurrentUser(Context context, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " = ?";
        Log.d("DatabaseHelper", "Query: " + query);
        Log.d("DatabaseHelper", "User ID: " + userId);
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor != null) {
            Log.d("DatabaseHelper", "Number of rows: " + cursor.getCount());
        } else {
            Log.d("DatabaseHelper", "Cursor is null");
        }

        return cursor;
    }


    public String addUser(String userName, String userEmail, String userPhone, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String userId = generateRandomID();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, userId);
        values.put(COLUMN_USERNAME, userName);
        values.put(COLUMN_EMAIL, userEmail);
        values.put(COLUMN_PHONE, userPhone);
        values.put(COLUMN_PASSWORD, userPassword);

        db.insert(TABLE_USERS, null, values);
        db.close();
        return userId;
    }
    public boolean updateUser(String userId, String userName, String userEmail, String userPhone, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userName);
        values.put(COLUMN_EMAIL, userEmail);
        values.put(COLUMN_PHONE, userPhone);
        values.put(COLUMN_PASSWORD, userPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{userId});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, COLUMN_ID + " = ?", new String[]{userId});
        db.close();
        return rowsAffected > 0;
    }

    private String generateRandomID() {
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789";
        int idLen = 10;
        Random random = new Random();

        StringBuilder randomID = new StringBuilder(idLen);
        for (int i = 0; i < idLen; i++) {
            int randomIndex = random.nextInt(charSet.length());
            randomID.append(charSet.charAt(randomIndex));
        }

        return randomID.toString();
    }

    public String checkUser(String usernameOrEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_USERNAME + " = ? OR " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{usernameOrEmail, usernameOrEmail, password}, null, null, null);

        String userId = null;

        if (cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            if (userIdIndex != -1) {
                userId = cursor.getString(userIdIndex);
            }

        }

        cursor.close();
        db.close();

        return userId;
    }



}
