package com.johnny.todo.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.johnny.todo.Activities.MainActivity;
import com.johnny.todo.R;

import static com.johnny.todo.Activities.App.CHANNEL_ID;

public class ReminderReceiver extends BroadcastReceiver {

    NotificationManagerCompat NotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(MainActivity.Notification_Title);
        String description = intent.getStringExtra(MainActivity.Notification_Description);
        int id = intent.getIntExtra(MainActivity.Notification_Id, -1);

        if(id == -1) return;

        NotificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        NotificationManager.notify(id, notification);
    }
}
