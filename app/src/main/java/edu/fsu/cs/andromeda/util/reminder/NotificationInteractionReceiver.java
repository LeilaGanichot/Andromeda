package edu.fsu.cs.andromeda.util.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationManagerCompat;

import edu.fsu.cs.andromeda.db.todo.ToDoRepository;

public class NotificationInteractionReceiver extends BroadcastReceiver {
    private int toDoId;
    private NotificationManagerCompat notificationManagerCompat;
    private ToDoRepository toDoRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        toDoId = intent.getIntExtra(ReminderHelper.EXTRA_REMINDER_ID, -1);
        notificationManagerCompat = NotificationManagerCompat.from(context);


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
        }
    }
}
