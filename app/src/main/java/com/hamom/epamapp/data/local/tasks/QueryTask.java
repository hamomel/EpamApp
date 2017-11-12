package com.hamom.epamapp.data.local.tasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by hamom on 12.11.17.
 */

public class QueryTask extends AsyncTask<Object, Void, Cursor> {
    private ContentResolver contentResolver;
    private QueryCallback callback;
    private Uri uri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    public QueryTask(ContentResolver contentResolver, QueryCallback callback) {
        this.contentResolver = contentResolver;
        this.callback = callback;
    }

    public void query(Uri uri, String[] projection,
                      String selection, String[] selectionArgs, String sortOrder) {
        this.uri = uri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;

        execute();
    }

    @Override
    protected Cursor doInBackground(Object... objects) {
        return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        callback.onDataLoaded(cursor);
    }

    public interface QueryCallback {
        void onDataLoaded(Cursor cursor);
    }
}
