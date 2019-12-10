package com.johnny.todo.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import com.johnny.todo.Fragments.SnoozeFragment;
import com.johnny.todo.Fragments.TasksDisplayFragment;
import com.johnny.todo.R;
import com.johnny.todo.Room.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;

    public static final String TASK_DISPLAY = "com.johnny.todo.TASK_DISPLAY";

    public static final String EXTRA_ID = "com.johnny.todo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.johnny.todo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.johnny.todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME = "com.johnny.todo.EXTRA_TIME";
    public static final String EXTRA_ALARM = "com.johnny.todo.EXTRA_ALARM";


    public static final String Notification_Description = "com.johnny.todo.NOTIFICATION_DESCRIPTION";
    public static final String Notification_Title = "com.johnny.todo.NOTIFICATION_TITLE";
    public static final String Notification_Id = "com.johnny.todo.NOTIFICATION_ID";
    public static final String Notification_Minutes = "com.johnny.todo.Notification_Minutes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new TasksDisplayFragment(), TASK_DISPLAY)
                .commit();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = intent.getIntExtra(Notification_Id, -1);
        String title = intent.getStringExtra(Notification_Title);
        String description = intent.getStringExtra(Notification_Description);
        Fragment snoozeFragment = new SnoozeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, id);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_DESCRIPTION, description);
        snoozeFragment.setArguments(bundle);
        if(id != -1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, snoozeFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }
}
