package edu.fsu.cs.andromeda.db.reminder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private ReminderRepository reminderRepository;

    private LiveData<List<Reminder>> allReminders;
    private LiveData<List<Reminder>> allRemindersByToDoFk;

    public ReminderViewModel(@NonNull Application application) {
        super(application);

        reminderRepository = new ReminderRepository(application);

        allReminders = reminderRepository.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }

    public LiveData<List<Reminder>> getAllRemindersByToDoFk(int toDoFk) {
        allRemindersByToDoFk = reminderRepository.getAllRemindersByToDoFk(toDoFk);
        return allRemindersByToDoFk;
    }

    public long upsertReminder(Reminder reminder){
        return reminderRepository.upsertReminder(reminder);
    }

    public void deleteReminder(Reminder reminder){
        reminderRepository.deleteReminder(reminder);
    }
}
