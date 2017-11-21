package com.hamom.epamapp.data.local;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.hamom.epamapp.data.local.db.TodoContract.TodoEntry;
import com.hamom.epamapp.data.local.db.TodoContract.UserEntry;
import com.hamom.epamapp.data.models.Todo;
import com.hamom.epamapp.data.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamom on 16.11.17.
 */

public class LocalService extends Service {
    private ContentResolver mContentResolver;
    private Handler mWorkingHandler;
    private Handler mUIHandler;

    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    public interface LocalServiceCallback<T> {
        void onExecuted(T result);
    }

    @Override
    public void onCreate() {
        mContentResolver = getContentResolver();
        mUIHandler = new Handler();
        HandlerThread handlerThread = new HandlerThread("db_service_worker");
        handlerThread.start();
        Looper workerLooper = handlerThread.getLooper();
        mWorkingHandler = new Handler(workerLooper);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public void getUserByName(String name, LocalServiceCallback<User> callback) {
        Uri uri = UserEntry.CONTENT_URI;
        String selection = UserEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{name};

        getUser(callback, uri, selection, selectionArgs);
    }

    public void getUserById(long id, LocalServiceCallback<User> callback) {
        Uri uri = UserEntry.CONTENT_URI;
        String selection = UserEntry.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        getUser(callback, uri, selection, selectionArgs);
    }

    private void getUser(LocalServiceCallback<User> callback, Uri uri, String selection, String[] selectionArgs) {
        mWorkingHandler.post(() -> {
               Cursor cursor = mContentResolver.query(uri, null, selection, selectionArgs, null);
               User user = fetchUserFromCursor(cursor);
               mUIHandler.post(() -> callback.onExecuted(user));
           });
    }

    private User fetchUserFromCursor(Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndex(UserEntry._ID));
            String name1 = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_NAME));
            cursor.close();
            return new User(id, name1);
        } else {
            return null;
        }
    }

    public void saveUser(String name, LocalServiceCallback<Uri> callback) {
        Uri uri = UserEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME_NAME, name);

        mWorkingHandler.post(() -> {
            Uri newUri = mContentResolver.insert(uri, contentValues);
            mUIHandler.post(() -> callback.onExecuted(newUri));
        });
    }

    public void getTodo(long todoId, LocalServiceCallback<Todo> callback) {
        Uri uri = TodoEntry.CONTENT_URI;
        String selection = TodoEntry._ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(todoId)};

        mWorkingHandler.post(() -> {
            Cursor cursor = mContentResolver.query(uri, null, selection, selectionArgs, null);
            Todo[] todo = new Todo[1];
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                todo[0] = fetchTodoFromCursor(cursor);
            }
            mUIHandler.post(() -> callback.onExecuted(todo[0]));
        });
    }

    public void getAllUserTodos(long userId, LocalServiceCallback<List<Todo>> callback) {
        Uri uri = TodoEntry.CONTENT_URI;
        String selection = TodoEntry.COLUMN_NAME_USER_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        mWorkingHandler.post(() -> {
            Cursor cursor = mContentResolver.query(uri, null, selection, selectionArgs, null);
            List<Todo> todos = fetchTodoListFromCursor(cursor);
            mUIHandler.post(() -> callback.onExecuted(todos));
        });
    }

    private List<Todo> fetchTodoListFromCursor(Cursor cursor) {
        List<Todo> todos = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Todo todo = fetchTodoFromCursor(cursor);
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        return todos;
    }

    @NonNull
    private Todo fetchTodoFromCursor(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_DESCRIPTION));
        long time = cursor.getLong(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TIME));
        int priority = cursor.getInt(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_PRIORITY));
        long id = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));
        return new Todo(id, title, description, time, priority);
    }

    public void saveTodo(Todo todo, long userId, LocalServiceCallback<Uri> callback) {
        Uri uri = TodoEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoEntry.COLUMN_NAME_TITLE, todo.getTitle());
        contentValues.put(TodoEntry.COLUMN_NAME_DESCRIPTION, todo.getDescription());
        contentValues.put(TodoEntry.COLUMN_NAME_TIME, todo.getTime());
        contentValues.put(TodoEntry.COLUMN_NAME_PRIORITY, todo.getPriority());
        contentValues.put(TodoEntry.COLUMN_NAME_USER_ID, userId);

        mWorkingHandler.post(() -> {
            Uri newUri = mContentResolver.insert(uri, contentValues);
            mUIHandler.post(() -> callback.onExecuted(newUri));
        });
    }

    public void updateTodo(Todo todo, long userId, LocalServiceCallback<Integer> callback) {
        Uri uri = TodoEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoEntry.COLUMN_NAME_TITLE, todo.getTitle());
        contentValues.put(TodoEntry.COLUMN_NAME_DESCRIPTION, todo.getDescription());
        contentValues.put(TodoEntry.COLUMN_NAME_TIME, todo.getTime());
        contentValues.put(TodoEntry.COLUMN_NAME_PRIORITY, todo.getPriority());
        contentValues.put(TodoEntry.COLUMN_NAME_USER_ID, userId);

        String where = TodoEntry._ID;
        String[] whereArgs = new String[] { String.valueOf(todo.getId()) };

        mWorkingHandler.post(() -> {
            int updated = mContentResolver.update(uri, contentValues, where, whereArgs);
            mUIHandler.post(() -> callback.onExecuted(updated));
        });
    }
}
