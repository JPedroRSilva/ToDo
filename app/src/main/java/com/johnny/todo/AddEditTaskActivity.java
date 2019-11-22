package com.johnny.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
    public static final String EXTRA_ALARM = "com.johnny.todo.EXTRA_ALARM";

    private EditText editTextTile;
    private EditText editTextDescription;
    private Switch reminderSwitch;
    private TextView datePicker;
    private TextView timePicker;
    private LocalDateTime tempTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        editTextTile = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        reminderSwitch = findViewById(R.id.switch_setReminder);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        editTextDescription.setText("");
        tempTime = LocalDateTime.now();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        final Intent intent = getIntent();

        if(!intent.getBooleanExtra(EXTRA_ALARM, false)){
            datePicker.setVisibility(View.INVISIBLE);
            timePicker.setVisibility(View.INVISIBLE);
        }
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Task");
            editTextTile.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            LocalDateTime temp = (LocalDateTime) intent.getSerializableExtra(EXTRA_TIME);
            String hours = temp.getHour() + "h: " + temp.getMinute()+ "m";
            String date = temp.getDayOfMonth() + "/" + temp.getMonthValue()+ "/" + temp.getYear();
            datePicker.setText(date);
            timePicker.setText(hours);
        }else{
            setTitle("Add Task");
        }
        reminderSwitch.setChecked(intent.getBooleanExtra(EXTRA_ALARM, false));
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }
                else{
                    datePicker.setVisibility(View.INVISIBLE);
                    timePicker.setVisibility(View.INVISIBLE);
                }
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alarmOn = intent.getBooleanExtra(EXTRA_ALARM, false);
                int year, month, day;
                if(alarmOn){
                    tempTime = (LocalDateTime) intent.getSerializableExtra(EXTRA_TIME);
                }
                else {
                    tempTime = LocalDateTime.now();
                }
                year = tempTime.getYear();
                month = tempTime.getMonthValue();
                day = tempTime.getDayOfMonth();
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(AddEditTaskActivity.this ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month+1) + "/" + year;
                        tempTime = tempTime.withDayOfMonth(dayOfMonth);
                        tempTime = tempTime.withMonth(month+1);
                        tempTime = tempTime.withYear(year);
                        datePicker.setText(date);
                    }
                }, year, month-1, day);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alarmOn = intent.getBooleanExtra(EXTRA_ALARM, false);
                if(alarmOn){
                    tempTime = (LocalDateTime) intent.getSerializableExtra(EXTRA_TIME);
                }else{
                   tempTime = LocalDateTime.now();
                }
                int hours, minutes;
                hours = tempTime.getHour();
                minutes = tempTime.getMinute();
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(AddEditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + "h:" + minute;
                        tempTime = tempTime.withHour(hourOfDay);
                        tempTime = tempTime.withMinute(minute);
                        timePicker.setText(time);
                    }
                }, hours, minutes, true);
                timePickerDialog.setTitle("Select hour and minutes");
                timePickerDialog.show();
            }
        });
    }

    private void saveTask(){
        String title = editTextTile.getText().toString();
        String description = editTextDescription.getText().toString();
        boolean alarmOn = reminderSwitch.isChecked();

        if(title.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_TIME, tempTime);
        data.putExtra(EXTRA_ALARM, alarmOn);
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
