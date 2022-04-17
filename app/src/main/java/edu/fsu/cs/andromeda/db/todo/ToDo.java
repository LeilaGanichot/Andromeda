package edu.fsu.cs.andromeda.db.todo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * A representation of a To Do as a POJO (plain old Java object).
 */
@Entity(tableName = "tableToDo")
public class ToDo implements Parcelable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int toDoId;

    private String title;

    private String body;

    private String dueDate;

    private boolean complete;

    /**
     * Constructor for the To Do object that represents a table on our database.
     * @param title the name of the to do as a String.
     * @param body a more detailed description of the to do as a String.
     * @param dueDate the hard due date for this task. It would be in the
     *                format of "yyy-MM-dd HH:mm:ss" to allow us to sort by date
     *                easily with a query through SQLite without doing it
     *                ourselves as logic within our codebase.
     * @param complete a boolean value that denotes a To Do as completed
     *                       or not. This is useful because if we check today's date
     *                       against the @dueDate, and this value is still set to
     *                       false it means either: the user forgot to mark it as
     *                       done, or genuinely forgot to complete it and it is
     *                       past its due date (late).
     */
    public ToDo(String title, String body, String dueDate, boolean complete) {
        this.title = title;
        this.body = body;
        this.dueDate = dueDate;
        this.complete = complete;
    }

    protected ToDo(Parcel in) {
        toDoId = in.readInt();
        title = in.readString();
        body = in.readString();
        dueDate = in.readString();
        complete = in.readByte() != 0;
    }

    public static final Creator<ToDo> CREATOR = new Creator<ToDo>() {
        @Override
        public ToDo createFromParcel(Parcel in) {
            return new ToDo(in);
        }

        @Override
        public ToDo[] newArray(int size) {
            return new ToDo[size];
        }
    };

    public int getToDoId() {
        return toDoId;
    }

    public void setToDoId(int toDoId) {
        this.toDoId = toDoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @NonNull
    @Override
    public String toString() {
        return "Title: " + title + "\n" + "Body:" + body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(toDoId);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(dueDate);
        dest.writeByte((byte) (complete ? 1 : 0));
    }
}