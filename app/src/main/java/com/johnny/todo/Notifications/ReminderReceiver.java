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
public class ReminderReceiver extends BroadcastReceiver {

    NotificationManagerCompat NotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra(MainActivity.Notification_Title);
        String description = intent.getStringExtra(MainActivity.Notification_Description);
        int id = intent.getIntExtra(MainActivity.Notification_Id, -1);

        if(id == -1) return;

        Intent broadcastIntentAction = new Intent(context, SnoozeReceiver.class);
        broadcastIntentAction.setClass(context, SnoozeReceiver.class);
        broadcastIntentAction.putExtra(MainActivity.Notification_Title,title);
        broadcastIntentAction.putExtra(MainActivity.Notification_Description,description);
        broadcastIntentAction.putExtra(MainActivity.Notification_Id,id);
        PendingIntent actionIntent = PendingIntent.getBroadcast(context, id,
                broadcastIntentAction, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(context, MainActivity.class);
        snoozeIntent.putExtra(MainActivity.Notification_Id, id);
        PendingIntent editSnooze = PendingIntent.getActivity(context, id, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(editSnooze)
                .addAction(R.drawable.ic_add_alarm_black_24dp, "Snooze 10 min", actionIntent)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .build();

        NotificationManager.notify(id, notification);
    }
}
