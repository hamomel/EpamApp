package com.hamom.epamapp.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.hamom.epamapp.BuildConfig;
import com.hamom.epamapp.data.local.db.TodoContract.*;
import com.hamom.epamapp.data.local.tasks.InsertTask;
import com.hamom.epamapp.data.local.tasks.QueryTask;
import com.hamom.epamapp.data.models.Todo;
import com.hamom.epamapp.data.models.User;
import com.hamom.epamapp.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamom on 12.11.17.
 */

public class ProviderHelper {
    private static String TAG = ConstantManager.TAG_PREFIX + "ProviderHelper: ";
    private ContentResolver mContentResolver;

    public ProviderHelper(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public void getUserByName(String name, HelperCallback callback) {
        Uri uri = UserEntry.CONTENT_URI;
        String selection = UserEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{name};

        getUser(callback, uri, selection, selectionArgs);
    }

    public void getUserById(long id, HelperCallback callback) {
        Uri uri = UserEntry.CONTENT_URI;
        String selection = UserEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        getUser(callback, uri, selection, selectionArgs);
    }

    private void getUser(HelperCallback callback, Uri uri, String selection, String[] selectionArgs) {
        QueryTask task = new QueryTask(mContentResolver, cursor -> fetchUserFromCursor(callback, cursor));
        task.query(uri, null, selection, selectionArgs, null);
    }

    private void fetchUserFromCursor(HelperCallback callback, Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndex(UserEntry._ID));
            String name1 = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_NAME));
            cursor.close();
            User user = new User(id, name1);
            if (BuildConfig.DEBUG) Log.d(TAG, "fetchUserFromCursor: " + user.getId() + " " + user.getName());


            callback.onExecuted(user);
        } else {
            callback.onExecuted(null);
        }
    }

    public void saveUser(String name, HelperCallback callback) {
        Uri uri = UserEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME_NAME, name);

        InsertTask task = new InsertTask(mContentResolver, callback::onExecuted);
        task.insert(uri, contentValues);
    }

    public void getAllUserTodos(long userId, HelperCallback callback) {
        Uri uri = TodoEntry.CONTENT_URI;
        String selection = TodoEntry.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        QueryTask task = new QueryTask(mContentResolver, cursor -> fetchTodoListFromCursor(callback, cursor));
        task.query(uri, null, selection, selectionArgs, null);
    }

    private void fetchTodoListFromCursor(HelperCallback callback, Cursor cursor) {
        List<Todo> todos = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
              String title = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TITLE));
              String description = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_DESCRIPTION));
              long time = cursor.getLong(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TIME));
              int priority = cursor.getInt(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_PRIORITY));
              long id = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));
              Todo todo = new Todo(id, title, description, time, priority);
              todos.add(todo);
            } while (cursor.moveToNext());
        }
        callback.onExecuted(todos);
    }

    public interface HelperCallback<T> {
        void onExecuted(T result);
    }
}
