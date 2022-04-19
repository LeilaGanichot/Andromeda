package edu.fsu.cs.andromeda.ui.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.note.NoteViewModel;

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        setOnClickListeners();
        fabAddNewNote = view.findViewById(R.id.fab_add_new_note);

        ArrayList<String> noteItems = new ArrayList<>();
        noteItems.add("Note 1");
        noteItems.add("Note 2");

        RecyclerView rView = view.findViewById(R.id.notesRV);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotesRecyclerViewAdapter(getActivity(), noteItems);

        rView.setAdapter(adapter);

        return view;

    }
    private void setOnClickListeners()
    {
      /*
        fabAddNewNote.setOnClickListener(v -> {
            NotesFragmentDirections.ActionNotesFragmentToAddEditNoteFragment action =
                    NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(null);
            Navigation.findNavController(v).navigate(action);
        });*/
    }


}