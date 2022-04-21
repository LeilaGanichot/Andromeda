package edu.fsu.cs.andromeda.util.reminder;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.andromeda.db.reminder.Reminder;
import edu.fsu.cs.andromeda.db.todo.ToDo;

public class ReminderHelper {

    //INTENT EXTRAS, Alarm stuff.
    public static final String EXTRA_REMINDER_TITLE = "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_TITLE";
    public static final String EXTRA_REMINDER_BODY = "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_BODY";
    public static final String EXTRA_REMINDER_ID =   "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_ID";
    public static final String EXTRA_REMINDER_ID_LIST =   "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_ID_LIST";
    public static final String EXTRA_TO_DO_ID =   "edu.fsu.cs.andromeda.util.reminder.EXTRA_TO_DO_ID";
    public static final String EXTRA_REMINDER_DATE = "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_DATE";
    public static final String EXTRA_REMINDER_TIME = "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_TIME";
    public static final String EXTRA_REMINDER_STATUS = "edu.fsu.cs.andromeda.util.reminder.EXTRA_REMINDER_STATUS";

    // Alarm stuff
    private static AlarmManager alarmManager;
    private static Intent reminderIntent;
    private static PendingIntent pendingIntent;

    private static ReminderHelper instance = null;
    private Activity activity;

    private ReminderHelper(Activity activity){
        this.activity = activity;
    }

    public static synchronized ReminderHelper getInstance(Activity activity){
        if(instance == null){
            instance = new ReminderHelper(activity);
            //Alarm stuff.
            alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
            reminderIntent = new Intent(activity, AlertReceiver.class);
        }
        return (instance);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createSingleReminder(long timeInMillis,
                                     String reminderTitle, String reminderBody, int toDoId,
                                     int reminderId){
        if(activity != null) {
            reminderFunctionalities(timeInMillis, reminderTitle, reminderBody, toDoId, reminderId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createMultiReminder(long timeInMillis,
                                     String reminderTitle, String reminderBody, int toDoId,
                                     ArrayList<Integer> reminderIds){
        if(activity != null) {
            reminderFunctionalitiesForMulti(timeInMillis, reminderTitle, reminderBody, toDoId, reminderIds);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void reminderFunctionalitiesForMulti(long timeInMillis,
                                                String reminderTitle, String reminderBody, int toDoId,
                                                        ArrayList<Integer> reminderIds){
//        String listOfIds = "";
//        for (int id: reminderIds) {
//            listOfIds += id + ", ";
//        }
        reminderIntent.putExtra(EXTRA_REMINDER_TITLE, reminderTitle);
        // Reminder intent -- CHECKPOINT
        reminderIntent.putExtra(EXTRA_REMINDER_BODY, reminderBody);
        reminderIntent.putIntegerArrayListExtra(EXTRA_REMINDER_ID_LIST, reminderIds);
        reminderIntent.putExtra(EXTRA_TO_DO_ID, toDoId);
        pendingIntent = PendingIntent.getBroadcast(
                instance.activity.getApplicationContext(),
                reminderIds.get(0),
                reminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void reminderFunctionalities(long timeInMillis,
                                                String reminderTitle, String reminderBody, int toDoId,
                                                int reminderId){
        reminderIntent.putExtra(EXTRA_REMINDER_TITLE, reminderTitle);
        // Reminder intent -- CHECKPOINT
        reminderIntent.putExtra(EXTRA_REMINDER_BODY, reminderBody);
        reminderIntent.putExtra(EXTRA_REMINDER_ID, reminderId);
        reminderIntent.putExtra(EXTRA_TO_DO_ID, toDoId);
        pendingIntent = PendingIntent.getBroadcast(
                instance.activity.getApplicationContext(),
                reminderId,
                reminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public void cancelReminders(Context context, ToDo toDo, List<Reminder> reminders) {
        Intent reminderIntent = new Intent(context, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;
        reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_TITLE, toDo.getTitle());
        reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_BODY, toDo.getBody());

        reminderIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        for (Reminder reminder : reminders) {
            reminderIntent.putExtra(ReminderHelper.EXTRA_REMINDER_ID, reminder.getReminderId());
            pendingIntent = PendingIntent.getBroadcast(
                    context,
                    reminder.getReminderId(),
                    reminderIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void onDestroy(){
        if(instance != null) instance = null;
    }
}
