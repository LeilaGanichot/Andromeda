package edu.fsu.cs.andromeda.util.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.andromeda.db.reminder.Reminder;
import edu.fsu.cs.andromeda.db.reminder.ReminderRepository;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoRepository;

public class NotificationInteractionReceiver extends BroadcastReceiver {
    private int toDoId;
    private int reminderId;
    private NotificationManagerCompat notificationManagerCompat;
    private ToDoRepository toDoRepository;
    private ReminderRepository reminderRepository;
    private ArrayList<Integer> reminderIds;


    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManagerCompat = NotificationManagerCompat.from(context);
        Bundle args = intent.getExtras();

        toDoId = args.getInt(ReminderHelper.EXTRA_TO_DO_ID, -1);
        reminderId = args.getInt(ReminderHelper.EXTRA_REMINDER_ID, -1);
        reminderIds = args.getIntegerArrayList(ReminderHelper.EXTRA_REMINDER_ID_LIST);

        new ExecuteReminderStatusChange(context).execute();
    }

    private class ExecuteReminderStatusChange extends AsyncTask<Context, Void, Void> {
        Context passedContext;

        public ExecuteReminderStatusChange(Context passedContext) {
            this.passedContext = passedContext;

        }

        @Override
        protected Void doInBackground(Context... contexts) {
            toDoRepository = new ToDoRepository(passedContext);
            toDoRepository.markToDoAsComplete(toDoId);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            notificationManagerCompat.cancel(reminderId);
            ToDo dToDo = new ToDo("", "", "", true);
            dToDo.setToDoId(toDoId);
            cancelReminders(passedContext, dToDo, reminderIds);
        }
    }

    private void cancelReminders(Context context, ToDo toDo, ArrayList<Integer> reminders) {
        Intent reminderIntent = new Intent(context, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;
        reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_TITLE, toDo.getTitle());
        reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_BODY, toDo.getBody());

        reminderIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        for (int reminderId : reminders) {
            reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_ID, reminderId);
            pendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminderId,
                    reminderIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            alarmManager.cancel(pendingIntent);
        }
    }
}
