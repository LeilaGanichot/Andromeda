package edu.fsu.cs.andromeda.db.todo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.fsu.cs.andromeda.db.AndromedaDB;

/**
 * Handles all operations that will interact with the DB.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class ToDoRepository {

    private ToDoDao toDoDao;

    private LiveData<List<ToDo>> allToDosByDueDate;
    private LiveData<List<ToDo>> searchToDoResults;

    /**
     * A constructor that will instantiate a repo object.
     * @param application the context this is being used from.
     */
    public ToDoRepository(Application application) {
        AndromedaDB andromedaDB = AndromedaDB.getInstance(application);

        toDoDao = andromedaDB.toDoDao();

        // LiveData objects
        allToDosByDueDate = toDoDao.getAllToDosByDueDate();
    }

    // Database operations
    public long upsertToDo(ToDo toDo) {
        try{
            return new UpsertToDoAsync(toDoDao).execute(toDo).get();
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteToDo(ToDo toDo) {
        new DeleteToDoAsync(toDoDao).execute(toDo);
    }

    public LiveData<List<ToDo>> getAllToDosByDueDate() {
        return allToDosByDueDate;
    }

    public LiveData<List<ToDo>> getSearchToDoResults(String searchQuery) {
        searchToDoResults = toDoDao.searchToDo(searchQuery);
        return searchToDoResults;
    }

    // Async task operations
    public class UpsertToDoAsync extends AsyncTask<ToDo, Void, Long> {

        ToDoDao toDoDao;

        public UpsertToDoAsync(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Long doInBackground(ToDo... toDos) {
            long id;
            id = toDoDao.upsertToDo(toDos[0]);
            return id;
        }
    }

    public class DeleteToDoAsync extends AsyncTask<ToDo, Void, Long> {

        ToDoDao toDoDao;

        public DeleteToDoAsync(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Long doInBackground(ToDo... toDos) {
            toDoDao.deleteToDo(toDos[0]);
            return null;
        }
    }
}
