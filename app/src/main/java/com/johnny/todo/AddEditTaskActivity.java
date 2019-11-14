package com.johnny.todo;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.johnny.todo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.johnny.todo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.johnny.todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME = "com.johnny.todo.EXTRA_TIME";

    private EditText editTextTile;
    private EditText ediiTextDescription;
    private EditText datePicker;
    private EditText timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
