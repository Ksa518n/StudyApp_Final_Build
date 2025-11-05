package com.studyapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudyAppDB.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_FILES = "files";
    public static final String TABLE_TASKS = "tasks";

    // Common Columns
    public static final String COLUMN_ID = "_id";

    // SUBJECTS Table Columns
    public static final String COLUMN_SUBJECT_NAME = "name";
    public static final String COLUMN_SUBJECT_GOAL = "goal";

    // NOTES Table Columns
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_SUBJECT_ID = "subject_id";

    // FILES Table Columns
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_FILE_PATH = "file_path"; // Internal path for security
    public static final String COLUMN_FILE_SUBJECT_ID = "subject_id";

    // TASKS Table Columns
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DUE_DATE = "due_date"; // Unix timestamp
    public static final String COLUMN_TASK_TYPE = "type"; // e.g., "Exam", "Homework"
    public static final String COLUMN_TASK_IS_COMPLETED = "is_completed";

    // SQL statement for creating the SUBJECTS table
    private static final String CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SUBJECT_NAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SUBJECT_GOAL + " TEXT"
            + ");";

    // SQL statement for creating the NOTES table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTE_CONTENT + " TEXT NOT NULL, "
            + COLUMN_NOTE_SUBJECT_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_NOTE_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
            + ");";

    // SQL statement for creating the FILES table
    private static final String CREATE_TABLE_FILES = "CREATE TABLE " + TABLE_FILES + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FILE_NAME + " TEXT NOT NULL, "
            + COLUMN_FILE_PATH + " TEXT NOT NULL, "
            + COLUMN_FILE_SUBJECT_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_FILE_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
            + ");";
            
    // SQL statement for creating the TASKS table
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_TITLE + " TEXT NOT NULL, "
            + COLUMN_TASK_DUE_DATE + " INTEGER NOT NULL, "
            + COLUMN_TASK_TYPE + " TEXT, "
            + COLUMN_TASK_IS_COMPLETED + " INTEGER DEFAULT 0" // 0 for false, 1 for true
            + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUBJECTS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_FILES);
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple drop and recreate strategy for initial development
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Enable foreign key constraints
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
