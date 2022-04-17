package edu.fsu.cs.andromeda.ui.todo;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoViewModel;
import edu.fsu.cs.andromeda.util.reminder.ReminderHelper;

public class AddEditToDoFragment extends Fragment {

    // local vars
    private ToDoViewModel toDoViewModel;
    private ToDo currentToDo = null;

    public static final int TEN_MIN_AS_MS = 600000;
    public static final int ONE_HOUR_IN_MS = 3600000;
    public static final int ONE_DAY_IN_MS = 86400000;

    // widgets
    private View view; // global for nav components

    private TextInputLayout tilToDoTitle;
    private TextInputLayout tilToDoBody;
    private Button btnSetToDoDueDate;

    private CheckBox cbTenMin;
    private CheckBox cbOneHour;
    private CheckBox cbOneDay;
    private CheckBox cbCustom;
    private Button btnCustomReminderTime;

    public AddEditToDoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_edit_to_do, container, false);
        defineViews();
        defineObservers();
        setOnClickListeners();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // editToDo could be null--if it is, you know it's a new to do and not an edit
        currentToDo = AddEditToDoFragmentArgs.fromBundle(getArguments()).getEditToDo();
        if(currentToDo != null) { // means the user is editing a to do
            // TODO @cm use the passed object's properties to populate the UI with its existing data
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_add_edit_fragment, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btn_save) {
            // TODO @cm check all fields for empty data before calling anything within this block
            createToDo();
            createReminder();

            Navigation.findNavController(view)
                    .navigate(R.id.action_addEditToDoFragment_to_toDoFragment);
            return true;
        } else {
            return false;
        }
    }

    private void defineViews() {
        tilToDoTitle = view.findViewById(R.id.til_to_do_title);
        tilToDoBody = view.findViewById(R.id.til_to_do_body);
        btnSetToDoDueDate = view.findViewById(R.id.btn_to_do_due_date);

        cbTenMin = view.findViewById(R.id.cb_10_min);
        cbOneHour = view.findViewById(R.id.cb_1_hour);
        cbOneDay = view.findViewById(R.id.cb_1_day);
        cbCustom = view.findViewById(R.id.cb_custom);
        btnCustomReminderTime = view.findViewById(R.id.btn_custom_reminder_time);
    }

    private void setOnClickListeners() {
        // TODO @cm add checkbox listeners, to set reminders accordingly; custom checkbox should
        //  toggle the visibility of the btnCustomReminderTime, so show or hide from the user
        btnSetToDoDueDate.setOnClickListener(v -> { // the to do's official due date
            // TODO @cm add a material date time picker & grab the datetime as a String of format "YYYY-MM-dd HH:mm:ss"
        });

        btnCustomReminderTime.setOnClickListener(v -> {
            // TODO @cm again, use a material date time picker like above
        });
    }

    private void defineObservers() {

    }

    private void createToDo() {
        if(currentToDo == null) { // new to do
            // TODO get data to build a ToDo object from the UI
            currentToDo = new ToDo(
                    "title",
                    "body",
                    "2022-04-17 12:50:00",
                    false);
            int toDoId = (int) toDoViewModel.upsertToDo(currentToDo);
            // we need to do the below in order to have a complete To Do object, since id is a PK
            // auto-incremented by the table, we don't know what this id is until this object has
            // been inserted into our database
            currentToDo.setToDoId(toDoId);
        } else {
            toDoViewModel.upsertToDo(currentToDo);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createReminder() {
        long dueDateInMs = convertToDateTime(currentToDo.getDueDate()).getTimeInMillis();
        ReminderHelper reminderHelper = ReminderHelper.getInstance(getActivity());

        // automatic, mandatory reminder on due date
        reminderHelper.createSingleReminder(
                dueDateInMs,
                currentToDo.getTitle(),
                currentToDo.getBody(),
                currentToDo.getToDoId()
        );
        // create another reminder 10 minutes before its due, it's a sample
        // TODO @cm these reminder times should be dynamically picked by the user (based upon
        //  the selected check boxes
        reminderHelper.createSingleReminder(
                dueDateInMs - TEN_MIN_AS_MS,
                currentToDo.getTitle(),
                currentToDo.getBody(),
                currentToDo.getToDoId()
        );
    }

    private Calendar convertToDateTime(String dateTime) {
        Calendar convertedDateTime = Calendar.getInstance();

        String[] dateTimeSplit = dateTime.split(" ");

        String[] dateSplit = dateTimeSplit[0].split("-");
        String[] timeSplit = dateTimeSplit[1].split(":");

        convertedDateTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]) - 1); // -1 because months are 0th indexed
        convertedDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[2]));
        convertedDateTime.set(Calendar.YEAR, Integer.parseInt(dateSplit[0]));

        convertedDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
        convertedDateTime.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
        convertedDateTime.set(Calendar.SECOND, 0);

        return convertedDateTime;
    }
}
