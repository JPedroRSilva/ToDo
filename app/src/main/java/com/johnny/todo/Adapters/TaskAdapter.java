package com.johnny.todo.Adapters;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.johnny.todo.R;
import com.johnny.todo.Room.Task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.johnny.todo.Room.LocalDateTimeConverter.toDate;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    private OnItemClickListener listener;

    public TaskAdapter(){
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getTime().equals(newItem.getTime()) &&
                    (oldItem.isAlarmOn()==newItem.isAlarmOn());
        }


    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent,false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task currentTask = getItem(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescrition.setText(currentTask.getDescription());
        if(currentTask.isAlarmOn()) {
            LocalDateTime teste =  toDate(currentTask.getTime());
            if(DateUtils.isToday(teste.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli())){
                String res = "Today, " + teste.getHour() + "h:" + teste.getMinute() + "m";
                holder.textViewTime.setText(res);
            }
            else{
                holder.textViewTime.setText(currentTask.timeToFormatedString());
            }
        }else{
            holder.textViewTime.setText("");
        }
    }

    class TaskHolder extends RecyclerView.ViewHolder{

        private TextView textViewTitle;
        private TextView textViewDescrition;
        private TextView textViewTime;

        public TaskHolder(View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_item_title);
            textViewDescrition = itemView.findViewById(R.id.text_item_description);
            textViewTime = itemView.findViewById(R.id.text_item_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });


        }
    }



    public Task getTaskAt(int position){
        return getItem(position);
    }

    public interface OnItemClickListener{
        void onItemClick(Task task);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
