package edu.fsu.cs.andromeda.db.note;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.fsu.cs.andromeda.db.AndromedaDB;

public class NoteRepository {
    private NoteDao noteDao;

    private LiveData<List<Note>> allNotesByDateCreated;
    private LiveData<List<Note>> searchNotesResults;

    /**
     * A constructor that will instantiate a repo object.
     * @param application the context this is being used from.
     */
    public NoteRepository(Application application) {
        AndromedaDB andromedaDB = AndromedaDB.getInstance(application);

        noteDao = andromedaDB.noteDao();

        // LiveData objects
        allNotesByDateCreated = noteDao.getAllNotesByDateCreated();
    }

    // Database operations
    public long upsertNote(Note note) {
        try{
            return new UpsertNoteAsync(noteDao).execute(note).get();
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteNote(Note note) {
        new DeleteNoteAsync(noteDao).execute(note);
    }



    public LiveData<List<Note>> getAllNotesByDateCreated() {
        return allNotesByDateCreated;
    }

    public LiveData<List<Note>> getSearchNotesResults(String searchQuery) {
        searchNotesResults = noteDao.searchNotes(searchQuery);
        return searchNotesResults;
    }



    // Async task operations
    public class UpsertNoteAsync extends AsyncTask<Note, Void, Long> {

        NoteDao noteDao;

        public UpsertNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Long doInBackground(Note... notes) {
            long id;
            id = noteDao.upsertNote(notes[0]);
            return id;
        }
    }

    public class DeleteNoteAsync extends AsyncTask<Note, Void, Long> {

        NoteDao noteDao;

        public DeleteNoteAsync(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Long doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            return null;
        }
    }




}
