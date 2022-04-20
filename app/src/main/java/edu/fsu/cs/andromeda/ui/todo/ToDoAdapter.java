package edu.fsu.cs.andromeda.ui.todo;

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
import edu.fsu.cs.andromeda.db.todo.ToDo;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private OnItemClickListener listener;

    private List<ToDo> toDosByDate = new ArrayList<>();

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_to_do, parent, false);

        return new ToDoAdapter.ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo currentToDo = toDosByDate.get(position);
        holder.tvToDoTitle.setText(currentToDo.getTitle());
        if(currentToDo.getBody().isEmpty()) {
            holder.tvToDoBody.setVisibility(View.GONE);
        } else {
            holder.tvToDoBody.setVisibility(View.VISIBLE);
            holder.tvToDoBody.setText(currentToDo.getBody());
        }
        holder.tvToDoDueDate.setText(formatDatePretty(currentToDo.getDueDate()));
    }

    public static String formatDatePretty(String dateTimeString){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatter = new SimpleDateFormat("EEE MMM d, yyyy - HH:mm:ss");
        assert date != null;
        return formatter.format(date);
    }

    @Override
    public int getItemCount() {
        return toDosByDate.size();
    }

    public ToDo getToDoAtPosition(int position) {
        return toDosByDate.get(position);
    }

    public void setToDosByDate(List<ToDo> toDosByDate){
        this.toDosByDate = toDosByDate;
        notifyDataSetChanged();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvToDoTitle;
        private TextView tvToDoBody;
        private TextView tvToDoDueDate;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvToDoTitle = itemView.findViewById(R.id.tv_to_do_title);
            tvToDoBody = itemView.findViewById(R.id.tv_to_do_body);
            tvToDoDueDate = itemView.findViewById(R.id.tv_to_do_due_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(toDosByDate.get(position), view);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ToDo toDo, View view);
    }
}


