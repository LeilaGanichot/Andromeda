package edu.fsu.cs.andromeda.util.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.fsu.cs.andromeda.AndromedaApp;
import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.ui.todo.ToDoFragment;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    public static final String CHANNEL_REMINDER_REG =
            "edu.fsu.cs.andromeda.util.reminder.CHANNEL_REMINDER_REG";
    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderTitle = intent.getStringExtra(ReminderHelper.EXTRA_REMINDER_TITLE);
        String reminderBody = intent.getStringExtra(ReminderHelper.EXTRA_REMINDER_BODY);
        int reminderId = intent.getIntExtra(ReminderHelper.EXTRA_REMINDER_ID, -1);
        int toDoId = intent.getIntExtra(ReminderHelper.EXTRA_TO_DO_ID, -1);
        ArrayList<Integer> reminderIds = intent.getIntegerArrayListExtra(ReminderHelper.EXTRA_REMINDER_ID_LIST);

        notificationManagerCompat = NotificationManagerCompat.from(context);

        sendOnRegularReminderChannel(context, reminderTitle, reminderBody, reminderIds, toDoId);

    }

    private void sendOnRegularReminderChannel(Context context, String reminderTitle,
                                              String reminderBody, ArrayList<Integer> reminderIds, int toDoId) {
        Intent activityIntent = new Intent(context, ToDoFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                reminderIds.get(0), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent = new Intent(context, NotificationInteractionReceiver.class);
        broadcastIntent.putExtra(ReminderHelper.EXTRA_REMINDER_ID_LIST, reminderIds);
        broadcastIntent.putExtra(ReminderHelper.EXTRA_REMINDER_BODY, reminderBody);
        broadcastIntent.putExtra(ReminderHelper.EXTRA_TO_DO_ID, toDoId);

        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                reminderIds.get(0), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context,
                AndromedaApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_moon)
                .setContentTitle(reminderTitle)
                .setContentText(reminderBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Mark as complete", actionIntent)
                .build();

        notificationManagerCompat.notify(reminderIds.get(0), notification);
    }
}
