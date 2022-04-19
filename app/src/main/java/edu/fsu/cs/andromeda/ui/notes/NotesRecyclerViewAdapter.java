package edu.fsu.cs.andromeda.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.fsu.cs.andromeda.R;

//Does not take user input yet. Focusing on design.
//Reference: https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
//Populates the recycler view with data
public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater Inflater;
    private List<String> notesData;

    NotesRecyclerViewAdapter(Context context, List<String> data)
    {
        this.Inflater = LayoutInflater.from(context);
        this.notesData = data;
    }
    

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String testNotes = notesData.get(position);
        holder.textView.setText(testNotes);

    }

    @Override
    public int getItemCount() {
        return notesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.notesRVItem);

        }
    }



}
