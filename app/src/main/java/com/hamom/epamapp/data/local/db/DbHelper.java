package com.hamom.epamapp.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.hamom.epamapp.data.local.db.TodoContract.TodoEntry;
import static com.hamom.epamapp.data.local.db.TodoContract.UserEntry;

/**
 * Created by hamom on 12.11.17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todo.db";
    private static int DB_VERSION = 1;

    private static final String CREATE_USER_SQL = "CREATE TABLE " +
            UserEntry.TABLE_NAME + " (" +
            UserEntry._ID + " INTEGER PRIMARY KEY," +
            UserEntry.COLUMN_NAME_NAME + " TEXT" +
            " )";

    private static final String CREATE_TODO_SQL = "CREATE TABLE " +
            TodoEntry.TABLE_NAME + " (" +
            TodoEntry._ID + " INTEGER PRIMARY KEY," +
            TodoEntry.COLUMN_NAME_TITLE + " TEXT," +
            TodoEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            TodoEntry.COLUMN_NAME_TIME + " INTEGER," +
            TodoEntry.COLUMN_NAME_PRIORITY + " INTEGER," +
            TodoEntry.COLUMN_NAME_USER_ID + " INTEGER," +
            " FOREIGN KEY (" + TodoEntry.COLUMN_NAME_USER_ID + ")" +
            " REFERENCES " + UserEntry.TABLE_NAME + "(" +
            UserEntry._ID + ") ON DELETE CASCADE" +
            " )";

    public DbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_SQL);
        sqLiteDatabase.execSQL(CREATE_TODO_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
