package edu.fsu.cs.andromeda.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.note.Note;
import edu.fsu.cs.andromeda.db.note.NoteViewModel;
import edu.fsu.cs.andromeda.ui.todo.AddEditToDoFragmentArgs;
import edu.fsu.cs.andromeda.util.AndromedaDate;

public class AddEditNoteFragment extends Fragment {
    private Note currentNote = null;
    private NoteViewModel noteViewModel;
    //widgets
    private View view;
    //private Button btnSave;
    private TextInputLayout noteTitle;
    private EditText noteBody;

    public AddEditNoteFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.new_note, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // define the views
        noteTitle = view.findViewById(R.id.new_note_title);
        noteBody = view.findViewById(R.id.note_Text);

        currentNote =  AddEditNoteFragmentArgs.fromBundle(getArguments()).getEditNote();
        if(currentNote != null) {
            // populate the UI with existing data if we are editing a note
            ((AppCompatActivity) getContext()).getSupportActionBar().setTitle("Edit note");
            noteTitle.getEditText().setText(currentNote.getTitle());
            noteBody.setText(currentNote.getBody());
        }
    }

    //use this instead of save button??
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_add_edit_fragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.btn_save)
        {
            createNote();
            Navigation.findNavController(view)
                    .navigate(R.id.action_addEditNoteFragment_to_notesFragment);
            return true;
        }
        else
        {
            return false;
        }

    }
    private void defineViews()
    {
        noteTitle = view.findViewById(R.id.new_note_title);
        noteBody = view.findViewById(R.id.note_Text);
    }

    private void defineObservers(){

    }

    private void createNote()
    {
        if(currentNote == null)
        {
            currentNote = new Note(
                    noteTitle.getEditText().getText().toString().trim(),
                    noteBody.getText().toString().trim(),
                    AndromedaDate.getTodaysDate()
            );
            int noteId = (int) noteViewModel.upsertNote(currentNote);
            currentNote.setNoteId(noteId);
        }
        else
        {
            // TODO don't forget to update your current note's properties with whatever data the
            //  user has changed in the UI before upserting it!
            currentNote.setTitle(noteTitle.getEditText().getText().toString().trim());
            currentNote.setBody(noteBody.getText().toString().trim());
            noteViewModel.upsertNote(currentNote);
        }
    }
}
