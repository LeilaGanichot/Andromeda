package edu.fsu.cs.andromeda.db.reminder;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert(onConflict = REPLACE)
    long upsertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Query("SELECT * FROM tableReminder ORDER BY reminderTimeInMs ASC")
    LiveData<List<Reminder>> getAllReminders();

    @Query("SELECT * FROM tableReminder WHERE toDoFk = :toDoFk ORDER BY reminderTimeInMs ASC")
    LiveData<List<Reminder>> getAllRemindersByToDoFk(int toDoFk);
}
