package com.johnny.todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.johnny.todo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.johnny.todo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.johnny.todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME = "com.johnny.todo.EXTRA_TIME";

    private EditText editTextTile;
    private EditText editTextDescription;
    private EditText datePicker;
    private EditText timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTile = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Task");
            editTextTile.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            LocalDateTime temp = (LocalDateTime) intent.getSerializableExtra(EXTRA_TIME);
            String date = temp.getDayOfMonth() + ", " + temp.getMonth();
            String hours = temp.getHour() + "h: " + temp.getMinute()+ "m";
            datePicker.setText(date);
            timePicker.setText(hours);
        }else{
            setTitle("Add Task");
        }
    }

    private void saveTask(){
        String title = editTextTile.getText().toString();
        String description = editTextDescription.getText().toString();
    }
}
