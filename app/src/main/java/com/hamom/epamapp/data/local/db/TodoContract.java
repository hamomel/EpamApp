package com.hamom.epamapp.data.local.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hamom on 12.11.17.
 */

public final class TodoContract {
    private static final String AUTHORITY = "content://com.hamom.epamapp.provider";
    private static final String MIME_TYPE_TABLE_PREFIX = "vnd.android.cursor.dir/vnd.com.hamom.epamapp.";
    private static final String MIME_TYPE_ITEM_PREFIX = "vnd.android.cursor.item/vnd.com.hamom.epamapp.";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

    public static class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String MIME_TYPE_ITEM = TodoContract.MIME_TYPE_ITEM_PREFIX + TABLE_NAME;
        public static final String MIME_TYPE_TABLE = TodoContract.MIME_TYPE_TABLE_PREFIX + TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.parse(TodoContract.AUTHORITY + "/" + TABLE_NAME);
    }

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todos";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String MIME_TYPE_ITEM = TodoContract.MIME_TYPE_ITEM_PREFIX + TABLE_NAME;
        public static final String MIME_TYPE_TABLE = TodoContract.MIME_TYPE_TABLE_PREFIX + TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.parse(TodoContract.AUTHORITY + "/" + TABLE_NAME);
    }
}
