package com.studyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.studyapp.models.SubjectModel;
import com.studyapp.models.TaskModel;
import com.studyapp.models.NoteModel;
import com.studyapp.models.FileModel;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private DatabaseHelper dbHelper;

    public AppRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // --- SUBJECTS Operations ---

    public long addSubject(SubjectModel subject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_GOAL, subject.getGoal());

        long id = db.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
        db.close();
        return id;
    }

    public List<SubjectModel> getAllSubjects() {
        List<SubjectModel> subjectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SUBJECTS + " ORDER BY " + DatabaseHelper.COLUMN_SUBJECT_NAME + " ASC";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubjectModel subject = new SubjectModel();
                subject.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                subject.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
                subject.setGoal(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_GOAL)));
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subjectList;
    }
    
    public int deleteSubject(long subjectId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Because of "ON DELETE CASCADE" in DatabaseHelper, deleting the subject will automatically delete
        // all associated notes and files.
        int rowsAffected = db.delete(DatabaseHelper.TABLE_SUBJECTS, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(subjectId)});
        db.close();
        return rowsAffected;
    }

    // --- TASKS Operations (Initial) ---

    public long addTask(TaskModel task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK_TITLE, task.getTitle());
        values.put(DatabaseHelper.COLUMN_TASK_DUE_DATE, task.getDueDate());
        values.put(DatabaseHelper.COLUMN_TASK_TYPE, task.getType());
        values.put(DatabaseHelper.COLUMN_TASK_IS_COMPLETED, task.isCompleted() ? 1 : 0);

        long id = db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public List<TaskModel> getUpcomingTasks() {
        List<TaskModel> taskList = new ArrayList<>();
        // Select all incomplete tasks, ordered by due date ascending
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_TASKS + " WHERE " + DatabaseHelper.COLUMN_TASK_IS_COMPLETED + " = 0"
                + " ORDER BY " + DatabaseHelper.COLUMN_TASK_DUE_DATE + " ASC";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE)));
                task.setDueDate(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DUE_DATE)));
                task.setType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TYPE)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_IS_COMPLETED)) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
    
    // --- FILES Operations ---

    public long addFile(FileModel file) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FILE_NAME, file.getFileName());
        values.put(DatabaseHelper.COLUMN_FILE_PATH, file.getFilePath());
        values.put(DatabaseHelper.COLUMN_FILE_SUBJECT_ID, file.getSubjectId());

        long id = db.insert(DatabaseHelper.TABLE_FILES, null, values);
        db.close();
        return id;
    }

    public List<FileModel> getFilesBySubject(long subjectId) {
        List<FileModel> fileList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FILES + " WHERE " + DatabaseHelper.COLUMN_FILE_SUBJECT_ID + " = " + subjectId;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FileModel file = new FileModel();
                file.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                file.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_NAME)));
                file.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_PATH)));
                file.setSubjectId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_SUBJECT_ID)));
                fileList.add(file);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fileList;
    }

    public int deleteFile(long fileId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TABLE_FILES, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(fileId)});
        db.close();
        return rowsAffected;
    }

    // --- NOTES Operations ---

    public long addNote(NoteModel note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE_CONTENT, note.getContent());
        values.put(DatabaseHelper.COLUMN_NOTE_SUBJECT_ID, note.getSubjectId());

        long id = db.insert(DatabaseHelper.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public NoteModel getNoteBySubjectId(long subjectId) {
        NoteModel note = null;
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NOTES + " WHERE " + DatabaseHelper.COLUMN_NOTE_SUBJECT_ID + " = " + subjectId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            note = new NoteModel();
            note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_CONTENT)));
            note.setSubjectId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_SUBJECT_ID)));
        }
        cursor.close();
        db.close();
        return note;
    }

    public int updateNote(NoteModel note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE_CONTENT, note.getContent());

        int rowsAffected = db.update(DatabaseHelper.TABLE_NOTES, values, DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rowsAffected;
    }

    // --- SEARCH Operations ---

    public List<Object> search(String query) {
        List<Object> results = new ArrayList<>();
        String likeQuery = "%" + query + "%";

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Search Subjects (by name)
        String subjectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SUBJECTS + " WHERE " + DatabaseHelper.COLUMN_SUBJECT_NAME + " LIKE ?";
        Cursor subjectCursor = db.rawQuery(subjectQuery, new String[]{likeQuery});
        if (subjectCursor.moveToFirst()) {
            do {
                SubjectModel subject = new SubjectModel();
                subject.setId(subjectCursor.getLong(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                subject.setName(subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
                subject.setGoal(subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_GOAL)));
                results.add(subject);
            } while (subjectCursor.moveToNext());
        }
        subjectCursor.close();

        // 2. Search Notes (by content)
        String noteQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NOTES + " WHERE " + DatabaseHelper.COLUMN_NOTE_CONTENT + " LIKE ?";
        Cursor noteCursor = db.rawQuery(noteQuery, new String[]{likeQuery});
        if (noteCursor.moveToFirst()) {
            do {
                NoteModel note = new NoteModel();
                note.setId(noteCursor.getLong(noteCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                note.setContent(noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_CONTENT)));
                note.setSubjectId(noteCursor.getLong(noteCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_SUBJECT_ID)));
                results.add(note);
            } while (noteCursor.moveToNext());
        }
        noteCursor.close();

        // 3. Search Files (by file_name)
        String fileQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FILES + " WHERE " + DatabaseHelper.COLUMN_FILE_NAME + " LIKE ?";
        Cursor fileCursor = db.rawQuery(fileQuery, new String[]{likeQuery});
        if (fileCursor.moveToFirst()) {
            do {
                FileModel file = new FileModel();
                file.setId(fileCursor.getLong(fileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                file.setFileName(fileCursor.getString(fileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_NAME)));
                file.setFilePath(fileCursor.getString(fileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_PATH)));
                file.setSubjectId(fileCursor.getLong(fileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FILE_SUBJECT_ID)));
                results.add(file);
            } while (fileCursor.moveToNext());
        }
        fileCursor.close();

        db.close();
        return results;
    }

    // Placeholder for other operations (Update, Delete)
    // Will be implemented in later phases.
}
