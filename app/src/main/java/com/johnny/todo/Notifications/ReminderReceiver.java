package com.johnny.todo.Notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.johnny.todo.Activities.MainActivity;
import com.johnny.todo.R;

import static com.johnny.todo.Activities.App.CHANNEL_ID;
import static com.johnny.todo.Activities.MainActivity.Notification_Minutes;

public class ReminderReceiver extends BroadcastReceiver {

    NotificationManagerCompat NotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(MainActivity.Notification_Title);
        String description = intent.getStringExtra(MainActivity.Notification_Description);
        int id = intent.getIntExtra(MainActivity.Notification_Id, -1);

        if(id == -1) return;

        Intent action10 = new Intent(context, SnoozeReceiver.class);
        Intent action30 = new Intent(context, SnoozeReceiver.class);
        Intent action60 = new Intent(context, SnoozeReceiver.class);
        Intent snoozeIntent = new Intent(context, MainActivity.class);
        action10.setClass(context, SnoozeReceiver.class);
        action10.putExtra(MainActivity.Notification_Title,title);
        action10.putExtra(MainActivity.Notification_Description,description);
        action10.putExtra(MainActivity.Notification_Id,id);
        action10.putExtras(action10);
        action30.putExtras(action10);
        action60.putExtras(action10);
        snoozeIntent.putExtras(action10);
        action10.putExtra(Notification_Minutes, 10);
        action30.putExtra(Notification_Minutes, 30);
        action60.putExtra(Notification_Minutes, 60);
        PendingIntent pendingAction10 = PendingIntent.getBroadcast(context, id,
                action10, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingAction30 = PendingIntent.getBroadcast(context, id,
                action10, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingAction60 = PendingIntent.getBroadcast(context, id,
                action10, PendingIntent.FLAG_UPDATE_CURRENT);

        snoozeIntent.putExtra(MainActivity.Notification_Id, id);
        PendingIntent editSnooze = PendingIntent.getActivity(context, id, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(editSnooze)
                .addAction(R.drawable.ic_add_alarm_black_24dp, "+10 min", pendingAction10)
                .addAction(R.drawable.ic_add_alarm_black_24dp, "+30 min", pendingAction30)
                .addAction(R.drawable.ic_add_alarm_black_24dp, "+1 hour", pendingAction60)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .build();

        NotificationManager.notify(id, notification);
    }
}
