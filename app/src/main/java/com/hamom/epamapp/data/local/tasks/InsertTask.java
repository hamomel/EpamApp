package com.hamom.epamapp.data.local.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by hamom on 12.11.17.
 */

public class InsertTask extends AsyncTask<Void, Void, Uri> {
    private ContentResolver mContentResolver;
    private InsertCallback mCallback;
    private Uri mUri;
    private ContentValues mContentValues;

    public InsertTask(ContentResolver contentResolver, InsertCallback callback) {
        mContentResolver = contentResolver;
        mCallback = callback;
    }

    public void insert(Uri uri, ContentValues contentValues) {
        mUri = uri;
        mContentValues = contentValues;
        execute();
    }

    @Override
    protected Uri doInBackground(Void... voids) {
        return mContentResolver.insert(mUri, mContentValues);
    }

    @Override
    protected void onPostExecute(Uri uri) {
        mCallback.onDataInserted(uri);
    }

    public interface InsertCallback {
        void onDataInserted(Uri uri);
    }
}
