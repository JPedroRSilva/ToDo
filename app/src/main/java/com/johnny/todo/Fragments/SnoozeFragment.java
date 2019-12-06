package com.johnny.todo.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.johnny.todo.R;
import com.johnny.todo.Room.TaskViewModel;

public class SnoozeFragment extends Fragment {

    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.snooze, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        Toast.makeText(getContext(), "Fragment", Toast.LENGTH_SHORT).show();
    }
}
