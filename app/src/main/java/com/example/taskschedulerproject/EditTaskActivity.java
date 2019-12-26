package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditTaskActivity extends AppCompatActivity {
    FloatingActionButton finish;
    EditText TaskTitle,TaskDiscription,DatePicked,TimePicked;
    ImageButton calenderButton,timeButton;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    ArrayList<String> priorities;
    ArrayAdapter<String> adapter;
    Spinner priority;
    Calendar c;
    int priorityId=0,MyDay=-1,MyMonth=-1,MyYear=-1,MyHour=-1,MyMin=-1;

    private TaskItem selectedTaskItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

//        selectedTaskItem = (TaskItem) getIntent().getSerializableExtra("SELECTEDTASK");
        timeButton = findViewById(R.id.timeButton);
        priority = findViewById(R.id.PrioritySpinner);
        DatePicked = findViewById(R.id.dateEditText);
        TimePicked = findViewById(R.id.timeEdittext);
        TaskTitle = findViewById(R.id.NoteTitleEdittext);
        TaskDiscription = findViewById(R.id.NoteDiscriptionEdittext);
        calenderButton = findViewById(R.id.calenderButton);
        priority = findViewById(R.id.PrioritySpinner);
        priorities = new ArrayList<>(3);
        priorities.add("Low");
        priorities.add("Medium");
        priorities.add("High");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,priorities);
        finish = findViewById(R.id.NoteFAB);
        priority.setAdapter(adapter);


//        TaskTitle.setText(selectedTaskItem.getTitle());
//        TaskDiscription.setText(selectedTaskItem.getDescription());
//        priority.setSelection(selectedTaskItem.getPriority());


        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priorityId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                priorityId = 0;
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mHours = c.get(Calendar.HOUR_OF_DAY);
                int mMins = c.get(Calendar.MINUTE);
                MyHour = mHours;
                MyMin = mMins;
                tpd = new TimePickerDialog(EditTaskActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                        TimePicked.setText(hours+":"+mins);
                    }
                },mHours,mMins,true);
                tpd.show();

            }
        });
        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                MyDay = day;
                MyMonth = month;
                MyYear = year;
                dpd = new DatePickerDialog(EditTaskActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        DatePicked.setText(i+"/"+(i1+1)+"/"+i2);
                    }
                },year,month,day);
                dpd.show();

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TaskItem task =  new TaskItem(TaskTitle.getText().toString(),TaskDiscription.getText().toString(),null,priorityId,0,null);
//                selectedTaskItem.setTitle(TaskTitle.getText().toString());
//                selectedTaskItem.setDescription(TaskDiscription.getText().toString());
//                selectedTaskItem.setPriority(priorityId);
//                selectedTaskItem.setDeadline(null);
//                selectedTaskItem.setDuration(0);
//                Intent intent = new Intent(getApplicationContext(),ItemsActivity.class);
//                intent.putExtra("Edited Task",task);
//                setResult(RESULT_OK, intent);
                if(TaskTitle.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Title written, Please go set a title",Toast.LENGTH_SHORT).show();
                if(TaskDiscription.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"No Descrtption written, Please go set a descritpion",Toast.LENGTH_SHORT).show();
                if(MyMonth == -1)
                    Toast.makeText(getApplicationContext(),"No Date Picked, Please go set a date.",Toast.LENGTH_SHORT).show();
                if(MyMin == -1)
                    Toast.makeText(getApplicationContext(),"No Time Picked, Please go set a time.",Toast.LENGTH_SHORT).show();
                if(MyMonth != -1 && MyMin != -1 && !TaskTitle.getText().toString().isEmpty() && !TaskDiscription.getText().toString().isEmpty())
                    finished();
            }
        });

    }


    public void finished(){
        finish();
    }

}
