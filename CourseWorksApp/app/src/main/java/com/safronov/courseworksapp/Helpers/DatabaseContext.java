package com.safronov.courseworksapp.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseContext extends SQLiteOpenHelper {

    public static final String T_USERS = "Users";
    public static final String T_COURSEWORKS = "CourseWorks";
    public static final String T_FAVWORKS = "FavoriteWorks";
    static final String USER_ID = "id";
    static final String USER_NAME = "username";
    static final String USER_PASS = "password";
    static final String USER_ROLE = "role";
    static final String USER_TOKEN = "token";
    static final String CRSWRKS_ID = "id";
    static final String CRSWRKS_NAME = "name";
    static final String CRSWRKS_DESC = "description";
    static final String CRSWRKS_FILE = "file";
    static final String CRSWRKS_FNAME = "file_name";
    static final String FAV_ID = "id";
    static final String FAV_USER_ID = "user_id";
    static final String FAV_CRSWRK_ID = "coursework_id";
    private static final String DB_NAME = "courseworks_app.db";
    private static final int SCHEMA = 3;

    DatabaseContext(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_USERS + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + USER_NAME + " TEXT,"
                + USER_PASS + " TEXT,"
                + USER_ROLE + " TEXT, "
                + USER_TOKEN + " TEXT "
                + ");");

        db.execSQL("CREATE TABLE " + T_COURSEWORKS + " ("
                + CRSWRKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + CRSWRKS_NAME + " TEXT,"
                + CRSWRKS_DESC + " TEXT,"
                + CRSWRKS_FILE + " TEXT,"
                + CRSWRKS_FNAME + " TEXT "
                + ");");

        db.execSQL("CREATE TABLE " + T_FAVWORKS + " ("
                + FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + FAV_USER_ID + " INTEGER,"
                + FAV_CRSWRK_ID + " INTEGER "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + T_COURSEWORKS);
        db.execSQL("DROP TABLE IF EXISTS " + T_FAVWORKS);
        onCreate(db);
    }


}

