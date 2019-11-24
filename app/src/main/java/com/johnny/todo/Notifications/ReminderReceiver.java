package com.johnny.todo.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.johnny.todo.MainActivity;
import com.johnny.todo.R;

import static com.johnny.todo.App.CHANNEL_ID;

public class ReminderReceiver extends BroadcastReceiver {

    NotificationManagerCompat NotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(MainActivity.Notification_title);
        String description = intent.getStringExtra(MainActivity.Notification_Description);

        NotificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        NotificationManager.notify(1, notification);
    }
}
