package edu.fsu.cs.andromeda.db.note;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tableNote")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int noteId;

    private String title;

    private String body;

    private String dateCreated;

    public Note(String title, String body, String dateCreated) {
        this.title = title;
        this.body = body;
        this.dateCreated = dateCreated;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
