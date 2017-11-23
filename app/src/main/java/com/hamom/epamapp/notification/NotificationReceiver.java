package com.hamom.epamapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hamom.epamapp.BuildConfig;
import com.hamom.epamapp.R;
import com.hamom.epamapp.ui.detail.TodoDetailActivity;
import com.hamom.epamapp.ui.todo_list.TodoListActivity;
import com.hamom.epamapp.utils.ConstantManager;


/**
 * Created by hamom on 23.11.17.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String EXTRA_TODO_ID = "extra_todo_id";
    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_DESCRIPTION = "extra_description";

    public static Intent getNewIntent(Context context, long todoId, long userId, String title, String description) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent detailIntent = TodoDetailActivity.getIntent(context, intent.getLongExtra(EXTRA_USER_ID, -1),
                intent.getLongExtra(EXTRA_TODO_ID, -1));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TodoDetailActivity.class);
        stackBuilder.addParentStack(TodoListActivity.class);
        stackBuilder.addNextIntent(detailIntent);
        stackBuilder.editIntentAt(0).putExtra(TodoListActivity.EXTRA_USER_ID,
                intent.getLongExtra(EXTRA_USER_ID, -1));

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(intent.getStringExtra(EXTRA_TITLE))
                .setContentText(intent.getStringExtra(EXTRA_DESCRIPTION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = ((NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE));
        notificationManager.notify(0, builder.build());

    }
}
