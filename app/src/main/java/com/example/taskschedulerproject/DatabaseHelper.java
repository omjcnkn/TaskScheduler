package com.example.taskschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaskSchedDB";
    private static final String MAIN_BOARD_NAME = "UserBoard";
    private static final String TASKS_TABLE = "Tasks";
    private static final String NOTES_TABLE = "Notes";
    public static final String CHECK_LIST_TYPE = "CheckList";
    public static final String NOTES_LIST_TYPE = "NotesList";

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

        db.execSQL("CREATE TABLE " + TASKS_TABLE + " ("+
                "Id INTEGER PRIMARY KEY," +
                "List TEXT,"+
                "TaskTitle TEXT UNIQUE,"+
                "TaskDescription TEXT,"+
                "TaskDate TEXT,"+
                "TaskPriority INTEGER(1),"+
                "TaskDuration TEXT,"+
                "TaskDeadline TEXT,"+
                "TaskChecked INTEGER(1))"
        );

        db.execSQL("CREATE TABLE " + NOTES_TABLE + " ("+
                "Id INTEGER PRIMARY KEY," +
                "List TEXT,"+
                "NoteTitle TEXT UNIQUE,"+
                "NoteDescription TEXT,"+
                "NoteDate TEXT)"
        );

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
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", CHECK_LIST_TYPE);
        db.insertOrThrow(MAIN_BOARD_NAME, null, contentValues);
        return true;
    }

    public boolean insertNotesList(String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", NOTES_LIST_TYPE);
        db.insertOrThrow(MAIN_BOARD_NAME, null, contentValues);
        return true;
    }

    public boolean insertTaskItem(String listName,String title,String desc,String creationDate,int priority,String taskDuration,String taskDeadline) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("List",listName);
        contentValues.put("TaskTitle",title);
        contentValues.put("TaskDescription",desc);
        contentValues.put("TaskDate",creationDate);
        contentValues.put("TaskPriority",priority);
        contentValues.put("TaskDuration",taskDuration);
        contentValues.put("TaskDeadline",taskDeadline);
        contentValues.put("TaskChecked",0);
        db.insertOrThrow(TASKS_TABLE,null,contentValues);
        return true;
    }

    public boolean insertNoteItem(String listName, String title, String description, String creationDate) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("List",listName);
        contentValues.put("NoteTitle",title);
        contentValues.put("NoteDescription", description);
        contentValues.put("NoteDate", creationDate);

        db.insertOrThrow(NOTES_TABLE,null,contentValues);
        return true;
    }

    public boolean updateCheckList(Integer id, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", CHECK_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "id = ? ", new String[] {Integer.toString(id)});

        return true;
    }

    public boolean updateNoteList(Integer id, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", NOTES_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "id = ? ", new String[] {Integer.toString(id)});

        return true;
    }

    public boolean updateCheckListItem(String taskTitle, String listName,String title,String desc,
                                       String creationDate,int priority,String taskDuration,String taskDeadline, int taskChecked) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("List",listName);
        contentValues.put("TaskTitle",title);
        contentValues.put("TaskDescription",desc);
        contentValues.put("TaskDate",creationDate);
        contentValues.put("TaskPriority",priority);
        contentValues.put("TaskDuration",taskDuration);
        contentValues.put("TaskDeadline",taskDeadline);
        contentValues.put("TaskChecked", taskChecked);

        db.update(TASKS_TABLE, contentValues, "TaskTitle='" + taskTitle + "'", null);

        return true;
    }

    public boolean updateNoteListItem(String noteTitle, String listName, String title, String description, String creationDate) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("List",listName);
        contentValues.put("NoteTitle",title);
        contentValues.put("NoteDescription", description);
        contentValues.put("NoteDate", creationDate);

        db.update(NOTES_TABLE, contentValues, "NoteTitle=" + noteTitle, null);
        return true;
    }

    public Cursor getAllLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + MAIN_BOARD_NAME, null);

        return res;
    }

    public Cursor getCheckListItems(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE + " WHERE List='" + listName + "'", null);

        return res;
    }

    public Cursor getNoteListItems(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE List='" + listName + "'", null);

        return res;
    }

    public Cursor getList(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + MAIN_BOARD_NAME + " WHERE ListName='"+name+"'", null );
        return res;
    }

    public Cursor getTaskItem(String taskTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE + " WHERE TaskTitle='" + taskTitle + "'", null);

        return res;
    }

    public Cursor getNoteItem(String noteTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE TaskTitle='" + noteTitle + "'", null);

        return res;
    }

    public void deleteAllLists() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MAIN_BOARD_NAME);
    }

    public void deleteAllTaskItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TASKS_TABLE);
    }

    public void deleteAllNoteItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + NOTES_TABLE);
    }

    public void removeList(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MAIN_BOARD_NAME, "Id=" + id + "", null);
    }
}
