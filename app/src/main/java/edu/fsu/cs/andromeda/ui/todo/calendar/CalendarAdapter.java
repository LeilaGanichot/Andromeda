package edu.fsu.cs.andromeda.ui.todo.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.fsu.cs.andromeda.R;
import edu.fsu.cs.andromeda.db.todo.ToDo;
import edu.fsu.cs.andromeda.util.AndromedaDate;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private String month;
    private OnItemClickListener listener;

    private List<ToDo> allToDos = new ArrayList<>();

    public CalendarAdapter(ArrayList<String> daysOfMonth, String month) {
        this.daysOfMonth = daysOfMonth;
        this.month = month;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_cell, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666); // get each cell to be exactly 1/6th of the view

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.tvDayOfMonth.setText(daysOfMonth.get(position));


        if(!allToDos.isEmpty()) setDueDateIndicator(holder, position);
    }

    private void setDueDateIndicator(CalendarViewHolder holder, int position) {
        for (ToDo toDo: allToDos) {
            if(toDo.getDueDate() != null){
                if(AndromedaDate.extractDayFromDate(toDo.getDueDate(), month).equals(daysOfMonth.get(position))){
                    holder.tvDueDateIndicator.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void setAllToDos(List<ToDo> allToDos){
        this.allToDos = allToDos;
        notifyDataSetChanged();
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDayOfMonth;
        private TextView tvDueDateIndicator;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDayOfMonth = itemView.findViewById(R.id.tv_cell_day_text);
            tvDueDateIndicator = itemView.findViewById(R.id.tv_to_do_indicator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition(), tvDayOfMonth.getText().toString());
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String dayText);
    }
}

