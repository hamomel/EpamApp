package com.hamom.epamapp.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.hamom.epamapp.data.local.db.TodoContract.Todo;
import static com.hamom.epamapp.data.local.db.TodoContract.User;

/**
 * Created by hamom on 12.11.17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todo.db";
    private static int DB_VERSION = 1;

    private static final String CREATE_USER_SQL = "CREATE TABLE " +
            User.TABLE_NAME + " (" +
            User.COLUMN_NAME_NAME + " TEXT" +
            " )";

    private static final String CREATE_TODO_SQL = "CREATE TABLE " +
            Todo.TABLE_NAME + " (" +
            Todo.COLUMN_NAME_TITLE + " TEXT," +
            Todo.COLUMN_NAME_DESCRIPTION + " TEXT," +
            Todo.COLUMN_NAME_TIME + " INTEGER," +
            Todo.COLUMN_NAME_PRIORITY + " INTEGER," +
            Todo.COLUMN_NAME_USER_ID + " INTEGER," +
            " FOREIGN KEY (" + Todo.COLUMN_NAME_USER_ID + ")" +
            " REFERENCES " + User.TABLE_NAME + "(" +
            User._ID + ") ON DELETE CASCADE" +
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
