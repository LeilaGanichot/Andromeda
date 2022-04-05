package edu.fsu.cs.andromeda.db.todo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A representation of a To Do as a POJO (plain old Java object).
 */
@Entity(tableName = "tableToDo")
public class ToDo {

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
}