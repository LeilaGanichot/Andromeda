package edu.fsu.cs.andromeda.ui.todo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoViewModel;
import edu.fsu.cs.andromeda.ui.todo.calendar.CalendarAdapter;

public class ToDoFragment extends Fragment {

    // Widgets
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, rvDayDetails;
    private TextView tvCourseTest, tvAssignmentTest;

    private FloatingActionButton fabAddNewToDo;

    // top calendar controls
    private CalendarAdapter calendarAdapter;
    private Button btnPrevMonth, btnNextMonth;
    // calendar holder
    private LinearLayout llCalendarHolder;

    // Quick to do view
    private ToDoAdapter toDoAdapter;

    // Local vars
    private org.joda.time.LocalDate selectedDate;
    // screen's dimensions
    int sWidth;
    int sHeight;
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
//        toDoViewModel.upsertToDo(
//                new ToDo("Complete udemy course on Kotlin",
//                        "Access from work laptop since it requires business account.",
//                        "2022-04-15 16:30:00",
//                        false)
//        );
//        toDoViewModel.upsertToDo(
//                new ToDo("Take Mulder to dog groomer",
//                        "Poor guy needs a bath ASAP",
//                        "2022-04-20 09:30:00",
//                        false)
//        );


        // get the device dimensions to change the display constraints dynamically
        sWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        sHeight = getContext().getResources().getDisplayMetrics().heightPixels;
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

        llCalendarHolder = view.findViewById(R.id.ll_calendar_holder);
        llCalendarHolder.setLayoutParams(new LinearLayout.LayoutParams(sWidth, sHeight));

        fabAddNewToDo = view.findViewById(R.id.fab_add_new_to_do);
    }

    private void setCalendarViews() {
        // month view
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        calendarAdapter = new CalendarAdapter(daysInMonth, monthFromDate(selectedDate));
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
        updateToDoQuickView();
        toDosByDate.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                toDoAdapter.setToDosByDate(toDos);
            }
        });
    }

    private void updateToDoQuickView() {
        toDosByDate = Transformations.switchMap(
                currentCalendarDay, updatedCalendarDay ->
                        toDoViewModel.getToDosByDueDate(formatDateForDB(selectedDate, updatedCalendarDay)));
    }

    public static String formatDateForDB(org.joda.time.LocalDate date, String dayNum){
        if(dayNum.equals(" ")) return " ";
        org.joda.time.LocalDate moddedDate = date.withDayOfMonth(Integer.parseInt(dayNum));
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        return dtf.print(moddedDate);
    }

    private String monthYearFromDate(org.joda.time.LocalDate selectedDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM yyyy");
        return dtf.print(selectedDate);
    }

    private ArrayList<String> daysInMonthArray(org.joda.time.LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        org.joda.time.YearMonth yearMonth = new YearMonth(
                selectedDate.getYear(),
                selectedDate.getMonthOfYear()
        );

        int daysInMonth = yearMonth
                .toDateTime(null)
                .dayOfMonth()
                .getMaximumValue();

        org.joda.time.LocalDate firstOfMonth = selectedDate.withDayOfMonth(1); // get first day of the month
        int dayOfWeek = firstOfMonth.getDayOfWeek();

        for (int i = 1; i < 42; i++){
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add(" "); // we add a blank
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthFromDate(LocalDate selectedDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM");
        return dtf.print(selectedDate); // minus because Joda time is not zero indexed
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
    }
}