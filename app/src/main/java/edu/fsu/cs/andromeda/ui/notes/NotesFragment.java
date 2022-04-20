package edu.fsu.cs.andromeda.ui.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.note.Note;
import edu.fsu.cs.andromeda.db.note.NoteViewModel;
import edu.fsu.cs.andromeda.ui.todo.AddEditToDoFragmentDirections;
import edu.fsu.cs.andromeda.util.AndromedaDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Add new note
    private FloatingActionButton fabAddNewNote;

    private NotesRecyclerViewAdapter adapter;

    private NotesRecyclerViewAdapter notesAdapter;

    private NoteViewModel noteViewModel;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        // TODO remove the below: just a way to insert dummy note data into the db
        noteViewModel.upsertNote(new Note(
                "Note title 1",
                "Note body 1",
                AndromedaDate.getTodaysDate()
        ));
        noteViewModel.upsertNote(new Note(
                "Note title 2",
                "Note body 2",
                AndromedaDate.getTodaysDate()
        ));
        noteViewModel.upsertNote(new Note(
                "Note title 3",
                "Note body 3",
                AndromedaDate.getTodaysDate()
        ));
        noteViewModel.upsertNote(new Note(
                "Note title 4",
                "Note body 4",
                AndromedaDate.getTodaysDate()
        ));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        fabAddNewNote = view.findViewById(R.id.fab_add_new_note);

        ArrayList<String> noteItems = new ArrayList<>();
        noteItems.add("Note 1");
        noteItems.add("Note 2");

        RecyclerView rView = view.findViewById(R.id.notesRV);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotesRecyclerViewAdapter();

        rView.setAdapter(adapter);
        rView.addOnScrollListener(new RecyclerView.OnScrollListener() { // just hides the FAB if the user scrolls through the notes, and shows it again once the user is at the very top of the list
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    fabAddNewNote.hide();
                } else if (dy < 0) {
                    fabAddNewNote.show();
                }
            }
        });

        setOnClickListeners();
        /*
            For this code below, it "observes" the table from our local database that houses the
            notes. When the data in the database table changes (such as when we insert/delete/update
            a note, this observer gets triggered automatically and the code within its onChanged method
            is executed, in our case, we update the RV's list from the adapter's setter method.
            Let me know if you have questions on this, be happy to explain!
         */
        noteViewModel.getAllNotesByDateCreated().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNoteList(notes);
            }
        });

        return view;

    }
    private void setOnClickListeners()
    {
        fabAddNewNote.setOnClickListener(v -> {
            NotesFragmentDirections.ActionNotesFragmentToAddEditNoteFragment action =
                NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(null);
            Navigation.findNavController(v).navigate(action);
        });

        adapter.setOnItemClickListener(new NotesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note, View view) {
                // we navigate to edit a note from here
                NotesFragmentDirections.ActionNotesFragmentToAddEditNoteFragment action =
                        NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(note);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }


}