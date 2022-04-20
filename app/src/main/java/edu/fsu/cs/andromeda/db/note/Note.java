package edu.fsu.cs.andromeda.db.note;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tableNote")
public class Note implements Serializable, Parcelable {

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

    protected Note(Parcel in) {
        noteId = in.readInt();
        title = in.readString();
        body = in.readString();
        dateCreated = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noteId);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(dateCreated);
    }
}
