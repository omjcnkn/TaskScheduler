package com.example.taskschedulerproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
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
        contentValues.put("List", listName);
        contentValues.put("NoteTitle", title);
        contentValues.put("NoteDescription", description);
        contentValues.put("NoteDate", creationDate);

        db.insertOrThrow(NOTES_TABLE,null,contentValues);
        return true;
    }

    public boolean updateCheckList(String oldTitle, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeOldTitle = DatabaseUtils.sqlEscapeString(oldTitle);

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", CHECK_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "ListName =" + escapeOldTitle + "", null);

        return true;
    }

    public boolean updateNoteList(String oldTitle, String title, String date) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeOldTitle = DatabaseUtils.sqlEscapeString(oldTitle);

        ContentValues contentValues = new ContentValues();
        contentValues.put("ListName", title);
        contentValues.put("CreationDate", date);
        contentValues.put("ListType", NOTES_LIST_TYPE);

        db.update(MAIN_BOARD_NAME, contentValues, "ListName=" + escapeOldTitle + "", null);

        return true;
    }

    public boolean updateCheckListItem(String taskTitle, String listName,String title,String desc,
                                       String creationDate,int priority,String taskDuration,String taskDeadline, int taskChecked) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeTaskTitle = DatabaseUtils.sqlEscapeString(taskTitle);

        ContentValues contentValues = new ContentValues();
        contentValues.put("List", listName);
        contentValues.put("TaskTitle", title);
        contentValues.put("TaskDescription", desc);
        contentValues.put("TaskDate",creationDate);
        contentValues.put("TaskPriority",priority);
        contentValues.put("TaskDuration",taskDuration);
        contentValues.put("TaskDeadline",taskDeadline);
        contentValues.put("TaskChecked", taskChecked);

        db.update(TASKS_TABLE, contentValues, "TaskTitle=" + escapeTaskTitle + "", null);

        return true;
    }

    /* Not updated yet */
    public boolean updateNoteListItem(String noteTitle, String listName, String title, String description, String creationDate) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeNoteTitle = DatabaseUtils.sqlEscapeString(noteTitle);

        ContentValues contentValues = new ContentValues();
        contentValues.put("List",listName);
        contentValues.put("NoteTitle",title);
        contentValues.put("NoteDescription", description);
        contentValues.put("NoteDate", creationDate);

        db.update(NOTES_TABLE, contentValues, "NoteTitle=" + escapeNoteTitle + "", null);
        return true;
    }

    public Cursor getAllLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + MAIN_BOARD_NAME, null);

        return res;
    }

    public Cursor getCheckListItems(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String escapeListName = DatabaseUtils.sqlEscapeString(listName);

        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE + " WHERE List=" + escapeListName + "", null);

        return res;
    }

    public Cursor getNoteListItems(String listName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String escapeListName = DatabaseUtils.sqlEscapeString(listName);

        Cursor res = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE List=" + escapeListName + "", null);

        return res;
    }

    public Cursor getList(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String escapeName = DatabaseUtils.sqlEscapeString(name);

        Cursor res =  db.rawQuery( "SELECT * FROM " + MAIN_BOARD_NAME + " WHERE ListName=" + escapeName + "", null );
        return res;
    }

    public Cursor getTaskItem(String taskTitle) {
        SQLiteDatabase db = this.getReadableDatabase();

        String escapeTaskTitle = DatabaseUtils.sqlEscapeString(taskTitle);

        Cursor res = db.rawQuery("SELECT * FROM " + TASKS_TABLE + " WHERE TaskTitle=" + escapeTaskTitle + "", null);

        return res;
    }

    public Cursor getNoteItem(String noteTitle) {
        SQLiteDatabase db = this.getReadableDatabase();

        String escapeNoteTitle = DatabaseUtils.sqlEscapeString(noteTitle);

        Cursor res = db.rawQuery("SELECT * FROM " + NOTES_TABLE + " WHERE NoteTitle=" + escapeNoteTitle + "", null);

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

    public void removeList(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeItemName = DatabaseUtils.sqlEscapeString(itemName);

        db.delete(MAIN_BOARD_NAME, "ListName=" + escapeItemName + "", null);

        db.delete(TASKS_TABLE, "List=" + escapeItemName + "", null);
        db.delete(NOTES_TABLE, "List=" + escapeItemName + "", null);
    }

    public void removeCheckListItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeItemName = DatabaseUtils.sqlEscapeString(itemName);

        db.delete(TASKS_TABLE, "TaskTitle=" + escapeItemName + "", null);
    }

    public void removeNoteListItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String escapeItemName = DatabaseUtils.sqlEscapeString(itemName);

        db.delete(NOTES_TABLE, "NoteTitle=" + escapeItemName + "", null);
    }
}
