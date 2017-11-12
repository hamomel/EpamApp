package com.hamom.epamapp.data.local.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by hamom on 12.11.17.
 */

public class UpdateTask extends AsyncTask<Void, Void, Integer> {
    private ContentResolver mContentResolver;
    private UpdateCallback mCallback;
    private Uri mUri;
    private ContentValues mContentValues;
    private String mSelection;
    private String[] mSelectionArgs;

    public UpdateTask(ContentResolver contentResolver, UpdateCallback callback) {
        mContentResolver = contentResolver;
        mCallback = callback;
    }

    public void update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        mUri = uri;
        mContentValues = contentValues;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        execute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return mContentResolver.update(mUri, mContentValues, mSelection, mSelectionArgs);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        mCallback.onUpdated(integer);
    }

    public interface UpdateCallback<Result> {
        void onUpdated(Result count);
    }
}
