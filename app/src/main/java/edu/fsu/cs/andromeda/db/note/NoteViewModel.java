package edu.fsu.cs.andromeda.db.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;

    private LiveData<List<Note>> allNotesByDateCreated;
    private LiveData<List<Note>> searchAllNotesResults;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepository = new NoteRepository(application);
        allNotesByDateCreated = noteRepository.getAllNotesByDateCreated();
    }

    public long upsertNote(Note note) {
        return noteRepository.upsertNote(note);
    }

    public void deleteNote(Note note) {
        noteRepository.deleteNote(note);
    }



    public LiveData<List<Note>> getAllNotesByDateCreated() {
        return allNotesByDateCreated;
    }

    public LiveData<List<Note>> getSearchAllNotesResults(String searchQuery) {
        searchAllNotesResults = noteRepository.getSearchNotesResults(searchQuery);
        return searchAllNotesResults;
    }
}
