package edu.fsu.cs.andromeda.ui.todo;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.reminder.Reminder;
import edu.fsu.cs.andromeda.db.reminder.ReminderViewModel;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.db.todo.ToDoViewModel;
import edu.fsu.cs.andromeda.util.AndromedaDate;
import edu.fsu.cs.andromeda.util.reminder.ReminderHelper;

public class AddEditToDoFragment extends Fragment {

    // local vars
    private ToDoViewModel toDoViewModel;
    private ReminderViewModel reminderViewModel;
    private ToDo currentToDo = null;

    public static final int TEN_MIN_AS_MS = 600000;
    public static final int ONE_HOUR_IN_MS = 3600000;
    public static final int ONE_DAY_IN_MS = 86400000;

    private String selectedDateTime = "";


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
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
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
            callDatePickerDialog();
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
                    tilToDoTitle.getEditText().getText().toString().trim(),
                    tilToDoBody.getEditText().getText().toString().trim(),
                    selectedDateTime,
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
        long dueDateInMs = AndromedaDate.convertToDateTime(currentToDo.getDueDate()).getTimeInMillis();
        ReminderHelper reminderHelper = ReminderHelper.getInstance(getActivity());

        // automatic, mandatory reminder on due date
        int reminderId = (int) reminderViewModel.upsertReminder(new Reminder(
                currentToDo.getToDoId(),
                dueDateInMs
        ));
        reminderHelper.createSingleReminder(
                dueDateInMs,
                currentToDo.getTitle(),
                currentToDo.getBody(),
                reminderId
        );
        // create another reminder 10 minutes before its due, it's a test
        // TODO @cm these reminder times should be dynamically picked by the user (based upon
        //  the selected check boxes
        reminderId = (int) reminderViewModel.upsertReminder(new Reminder(
                currentToDo.getToDoId(),
                dueDateInMs - TEN_MIN_AS_MS
        ));
        reminderHelper.createSingleReminder(
                dueDateInMs - TEN_MIN_AS_MS,
                currentToDo.getTitle(),
                currentToDo.getBody(),
                reminderId
        );
    }

    private void callDatePickerDialog(){
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker<Long> mdp = materialDateBuilder.build();
        mdp.show(getChildFragmentManager(),"Material date picker from AddEditTaskFragment.java");
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                callTimePickerDialog(mdp.getHeaderText());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void callTimePickerDialog(String selectedDate){
        MaterialTimePicker mtp = new MaterialTimePicker.Builder()
                .build();

        mtp.show(getChildFragmentManager(), "Material time picker from AddEditToDoFragment.java");
        mtp.addOnPositiveButtonClickListener(v -> {
            String hour = (isSingleDigitTime(String.valueOf(mtp.getHour()))) ?
                    singleDigitToDouble(mtp.getHour()) : String.valueOf(mtp.getHour());
            String minute = (isSingleDigitTime(String.valueOf(mtp.getMinute()))) ?
                    singleDigitToDouble(mtp.getMinute()) : String.valueOf(mtp.getMinute());

            // sets the date to be attached to the Task
            selectedDateTime = AndromedaDate.formatDateTimeFromMDPToDBFormat(
                            selectedDate + " " + hour + ":" + minute + ":00"
                    );
            btnSetToDoDueDate.setText("Due on: " + selectedDate + " " + hour + ":" + minute + ":00");
        });
    }

    private boolean isSingleDigitTime(String time){
        return time.length() <= 1;
    }

    private String singleDigitToDouble(int singleDigitTime){
        return "0" + singleDigitTime;
    }
}
