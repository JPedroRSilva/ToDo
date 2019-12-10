package com.johnny.todo.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.johnny.todo.Activities.App.CHANNEL_ID;
import static com.johnny.todo.Activities.MainActivity.Notification_Description;
import static com.johnny.todo.Activities.MainActivity.Notification_Id;
import static com.johnny.todo.Activities.MainActivity.Notification_Minutes;
import static com.johnny.todo.Activities.MainActivity.Notification_Title;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(Notification_Title);
        String description = intent.getStringExtra(Notification_Description);
        int id = intent.getIntExtra(Notification_Id, -1);
        int minutes = intent.getIntExtra(Notification_Minutes, -1);

        if(id == -1) {
            return;
        }

        NotificationManagerCompat NotificationManager = NotificationManagerCompat.from(context);
        NotificationManager.cancel(id);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, ReminderReceiver.class);
        newIntent.setClass(context, ReminderReceiver.class);
        newIntent.putExtra(Notification_Title, title);
        newIntent.putExtra(Notification_Description, description);
        newIntent.putExtra(Notification_Id, id);
        LocalDateTime time = LocalDateTime.now().plusMinutes(minutes);
        long timeMilli = time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMilli, pendingIntent);
    }
}