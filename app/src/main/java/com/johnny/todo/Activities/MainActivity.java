package com.johnny.todo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import com.johnny.todo.Fragments.SnoozeFragment;
import com.johnny.todo.Fragments.TasksDisplayFragment;
import com.johnny.todo.R;
import com.johnny.todo.Room.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    public static final String EXTRA_ID = "com.johnny.todo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.johnny.todo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.johnny.todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME = "com.johnny.todo.EXTRA_TIME";
    public static final String EXTRA_ALARM = "com.johnny.todo.EXTRA_ALARM";


    public static final String Notification_Description = "com.johnny.todo.NOTIFICATION_DESCRIPTION";
    public static final String Notification_Title = "com.johnny.todo.NOTIFICATION_TITLE";
    public static final String Notification_Id = "com.johnny.todo.NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new TasksDisplayFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = intent.getIntExtra(Notification_Id, -1);
        if(id != -1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new SnoozeFragment())
                    .commit();
        }
    }


}
