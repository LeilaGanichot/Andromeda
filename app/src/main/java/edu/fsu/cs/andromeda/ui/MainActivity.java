package edu.fsu.cs.andromeda.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoViewModel;

public class MainActivity extends AppCompatActivity {

    // TODO @cm test stuff; remove {
    private EditText etSearch;
    private TextView tvDisplayResults;
    private Button btnSearch;

    private ToDoViewModel toDoViewModel;

    private LiveData<List<ToDo>> searchToDosResults;
    private MutableLiveData<String> searchQueryText = new MutableLiveData<>("");
    // TODO @cm test stuff; remove }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO @cm test stuff; remove {
        etSearch = findViewById(R.id.et_search);
        tvDisplayResults = findViewById(R.id.tv_display_results);
        btnSearch = findViewById(R.id.btn_positive);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.upsertToDo(
                new ToDo("Complete udemy course on Kotlin",
                        "Access from work laptop since it requires business account.",
                        "2022-04-15 16:30:00",
                        false)
        );
        toDoViewModel.upsertToDo(
                new ToDo("Take Mulder to dog groomer",
                        "",
                        "2022-04-05 09:30:00",
                        false)
        );

        searchToDosResults = Transformations.switchMap(searchQueryText,
                updatedSearchQueryText -> toDoViewModel.getSearchToDosResults(updatedSearchQueryText));
        searchToDosResults.observe(this, toDos -> {
            String results = "";
            for (int i = 0; i < toDos.size(); i++) {
                results += toDos.get(i).toString() + "\n";
            }
            tvDisplayResults.setText(results);
        });

        btnSearch.setOnClickListener(v -> searchQueryText.postValue(etSearch.getText().toString()));
        // TODO @cm test stuff, remove }
    }
}