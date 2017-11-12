package com.hamom.epamapp.data.local.tasks;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by hamom on 12.11.17.
 */

public class DeleteTask extends AsyncTask<Void, Void, Integer> {
    private ContentResolver mContentResolver;
    private DeleteCallback mCallback;
    private Uri mUri;
    private String mSelection;
    private String[] mSelectionArgs;

    public DeleteTask(ContentResolver contentResolver, DeleteCallback callback) {
        mContentResolver = contentResolver;
        mCallback = callback;
    }

    public void delete(Uri uri, String selection, String[] selectionArgs) {
        mUri = uri;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        execute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return mContentResolver.delete(mUri, mSelection, mSelectionArgs);
    }

    @Override
    protected void onPostExecute(Integer count) {
        mCallback.onDataDeleted(count);
    }

    public interface DeleteCallback {
        void onDataDeleted(Integer count);
    }
}
