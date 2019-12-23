package com.example.taskschedulerproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class EditTaskActivity extends AppCompatActivity {
    FloatingActionButton finish;
    EditText TaskTitle,TaskDiscription,DatePicked;
    ImageButton calenderButton;
    DatePickerDialog dpd;

    ArrayList<String> priorities;
    ArrayAdapter<String> adapter;
    Spinner priority;
    Calendar c;
    int priorityId=0,MyDay,MyMonth,MyYear;

    private TaskItem selectedTaskItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        selectedTaskItem = (TaskItem) getIntent().getSerializableExtra("SELECTEDTASK");

        priority = findViewById(R.id.PrioritySpinner);
        DatePicked = findViewById(R.id.dateEditText);
        TaskTitle = findViewById(R.id.TaskTitleEdittext);
        TaskDiscription = findViewById(R.id.TaskDiscriptionEdittext);
        calenderButton = findViewById(R.id.calenderButton);
        priority = findViewById(R.id.PrioritySpinner);
        priorities = new ArrayList<>(3);
        priorities.add("Low");
        priorities.add("Medium");
        priorities.add("High");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,priorities);
        finish = findViewById(R.id.finishFloatingActionButton);
        priority.setAdapter(adapter);


        TaskTitle.setText(selectedTaskItem.getTitle());
        TaskDiscription.setText(selectedTaskItem.getDescription());
        priority.setSelection(selectedTaskItem.getPriority());


        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priorityId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day= c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int ChoosedYear, int ChoosedMonth, int ChoosedDay) {
                        MyDay=ChoosedDay;
                        MyMonth = ChoosedMonth;
                        MyYear = ChoosedYear;
                        DatePicked.setText(ChoosedDay +"/"+ChoosedMonth+"/"+ChoosedYear);
                    }
                },day,month,year);



            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItem task =  new TaskItem(TaskTitle.getText().toString(),TaskDiscription.getText().toString(),null,priorityId,0,null);
                selectedTaskItem.setTitle(TaskTitle.getText().toString());
                selectedTaskItem.setDescription(TaskDiscription.getText().toString());
                selectedTaskItem.setPriority(priorityId);
                selectedTaskItem.setDeadline(null);
                selectedTaskItem.setDuration(0);
                Intent intent = new Intent(getApplicationContext(),ItemsActivity.class);
                intent.putExtra("Edited Task",task);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
