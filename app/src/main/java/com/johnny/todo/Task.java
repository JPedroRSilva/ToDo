package com.johnny.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static com.johnny.todo.LocalDateTimeConverter.toDate;
import static com.johnny.todo.LocalDateTimeConverter.toDateString;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private String time;

    private boolean alarmOn;

    public Task(String title, String description, String time, boolean alarmOn) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.alarmOn = alarmOn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return this.time;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public String timeToFormatedString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd/MM/yyyy 'at' HH:mm");
        LocalDateTime formatedTime = toDate(this.time);
        return formatter.format(formatedTime);
    }
}
