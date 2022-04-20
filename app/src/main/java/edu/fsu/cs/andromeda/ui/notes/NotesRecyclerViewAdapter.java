package edu.fsu.cs.andromeda.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.note.Note;

//Does not take user input yet. Focusing on design.
//Reference: https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
//Populates the recycler view with data
public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater Inflater;
    private List<String> notesData;
    private List<Note> noteList = new ArrayList<>(); // this should be used to populate your recycler view list
    private OnItemClickListener listener;

    // TODO I would advise against using a constructor for this adapter, it isn't necessary from what I can see thus far for your use case,
    //     but let me know if I am wrong, and if you have a reason for doing the above we can change it and adapt to the existing implementation
//    NotesRecyclerViewAdapter(Context context, List<String> data)
//    {
//        this.Inflater = LayoutInflater.from(context);
//        this.notesData = data;
//    }
    

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = Inflater.inflate(R.layout.item_notes, parent, false);
//        return new ViewHolder(view); // TODO again, since we don't use the constructor, we can just create the viewholder as below
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);

        return new NotesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String testNotes = notesData.get(position);
        Note currentNote = noteList.get(position);
        holder.noteTitle.setText(currentNote.getTitle()); // TODO set the rest of the views as necessary (are we displaying the body of the note & its creation date, similar to the ToDoAdapter maybe?)
        if(currentNote.getBody().isEmpty())
        {
            holder.noteBody.setVisibility(View.GONE);
        }
        else
        {
            holder.noteBody.setVisibility(View.VISIBLE);
            holder.noteBody.setText(currentNote.getBody());
        }
        holder.noteDate.setText(formatDate(currentNote.getDateCreated()));
    }

    public static String formatDate(String dateStr)
    {
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
            date = (Date) parser.parse(dateStr);
        } catch(ParseException e)
        {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy - HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    public Note getNote(int pos)
    {
        return noteList.get(pos);
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged(); //  needed to inform our RV that the data has changed through the observer, so we use this method
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView noteTitle;
        private TextView noteBody;
        private TextView noteDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notesRVtitle);
            noteBody = itemView.findViewById(R.id.notesRVbody);
            noteDate = itemView.findViewById(R.id.notesRVdate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(noteList.get(position), v);
                    }
                }
            });
        }
    }

    // called from the fragment that uses this adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Note note, View view);
    }
}
