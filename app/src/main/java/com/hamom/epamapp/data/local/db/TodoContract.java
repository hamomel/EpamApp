package com.hamom.epamapp.data.local.db;

import android.provider.BaseColumns;

/**
 * Created by hamom on 12.11.17.
 */

public final class TodoContract {
    public static final String CONTENT_URI = "com.hamom.epamapp";

    static class User implements BaseColumns{
        static final String TABLE_NAME = "users";
        static final String COLUMN_NAME_NAME = "name";
    }

    static class Todo implements BaseColumns{
        static final String TABLE_NAME = "toods";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_DESCRIPTION = "description";
        static final String COLUMN_NAME_TIME = "time";
        static final String COLUMN_NAME_PRIORITY = "priority";
        static final String COLUMN_NAME_USER_ID = "user_id";
    }
}
