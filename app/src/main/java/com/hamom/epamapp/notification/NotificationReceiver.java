package com.hamom.epamapp.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    public static final String GROUP_KEY = "todo_group";
    public static final int SUMMARY_ID = 111;
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
        Notification notification = getNotification(context, intent);
        int notificationId = ((int) intent.getLongExtra(EXTRA_TODO_ID, -1));

        NotificationManager manager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        //noinspection ConstantConditions
        manager.notify(notificationId, notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Notification summary = getSummary(context, intent);
            manager.notify(SUMMARY_ID, summary);
        }
    }

    private Notification getSummary(Context context, Intent intent) {
        PendingIntent summaryIntent = getSummaryIntent(context, intent);
        return  new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.some_new_todos))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(summaryIntent)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .build();
    }

    private PendingIntent getSummaryIntent(Context context, Intent intent) {
        Intent listIntent = TodoListActivity.getNewIntent(context, intent.getLongExtra(EXTRA_USER_ID, -1));

        return PendingIntent.getActivity(context, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification getNotification(Context context, Intent intent) {
        PendingIntent pendingIntent = getPendingIntent(context, intent);

        return new NotificationCompat.Builder(context)
                .setContentTitle(intent.getStringExtra(EXTRA_TITLE))
                .setContentText(intent.getStringExtra(EXTRA_DESCRIPTION))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY)
                .build();
    }

    private PendingIntent getPendingIntent(Context context, Intent intent) {
        long userId = intent.getLongExtra(EXTRA_USER_ID, -1);
        long todoId = intent.getLongExtra(EXTRA_TODO_ID, -1);
        Intent detailIntent = TodoDetailActivity.getIntent(context, userId, todoId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context)
                .addParentStack(TodoDetailActivity.class)
                .addParentStack(TodoListActivity.class)
                .addNextIntent(detailIntent);
        stackBuilder.editIntentAt(0).putExtra(TodoListActivity.EXTRA_USER_ID, userId);

        int requestCode = ((int) todoId);
        return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
