package com.hamom.epamapp.ui.todo_list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hamom.epamapp.ui.base.OneFragmentActivity;

/**
 * Created by hamom on 03.11.17.
 */

public class TodoListActivity extends OneFragmentActivity {
    public static final String EXTRA_USER_ID = "extra_user_id";
    @Override
    protected Fragment getFragment() {
        long userId = getIntent().getLongExtra(EXTRA_USER_ID, -1);
        return TodoListFragment.newInstance(userId);
    }

    public static Intent getNewIntent(Context context, long id) {
        Intent intent = new Intent(context, TodoListActivity.class);
        intent.putExtra(EXTRA_USER_ID, id);
        return intent;
    }
}
