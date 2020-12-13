package com.safronov.courseworksapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safronov.courseworksapp.Models.CourseWork;
import com.safronov.courseworksapp.Models.FavoriteWork;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    private DatabaseContext databaseContext;
    private SQLiteDatabase db;
    private Cursor userCursor;

    public DatabaseOperations(Context context) {
        databaseContext = new DatabaseContext(context);
        db = databaseContext.getWritableDatabase();
    }

    // insert
    public void insertUser(String id, String username, String password, String token) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContext.USER_ID, id);
        cv.put(DatabaseContext.USER_NAME, username);
        cv.put(DatabaseContext.USER_PASS, password);
        cv.put(DatabaseContext.USER_TOKEN, token);
        db.insert(DatabaseContext.T_USERS, null, cv);
    }

    public void insertCourseWorks(List<CourseWork> courseWorks) {
        for (CourseWork cw : courseWorks) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContext.CRSWRKS_ID, cw.id);
            cv.put(DatabaseContext.CRSWRKS_NAME, cw.name);
            cv.put(DatabaseContext.CRSWRKS_DESC, cw.description);
            cv.put(DatabaseContext.CRSWRKS_FNAME, cw.file_name);
            db.insert(DatabaseContext.T_COURSEWORKS, null, cv);
        }
    }

    public void insertFavWork(int work_id) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContext.FAV_USER_ID, getUserId());
        cv.put(DatabaseContext.FAV_CRSWRK_ID, work_id);
        db.insert(DatabaseContext.T_FAVWORKS, null, cv);
    }

    public void insertFavWorks(List<FavoriteWork> favoriteWorks) {
        for (FavoriteWork fw : favoriteWorks) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseContext.FAV_ID, fw.id);
            cv.put(DatabaseContext.FAV_USER_ID, fw.username_Id);
            cv.put(DatabaseContext.FAV_CRSWRK_ID, fw.courseWork_Id);
            db.insert(DatabaseContext.T_FAVWORKS, null, cv);
        }
    }

    // get
    public String getUsername() {
        userCursor = db.rawQuery("select " + DatabaseContext.USER_NAME + " from " + DatabaseContext.T_USERS, null);
        userCursor.moveToFirst();
        return userCursor.getString(0);
    }

    public int getUserId() {
        userCursor = db.rawQuery("select " + DatabaseContext.USER_ID + " from " + DatabaseContext.T_USERS, null);
        userCursor.moveToFirst();
        return userCursor.getInt(0);
    }

    public String getToken() {
        userCursor = db.rawQuery("select " + DatabaseContext.USER_TOKEN + " from " + DatabaseContext.T_USERS, null);
        userCursor.moveToFirst();
        return userCursor.getString(0);
    }

    public CourseWork getCourseWork(int id) {

        userCursor = db.rawQuery("select * from " + DatabaseContext.T_COURSEWORKS + " where "
                + DatabaseContext.CRSWRKS_ID + " = " + id, null);

        userCursor.moveToFirst();
        String desciprion = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_DESC));
        String name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_NAME));
        String file_name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_FNAME));
        userCursor.close();

        return new CourseWork(id, name, desciprion, file_name);
    }

    public List<CourseWork> getCourseWorks() {

        List<CourseWork> courseWorks = new ArrayList<>();

        userCursor = db.rawQuery("select * from " + DatabaseContext.T_COURSEWORKS + " ORDER BY " + DatabaseContext.CRSWRKS_ID + " DESC ", null);

        userCursor.moveToFirst();

        while (!userCursor.isAfterLast()) {
            int id = userCursor.getInt(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_ID));
            String desciprion = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_DESC));
            String name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_NAME));
            String file_name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_FNAME));
            courseWorks.add(new CourseWork(id, name, desciprion, file_name));
            userCursor.moveToNext();
        }
        userCursor.close();
        return courseWorks;
    }

    public boolean isFavWorkExist(int work_id) {

        userCursor = db.query(DatabaseContext.T_FAVWORKS, null,
                DatabaseContext.FAV_CRSWRK_ID + " = " + work_id + " AND " +
                        DatabaseContext.FAV_USER_ID + " = " + getUserId(), null,
                null, null, null);
        userCursor.moveToFirst();

        try {
            return !userCursor.isNull(0);
        } catch (Exception e) {
            return false;
        }
    }

    public List<CourseWork> getFavWorks() {

        List<CourseWork> courseWorks = new ArrayList<>();

        userCursor = db.rawQuery("select * from " + DatabaseContext.T_COURSEWORKS + " where "
                + DatabaseContext.CRSWRKS_ID + " in (select "
                + DatabaseContext.FAV_CRSWRK_ID + " from " + DatabaseContext.T_FAVWORKS + " where "
                + DatabaseContext.FAV_USER_ID + " = " + getUserId() + ")" + " ORDER BY " + DatabaseContext.CRSWRKS_ID + " DESC", null);

        userCursor.moveToFirst();

        while (!userCursor.isAfterLast()) {
            int id = userCursor.getInt(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_ID));
            String desciprion = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_DESC));
            String name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_NAME));
            String file_name = userCursor.getString(userCursor.getColumnIndex(DatabaseContext.CRSWRKS_FNAME));
            courseWorks.add(new CourseWork(id, name, desciprion, file_name));
            userCursor.moveToNext();
        }
        userCursor.close();
        return courseWorks;
    }

    // delete
    public void deleteFavWork(int work_id) {
        db.delete(DatabaseContext.T_FAVWORKS, DatabaseContext.FAV_CRSWRK_ID + " = " + work_id, null);
    }

    // drop
    public void dropTable(String table) {
        db.execSQL("DELETE FROM " + table);
    }
}
