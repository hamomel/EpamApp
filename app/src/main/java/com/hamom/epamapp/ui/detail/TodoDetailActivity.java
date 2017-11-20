package com.hamom.epamapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hamom.epamapp.ui.base.OneFragmentActivity;

/**
 * Created by hamom on 20.11.17.
 */

public class TodoDetailActivity extends OneFragmentActivity {
    private static final String EXTRA_TODO_ID = "extra_todo_id";

    public static Intent getIntent(Context context, long todoId) {
        Intent intent = new Intent(context, TodoDetailActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }
    @Override
    protected Fragment getFragment() {
        long todoId = getIntent().getLongExtra(EXTRA_TODO_ID, -1);
        return TodoDetailFragment.newInstance(todoId);
    }

    public static Intent getIntent(Context context) {
        return getIntent(context, -1);
    }
}
