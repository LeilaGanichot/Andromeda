package edu.fsu.cs.andromeda.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.fsu.cs.andromeda.db.note.Note;
import edu.fsu.cs.andromeda.db.note.NoteDao;
import edu.fsu.cs.andromeda.db.reminder.Reminder;
import edu.fsu.cs.andromeda.db.reminder.ReminderDao;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoDao;

@Database(
        entities = {
                Note.class,
                Reminder.class,
                ToDo.class
        },
        version = 1
)
public abstract class AndromedaDB extends RoomDatabase {
    private static AndromedaDB instance;

    // DAOs
    public abstract NoteDao     noteDao();
    public abstract ReminderDao reminderDao();
    public abstract ToDoDao     toDoDao();

    public static synchronized AndromedaDB getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AndromedaDB.class,
                    "andromeda_db"
            ).build();
        }
        return instance;
    }
}
