package edu.fsu.cs.andromeda.ui.todo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoViewModel;
import edu.fsu.cs.andromeda.ui.todo.calendar.CalendarAdapter;
import edu.fsu.cs.andromeda.util.AndromedaDate;

public class ToDoFragment extends Fragment {

    // Widgets
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, rvDayDetails;
    private TextView tvCourseTest, tvAssignmentTest;

    private FloatingActionButton fabAddNewToDo;

    // top calendar controls
    private CalendarAdapter calendarAdapter;
    private Button btnPrevMonth, btnNextMonth;

    // Quick to do view
    private ToDoAdapter toDoAdapter;

    // Local vars
    private org.joda.time.LocalDate selectedDate;
    // screen's dimensions
    private ToDoViewModel toDoViewModel;
    private MutableLiveData<String> currentCalendarDay = new MutableLiveData<>(" ");
    private LiveData<List<ToDo>> toDosByDate;

    public ToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_to_do, container, false);

        selectedDate = LocalDate.now();
        defineWidgets(view);
        setCalendarViews();
        setOnClickListeners();

        return view;
    }

    private void defineWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.rv_calendar);
        rvDayDetails = view.findViewById(R.id.rv_day_details);
        monthYearText = view.findViewById(R.id.tv_month_year);

        btnPrevMonth = view.findViewById(R.id.btn_prev_month);
        btnNextMonth = view.findViewById(R.id.btn_next_month);

        fabAddNewToDo = view.findViewById(R.id.fab_add_new_to_do);
    }

    private void setCalendarViews() {
        // month view
        monthYearText.setText(AndromedaDate.monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = AndromedaDate.daysInMonthArray(selectedDate);
        calendarAdapter = new CalendarAdapter(daysInMonth, AndromedaDate.monthFromDate(selectedDate));
        toDoAdapter = new ToDoAdapter();

        if(toDoViewModel != null) {
            toDoViewModel.getAllToDos().observe(
                    getViewLifecycleOwner(),
                    toDos -> calendarAdapter.setAllToDos(toDos)
            );
        }

        // rv stuff
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7); // 7 columns in the rv
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String dayText) {
                if(!dayText.equals(" ")){
                    currentCalendarDay.postValue(dayText);
                }
            }
        });

        rvDayDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDayDetails.setHasFixedSize(true);
        rvDayDetails.setAdapter(toDoAdapter);
        toDoAdapter.setOnItemClickListener((toDo, view) -> {
            ToDoFragmentDirections.ActionToDoFragmentToAddEditToDoFragment action =
                    ToDoFragmentDirections.actionToDoFragmentToAddEditToDoFragment(toDo);
            Navigation.findNavController(view).navigate(action);
        });
        // just a simple animation to hide the FAB when the user scrolls down the RV,
        // and restore it when they scroll to top
        rvDayDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    fabAddNewToDo.hide();
                } else if (dy < 0) {
                    fabAddNewToDo.show();
                }
            }
        });
        updateToDoQuickView();
        toDosByDate.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                toDoAdapter.setToDosByDate(toDos);
            }
        });

        // delete on swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                toDoViewModel.deleteToDo(toDoAdapter.getToDoAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "To Do deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvDayDetails);
    }

    private void updateToDoQuickView() {
        toDosByDate = Transformations.switchMap(
                currentCalendarDay, updatedCalendarDay ->
                        toDoViewModel.getToDosByDueDate(AndromedaDate.formatDateForDB(selectedDate, updatedCalendarDay)));
    }

    private void setOnClickListeners() {
        btnPrevMonth.setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            setCalendarViews();
        });

        btnNextMonth.setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            setCalendarViews();
        });

        fabAddNewToDo.setOnClickListener(v -> {
            ToDoFragmentDirections.ActionToDoFragmentToAddEditToDoFragment action =
                    ToDoFragmentDirections.actionToDoFragmentToAddEditToDoFragment(null);
            Navigation.findNavController(v).navigate(action);
        });
    }
}