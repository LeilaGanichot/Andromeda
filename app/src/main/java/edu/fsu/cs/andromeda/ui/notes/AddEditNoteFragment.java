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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.note.Note;
import edu.fsu.cs.andromeda.db.note.NoteViewModel;

public class AddEditNoteFragment extends Fragment {
    private Note currentNote = null;
    private NoteViewModel noteViewModel;
    //widgets
    private View view;
    private Button btnSave;
    private TextInputLayout noteTitle;
    private EditText noteBody;

    public AddEditNoteFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.new_note, container, false);
        return view;
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
            // TODO add navigation
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
                    "title",
                    "body",
                    "2022-04-17 12:50:00"
            );
            int noteId = (int) noteViewModel.upsertNote(currentNote);
            currentNote.setNoteId(noteId);
        }
        else
        {
            noteViewModel.upsertNote(currentNote);
        }
    }
}
