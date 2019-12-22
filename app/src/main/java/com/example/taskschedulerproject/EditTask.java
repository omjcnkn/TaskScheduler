package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditTask extends AppCompatActivity {
    ArrayList<String> priorities;
    ArrayAdapter<String> adapter;
    Spinner priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        priority = findViewById(R.id.PrioritySpinner);
        priorities = new ArrayList<>(3);
        priorities.add("Low");
        priorities.add("Medium");
        priorities.add("High");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,priorities);
        priority.setAdapter(adapter);


    }

}
