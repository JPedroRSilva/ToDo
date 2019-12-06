package com.johnny.todo.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johnny.todo.Adapters.TaskAdapter;
import com.johnny.todo.Notifications.ReminderReceiver;
import com.johnny.todo.R;
import com.johnny.todo.Room.Task;
import com.johnny.todo.Room.TaskViewModel;
import java.util.List;

import static com.johnny.todo.Activities.MainActivity.EXTRA_ALARM;
import static com.johnny.todo.Activities.MainActivity.EXTRA_DESCRIPTION;
import static com.johnny.todo.Activities.MainActivity.EXTRA_TIME;
import static com.johnny.todo.Activities.MainActivity.EXTRA_TITLE;
import static com.johnny.todo.Activities.MainActivity.EXTRA_ID;
import static com.johnny.todo.Room.LocalDateTimeConverter.toDate;

public class TasksDisplayFragment extends Fragment {
    private TaskViewModel taskViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.taskdisplay, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        FloatingActionButton buttonAddTask = getView().findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putInt(EXTRA_ID, -1);
                AddEditTaskFragment fragment = new AddEditTaskFragment();
                fragment.setArguments(data);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);


        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Task task = adapter.getTaskAt(viewHolder.getAdapterPosition());
                if(task.isAlarmOn()){
                    cancelReminder(task.getId());
                }
                taskViewModel.delete(task);
                Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
            }


        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                AddEditTaskFragment fragment = new AddEditTaskFragment();
                Bundle data = new Bundle();
                data.putInt(EXTRA_ID, task.getId());
                data.putString(EXTRA_TITLE, task.getTitle());
                data.putString(EXTRA_DESCRIPTION, task.getDescription());
                data.putSerializable(EXTRA_TIME, toDate(task.getTime()));
                data.putBoolean(EXTRA_ALARM, task.isAlarmOn());
                fragment.setArguments(data);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_tasks:
                taskViewModel.deleteAllTasks();
                Toast.makeText(getContext(), "All tasks deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cancelReminder(int id){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
