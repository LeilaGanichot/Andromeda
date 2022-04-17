package edu.fsu.cs.andromeda.db.todo;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoDao {

    /**
     * Insert/Update method--if the ID is the same as one already existing
     * in our database, it will replace it, else, insert a new to do entry.
     */
    @Insert(onConflict = REPLACE)
    long upsertToDo(ToDo toDo);

    @Delete
    void deleteToDo(ToDo toDo);

    @Query("SELECT * FROM tableToDo ORDER BY dueDate DESC")
    LiveData<List<ToDo>> getAllToDos();

    @Query("SELECT * FROM tableToDo " +
            "WHERE title LIKE '%' || :searchQuery || '%' " +
            "OR body LIKE '%' || :searchQuery || '%' " +
            "ORDER BY dueDate DESC")
    LiveData<List<ToDo>> searchToDo(String searchQuery);

    @Query("SELECT * FROM tableToDo WHERE dueDate LIKE :dueDate || '%'")
    LiveData<List<ToDo>> getToDosByDueDate(String dueDate);

    @Query("UPDATE tableToDo SET complete = 1 WHERE toDoId = :toDoId")
    void markToDoAsComplete(int toDoId);
}
