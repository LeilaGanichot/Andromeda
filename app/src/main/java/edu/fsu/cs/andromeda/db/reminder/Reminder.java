package edu.fsu.cs.andromeda.db.reminder;

import static androidx.room.ForeignKey.CASCADE;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import edu.fsu.cs.andromeda.db.todo.ToDo;

@Entity(
        tableName = "tableReminder",
        foreignKeys = {
                @ForeignKey(
                        entity = ToDo.class,
                        parentColumns = "toDoId",
                        childColumns = "toDoFk",
                        onDelete = CASCADE
                )
        })
public class Reminder implements Parcelable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int reminderId;

    private int toDoFk;

    private long reminderTimeInMs;

    public Reminder(int toDoFk, long reminderTimeInMs) {
        this.toDoFk = toDoFk;
        this.reminderTimeInMs = reminderTimeInMs;
    }

    protected Reminder(Parcel in) {
        reminderId = in.readInt();
        toDoFk = in.readInt();
        reminderTimeInMs = in.readLong();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };


    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getToDoFk() {
        return toDoFk;
    }

    public void setToDoFk(int toDoFk) {
        this.toDoFk = toDoFk;
    }

    public long getReminderTimeInMs() {
        return reminderTimeInMs;
    }

    public void setReminderTimeInMs(long reminderTimeInMs) {
        this.reminderTimeInMs = reminderTimeInMs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(reminderId);
        parcel.writeInt(toDoFk);
        parcel.writeLong(reminderTimeInMs);
    }
}
