package edu.fsu.cs.andromeda.db.note;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = REPLACE)
    long upsertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM tableNote ORDER BY dateCreated DESC")
    LiveData<List<Note>> getAllNotesByDateCreated();

    @Query("SELECT * FROM tableNote " +
            "WHERE title LIKE '%' || :searchQuery || '%' " +
            "OR body LIKE '%' || :searchQuery || '%' " +
            "ORDER BY dateCreated DESC")
    LiveData<List<Note>> searchNotes(String searchQuery);
}
