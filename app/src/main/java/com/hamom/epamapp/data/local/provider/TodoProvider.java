package com.hamom.epamapp.data.local.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hamom.epamapp.data.local.db.DbHelper;
import com.hamom.epamapp.data.local.db.TodoContract;

/**
 * Created by hamom on 12.11.17.
 */

public class TodoProvider extends ContentProvider {
    private DbHelper mDbHelper;
    private static final UriMatcher mUriMatcher;
    private static final int USERS_URI = 1;
    private static final int USERS_ID_URI = 2;
    private static final int TODO_URI = 3;
    private static final int TODO_ID_URI = 4;


    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(TodoContract.CONTENT_URI.getAuthority(), TodoContract.User.TABLE_NAME, USERS_URI);
        mUriMatcher.addURI(TodoContract.CONTENT_URI.getAuthority(), TodoContract.User.TABLE_NAME + "/#", USERS_ID_URI);
        mUriMatcher.addURI(TodoContract.CONTENT_URI.getAuthority(), TodoContract.Todo.TABLE_NAME, TODO_ID_URI);
        mUriMatcher.addURI(TodoContract.CONTENT_URI.getAuthority(), TodoContract.User.TABLE_NAME + "/#", TODO_ID_URI);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName;
        switch (mUriMatcher.match(uri)) {
            case USERS_URI:
                tableName = TodoContract.User.TABLE_NAME;
                    if (TextUtils.isEmpty(sortOrder)){
                        sortOrder = TodoContract.User.COLUMN_NAME_NAME + " ASC";
                    }
                break;
            case USERS_ID_URI:
                tableName = TodoContract.User.TABLE_NAME;
                selection = TodoContract.User._ID + " = " + uri.getLastPathSegment();
                 break;
            case TODO_URI:
                tableName = TodoContract.Todo.TABLE_NAME;
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = TodoContract.Todo.COLUMN_NAME_TIME + " ASC";
                }
                break;
            case TODO_ID_URI:
                tableName = TodoContract.Todo.TABLE_NAME;
                selection = TodoContract.Todo._ID + " = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Uri " + uri.getPath() + " doesn't match any known path");
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs,
                null, null, sortOrder);
        db.close();
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case USERS_URI:
                return TodoContract.User.MIME_TYPE_TABLE;
            case USERS_ID_URI:
                return TodoContract.User.MIME_TYPE_ITEM;
            case TODO_URI:
                return TodoContract.Todo.MIME_TYPE_TABLE;
            case TODO_ID_URI:
                return TodoContract.Todo.MIME_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Uri " + uri.getPath() + " doesn't match any known path");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String tableName = resolveTableName(uri);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(tableName, null, contentValues);
        db.close();
        return ContentUris.withAppendedId(uri, rowId);
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = resolveTableName(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.delete(tableName, selection, selectionArgs);
        db.close();
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = resolveTableName(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.update(tableName, contentValues, selection, selectionArgs);
        db.close();
        return count;
    }

    private String resolveTableName(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case USERS_URI:
                return TodoContract.User.TABLE_NAME;
            case TODO_URI:
                return TodoContract.Todo.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Uri '" + uri.getPath() + "' doesn't match any known path");
        }
    }
}
