package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class TaskProgressActivity extends AppCompatActivity {
    private ProgressBar taskProgressBar;

    private DatabaseHelper dbh;

    private CountDownTimer countDownTimer;

    private String currentTaskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);

        initUI();
        initLogic();
    }

    private void initLogic() {
        dbh = new DatabaseHelper(getApplicationContext());

        currentTaskName = getIntent().getStringExtra("TASK_NAME");
        Cursor c = dbh.getTaskItem(currentTaskName);
        c.moveToFirst();

        final String taskDuration = c.getString(c.getColumnIndex("TaskDuration"));
        final int taskDurationInMillis = parseDuration(taskDuration);

        countDownTimer = new CountDownTimer(taskDurationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                taskProgressBar.setProgress(taskProgressBar.getProgress() * 100 - (int)(taskDurationInMillis * 1.0/ 100000) * 100);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void initUI() {
        taskProgressBar = findViewById(R.id.taskProgressBar);
    }

    private int parseDuration(String duration) {
        String hh = duration.substring(0, 2);
        String mm = duration.substring(3);

        int durationInMilliSeconds = Integer.parseInt(hh) * 60 * 60 * 1000;
        durationInMilliSeconds += (Integer.parseInt(mm) * 60 * 1000);

        return durationInMilliSeconds;
    }
}
