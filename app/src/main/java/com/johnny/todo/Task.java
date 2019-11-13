package com.johnny.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private LocalDateTime time;

    public Task(String title, String description, LocalDateTime time) {
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getTime() {
        return time;
    }

    public String timeToFormatedString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd/MM/yyyy 'at' HH:mm");
        return this.time.format(formatter);
    }
}
