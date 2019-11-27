package com.johnny.todo.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.johnny.todo.R;
import com.johnny.todo.Room.TaskViewModel;

public class SnoozeActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.snooze);

        Toast.makeText(this, "Teste", Toast.LENGTH_SHORT).show();
    }
}
