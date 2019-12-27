package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskProgressActivity extends AppCompatActivity {
    private ProgressBar taskProgressBar;
    private FloatingActionButton doneFloatingActionButton;

    private DatabaseHelper dbh;

    private CountDownTimer countDownTimer;

    private String currentTaskName;

    private TextView timer;

    private Cursor taskCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress);

        initUI();
        initLogic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    private void initLogic() {
        dbh = new DatabaseHelper(getApplicationContext());

        currentTaskName = getIntent().getStringExtra("TASK_NAME");

        getSupportActionBar().setTitle(currentTaskName);

        taskCursor = dbh.getTaskItem(currentTaskName);
        taskCursor.moveToFirst();

        final String taskDuration = taskCursor.getString(taskCursor.getColumnIndex("TaskDuration"));
        final int taskDurationInMillis = parseDuration(taskDuration);
        Log.e("Task Duration", taskDurationInMillis + "");
        taskProgressBar.setMax(taskDurationInMillis);
        taskProgressBar.setProgress(taskDurationInMillis);
        timer.setText(millisecondsConverter(taskDurationInMillis));

        /* 100 000 --> 1
            60 000 --> x
         */
        countDownTimer = new CountDownTimer(taskDurationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("PROGRESS_BAR", ((taskProgressBar.getProgress() * 100) - (int) (taskDurationInMillis * 1.0 / 100000) * 100) + "");
                taskProgressBar.setProgress(taskProgressBar.getProgress() - 1000);
                timer.setText(millisecondsConverter(taskProgressBar.getProgress()));

            }

            @Override
            public void onFinish() {
                finishUI();
            }
        }.start();
    }

    private void initUI() {
        taskProgressBar = findViewById(R.id.taskProgressBar);
        timer = findViewById(R.id.timerTextView);
        doneFloatingActionButton = findViewById(R.id.doneFloatingActionButton);

        doneFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask();

                finish();
            }
        });
    }

    private int parseDuration(String duration) {
        String hh = duration.substring(0, 2);
        String mm = duration.substring(3);

        int durationInMilliSeconds = Integer.parseInt(hh) * 60 * 60 * 1000;
        durationInMilliSeconds += (Integer.parseInt(mm) * 60 * 1000);

        return durationInMilliSeconds;
    }

    private String millisecondsConverter(long ms) {
        long totalSecs = ms / 1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;


        String hrsString = (hours == 0)
                ? "00"
                : ((hours < 10)
                ? "0" + hours
                : "" + hours);

        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                ? "0" + mins
                : "" + mins);


        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                ? "0" + secs
                : "" + secs);


        return hrsString + ":" + minsString + ":" + secsString;
    }

    private void finishUI () {
        taskProgressBar.setProgress(0);
        timer.setText("00:00:00");
    }

    private void updateTask() {
        try {
            String listName = taskCursor.getString(taskCursor.getColumnIndex("List"));
            String checkListName = taskCursor.getString(taskCursor.getColumnIndex("TaskTitle"));
            String checkListDescription = taskCursor.getString(taskCursor.getColumnIndex("TaskDescription"));
            String creationDate = taskCursor.getString(taskCursor.getColumnIndex("TaskDate"));
            int priority = taskCursor.getInt(taskCursor.getColumnIndex("TaskPriority"));
            String duration = taskCursor.getString(taskCursor.getColumnIndex("TaskDuration"));
            String deadline = taskCursor.getString(taskCursor.getColumnIndex("TaskDeadline"));
            int checked = 1;

            dbh.updateCheckListItem(checkListName, listName, checkListName, checkListDescription,
                    creationDate, priority, duration, deadline, checked);

            RewardingSystem.getRewardingSystem(UserBoard.getUserBoard(getApplicationContext())).rewardCheckingTask(priority);

            setResult(RESULT_OK);
        } catch(SQLException ex) {
            Toast.makeText(TaskProgressActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
        }
    }
}
