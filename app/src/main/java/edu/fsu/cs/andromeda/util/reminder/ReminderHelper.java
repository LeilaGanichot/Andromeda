package edu.fsu.cs.andromeda.util.reminder;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ReminderHelper {

    //INTENT EXTRAS, Alarm stuff.
    public static final String EXTRA_REMINDER_TITLE = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_TITLE";
    public static final String EXTRA_REMINDER_BODY = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_BODY";
    public static final String EXTRA_REMINDER_ID =   "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_ID";
    public static final String EXTRA_REMINDER_DATE = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_DATE";
    public static final String EXTRA_REMINDER_TIME = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_TIME";
    public static final String EXTRA_REMINDER_STATUS = "com.pinkmoon.flux.ui.notifications.EXTRA_REMINDER_STATUS";

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
                                     String reminderTitle, String reminderBody,
                                     int reminderId){
        if(activity != null) {
            reminderFunctionalities(timeInMillis, reminderTitle, reminderBody, reminderId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void reminderFunctionalities(long timeInMillis,
                                                String reminderTitle, String reminderBody,
                                                int reminderId){
        reminderIntent.putExtra(EXTRA_REMINDER_TITLE, reminderTitle);
        // Reminder intent -- CHECKPOINT
        reminderIntent.putExtra(EXTRA_REMINDER_BODY, reminderBody);
        reminderIntent.putExtra(EXTRA_REMINDER_ID, reminderId);
        pendingIntent = PendingIntent.getBroadcast(
                instance.activity.getApplicationContext(),
                reminderId,
                reminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public static void onDestroy(){
        if(instance != null) instance = null;
    }
}
