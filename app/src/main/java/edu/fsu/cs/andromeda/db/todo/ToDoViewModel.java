package edu.fsu.cs.andromeda.db.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;

    private LiveData<List<ToDo>> allToDos;
    private LiveData<List<ToDo>> searchToDosResults;
    private LiveData<List<ToDo>> toDosByDueDate;

    public ToDoViewModel(@NonNull Application application) {
        super(application);

        toDoRepository = new ToDoRepository(application);
        allToDos = toDoRepository.getAllToDos();
    }

    public long upsertToDo(ToDo toDo) {
        return toDoRepository.upsertToDo(toDo);
    }

    public void deleteToDo(ToDo toDo) {
        toDoRepository.deleteToDo(toDo);
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public LiveData<List<ToDo>> getSearchToDosResults(String searchQuery) {
        searchToDosResults = toDoRepository.getSearchToDoResults(searchQuery);
        return searchToDosResults;
    }

    public LiveData<List<ToDo>> getToDosByDueDate(String dueDate) {
        toDosByDueDate = toDoRepository.getToDosByDueDate(dueDate);
        return toDosByDueDate;
    }
}
