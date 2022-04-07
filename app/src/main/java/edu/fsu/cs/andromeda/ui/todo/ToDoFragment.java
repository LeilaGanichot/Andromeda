package edu.fsu.cs.andromeda.ui.todo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    // Local vars
    private org.joda.time.LocalDate selectedDate;
    // screen's dimensions
    int sWidth;
    int sHeight;
    private ToDoViewModel toDoViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO @cm used for communication between fragments, but may be scrapped in favor of using
    //      the Navigation library, not sure yet
    private ToDoFragmentListener listener;

    public ToDoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        // get the device dimensions to change the display constraints dynamically
        sWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        sHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_to_do, container, false);

        defineWidgets(view);
        //setCalendarViews();
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

        if(toDoViewModel != null) {
            toDoViewModel.getAllToDosByDueDate().observe(
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

                }
            }
        });

        rvDayDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDayDetails.setHasFixedSize(true);
        // rvDayDetails.setAdapter(); TODO @cm set adapter of To Dos
    }

    private String monthYearFromDate(org.joda.time.LocalDate selectedDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM yyyy");
        return dtf.print(selectedDate);
    }

    private ArrayList<String> daysInMonthArray(org.joda.time.LocalDate selectedDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        // TODO @cm probably should switch, Joda time is being uncooperative
        return daysInMonthArray;
    }

    private String monthFromDate(LocalDate selectedDate) {
        return null;
    }

    private void setOnClickListeners() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ToDoFragmentListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ToDoFragmentListener {
        // TODO @cm add interface methods as needed for fragment to parent communication if needed
    }
}