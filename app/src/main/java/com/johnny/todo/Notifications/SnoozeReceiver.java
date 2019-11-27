package com.johnny.todo.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import static com.johnny.todo.Activities.MainActivity.Notification_Description;
import static com.johnny.todo.Activities.MainActivity.Notification_Id;
import static com.johnny.todo.Activities.MainActivity.Notification_Title;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(Notification_Title);
        String description = intent.getStringExtra(Notification_Description);
        int id = intent.getIntExtra(Notification_Id, -1);

        if(id == -1) {
            return;
        }

        int snooze = 10000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent newIntent = new Intent(context, ReminderReceiver.class);
        newIntent.setClass(context, ReminderReceiver.class);
        intent.putExtra(Notification_Title, title);
        intent.putExtra(Notification_Description, description);
        intent.putExtra(Notification_Id, id);
        long time = snooze + System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
