package com.johnny.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTile = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);

        editTextDescription.setText("");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Task");
            editTextTile.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            LocalDateTime temp = (LocalDateTime) intent.getSerializableExtra(EXTRA_TIME);
            String hours = temp.getHour() + "h: " + temp.getMinute()+ "m";
            datePicker.updateDate(temp.getYear(), temp.getMonthValue(), temp.getDayOfMonth());
            timePicker.setHour(temp.getHour());
            timePicker.setMinute(temp.getMinute());
        }else{
            setTitle("Add Task");
        }
    }

    private void saveTask(){
        String title = editTextTile.getText().toString();
        String description = editTextDescription.getText().toString();
        int year, month, day, hour, minute;
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);

        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_TIME, time);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_task:
                saveTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
