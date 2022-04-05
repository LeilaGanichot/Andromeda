package edu.fsu.cs.andromeda.db.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;

    private LiveData<List<ToDo>> allToDosByDueDate;
    private LiveData<List<ToDo>> searchToDosResults;

    public ToDoViewModel(@NonNull Application application) {
        super(application);

        toDoRepository = new ToDoRepository(application);
        allToDosByDueDate = toDoRepository.getAllToDosByDueDate();
    }

    public long upsertToDo(ToDo toDo) {
        return toDoRepository.upsertToDo(toDo);
    }

    public void deleteToDo(ToDo toDo) {
        toDoRepository.deleteToDo(toDo);
    }

    public LiveData<List<ToDo>> getAllToDosByDueDate() {
        return allToDosByDueDate;
    }

    public LiveData<List<ToDo>> getSearchToDosResults(String searchQuery) {
        searchToDosResults = toDoRepository.getSearchToDoResults(searchQuery);
        return searchToDosResults;
    }
}
