package com.johnny.todo.Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.johnny.todo.Activities.MainActivity;
import com.johnny.todo.Notifications.ReminderReceiver;
import com.johnny.todo.R;
import com.johnny.todo.Room.Task;
import com.johnny.todo.Room.TaskViewModel;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.johnny.todo.Activities.MainActivity.EXTRA_ALARM;
import static com.johnny.todo.Activities.MainActivity.EXTRA_DESCRIPTION;
import static com.johnny.todo.Activities.MainActivity.EXTRA_TIME;
import static com.johnny.todo.Activities.MainActivity.EXTRA_TITLE;
import static com.johnny.todo.Activities.MainActivity.Notification_Description;
import static com.johnny.todo.Activities.MainActivity.Notification_Id;
import static com.johnny.todo.Activities.MainActivity.Notification_Title;
import static com.johnny.todo.Activities.MainActivity.TASK_DISPLAY;
import static com.johnny.todo.Room.LocalDateTimeConverter.toDateString;

public class AddEditTaskFragment extends Fragment {
    private TaskViewModel taskViewModel;

    private EditText editTextTile;
    private EditText editTextDescription;
    private Switch reminderSwitch;
    private TextView datePicker;
    private TextView timePicker;
    private LocalDateTime tempTime;
    private String title;
    private String description;
    private boolean isAlarmOn;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

        Activity activity = getActivity();
        editTextTile = activity.findViewById(R.id.edit_text_title);
        editTextDescription = activity.findViewById(R.id.edit_text_description);
        reminderSwitch = activity.findViewById(R.id.switch_setReminder);
        datePicker = activity.findViewById(R.id.date_picker);
        timePicker = activity.findViewById(R.id.time_picker);
        editTextDescription.setText("");
        tempTime = LocalDateTime.now();

        Bundle data = this.getArguments();
        id = data.getInt(MainActivity.EXTRA_ID, -1);

        if(id != -1){
            title = data.getString(EXTRA_TITLE);
            description = data.getString(EXTRA_DESCRIPTION);
            tempTime = (LocalDateTime) data.getSerializable(EXTRA_TIME);
            isAlarmOn = data.getBoolean(EXTRA_ALARM);

            activity.setTitle("Edit Task");
            editTextTile.setText(title);
            editTextDescription.setText(description);
            String hours = tempTime.getHour() + "h: " + tempTime.getMinute()+ "m";
            String date = tempTime.getDayOfMonth() + "/" + tempTime.getMonthValue()+ "/" + tempTime.getYear();
            datePicker.setText(date);
            timePicker.setText(hours);
            datePicker.setVisibility(isAlarmOn ? View.VISIBLE : View.INVISIBLE);
            timePicker.setVisibility(isAlarmOn ? View.VISIBLE : View.INVISIBLE);
            reminderSwitch.setChecked(isAlarmOn);
        }else {
            activity.setTitle("Add Task");
            LocalDateTime temp = LocalDateTime.now();
            String hours = temp.getHour() + "h: " + temp.getMinute()+ "m";
            String date = temp.getDayOfMonth() + "/" + temp.getMonthValue()+ "/" + temp.getYear();
            datePicker.setVisibility(View.INVISIBLE);
            timePicker.setVisibility(View.INVISIBLE);
            datePicker.setText(date);
            timePicker.setText(hours);
            reminderSwitch.setChecked(false);
        }
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
                int year, month, day;
                if(id != -1){
                    if(LocalDateTime.now().isAfter(tempTime)){
                        LocalDateTime aux = LocalDateTime.now();
                        tempTime = tempTime.withYear(aux.getYear());
                        tempTime = tempTime.withMonth(aux.getMonthValue());
                        tempTime = tempTime.withDayOfMonth(aux.getDayOfMonth());
                    }
                }else{
                    LocalDateTime aux = LocalDateTime.now();
                    tempTime = tempTime.withYear(aux.getYear());
                    tempTime = tempTime.withMonth(aux.getMonthValue());
                    tempTime = tempTime.withDayOfMonth(aux.getDayOfMonth());
                }
                year = tempTime.getYear();
                month = tempTime.getMonthValue();
                day = tempTime.getDayOfMonth();
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(getContext() ,new DatePickerDialog.OnDateSetListener() {
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id != -1){
                    if(LocalDateTime.now().isAfter(tempTime)){
                        LocalDateTime aux = LocalDateTime.now();
                        tempTime = tempTime.withMinute(aux.getMinute());
                        tempTime = tempTime.withHour(aux.getHour());
                    }
                }else{
                    LocalDateTime aux = LocalDateTime.now();
                    tempTime = tempTime.withMinute(aux.getMinute());
                    tempTime = tempTime.withHour(aux.getHour());
                }
                int hours, minutes;
                hours = tempTime.getHour();
                minutes = tempTime.getMinute();
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + "h:" + minute;
                        tempTime = tempTime.withHour(hourOfDay);
                        tempTime = tempTime.withMinute(minute);
                        tempTime = tempTime.withSecond(0);
                        timePicker.setText(time);
                    }
                }, hours, minutes+1, DateFormat.is24HourFormat(getContext()));
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
            Toast.makeText(getContext(), "Please insert a title",Toast.LENGTH_SHORT).show();
            return;
        }

        if(alarmOn && tempTime.isBefore(LocalDateTime.now())){
            Toast.makeText(getContext(), "Please choose a time and date in the future", Toast.LENGTH_SHORT).show();
            return;
        }

        Task editedTask = new Task(title, description, toDateString(tempTime), alarmOn);
        if(id != -1){
            if(alarmOn){
                setReminder(id, tempTime, title, description);
            }else{
                if(isAlarmOn){
                    cancelReminder(id);
                }
            }
            editedTask.setId(id);
            taskViewModel.update(editedTask);
        }else{
            taskViewModel.insert(editedTask);
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        TasksDisplayFragment tasksDisplayFragment = (TasksDisplayFragment) fragmentManager.findFragmentByTag(TASK_DISPLAY);

        if(tasksDisplayFragment != null){
            fragmentManager.beginTransaction().remove(tasksDisplayFragment).commit();
            fragmentManager.popBackStack();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, new TasksDisplayFragment(), TASK_DISPLAY)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_task_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    public void setReminder(long Lid, LocalDateTime reminder, String title , String description){
        int id = (int) Lid;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        intent.putExtra(Notification_Title, title);
        intent.putExtra(Notification_Description, description);
        intent.putExtra(Notification_Id, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(), pendingIntent);
    }

    public void cancelReminder(int id){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
