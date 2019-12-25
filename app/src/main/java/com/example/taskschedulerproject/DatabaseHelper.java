package com.example.taskschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaskSchedDB";
    private static final String MAIN_BOARD_NAME = "UserBoard";
    private final String CHECK_LIST_TYPE = "CheckList";
    private final String NOTES_LIST_TYPE = "NotesList";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MAIN_BOARD_NAME + " (" +
                "Id INTEGER PRIMARY KEY," +
                "ListName TEXT UNIQUE," +
                "CreationDate TEXT," +
                "ListType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_BOARD_NAME);
        onCreate(db);
    }

    public boolean insertCheckList(String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("Date", date);
        contentValues.put("ListType", CHECK_LIST_TYPE);
        db.insertOrThrow(MAIN_BOARD_NAME, null, contentValues);
        db.execSQL("CREATE TABLE " + title + " (" +
                "Id INTEGER PRIMARY KEY," +
                "Title TEXT," +
                "Description TEXT," +
                "CreationDate TEXT," +
                "Priority INTEGER(3)," +
                "Duration TEXT," +
                "Deadline TEXT," +
                "Checked INTEGER)");
        return true;
    }

    public boolean insertNotesList(String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("Date", date);
        contentValues.put("ListType", NOTES_LIST_TYPE);
        db.insertOrThrow(MAIN_BOARD_NAME, null, contentValues);
        db.execSQL("CREATE TABLE " + title + " (" +
                "Id INTEGER PRIMARY KEY," +
                "Title TEXT," +
                "Description TEXT," +
                "CreationDate TEXT)");
        return true;
    }

    public boolean updateCheckList(Integer id, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("Date", date);
        contentValues.put("ListType", CHECK_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "id = ? ", new String[] {Integer.toString(id)});

        return true;
    }

    public boolean updateNoteList(Integer id, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("Date", date);
        contentValues.put("ListType", NOTES_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "id = ? ", new String[] {Integer.toString(id)});

        return true;
    }
}
