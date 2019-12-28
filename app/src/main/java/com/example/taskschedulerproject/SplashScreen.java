package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_SCREEN_DELAY = 1000;

    private TextView usernameTextView;
    private EditText usernameEditText;
    private Button registerButton;

    private UserBoard userBoard;
    private RewardingSystem rewardingSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initUI();
        initLogic();

        if(checkIfRegistered()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, SPLASH_SCREEN_DELAY);
        } else {
            showUI();
        }
    }

    private void initUI() {
        usernameTextView = findViewById(R.id.usernameTextView);
        usernameEditText = findViewById(R.id.usernameEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
//                SharedPreferences sharedPreferences = getSharedPreferences(UserBoard.AppPREFERNCE, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(UserBoard.Username, username);
//                editor.commit();
                userBoard.setUsername(username);

                startMainActivity();
            }
        });
    }

    private void showUI() {
        usernameTextView.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);

        usernameEditText.setEnabled(true);
        registerButton.setEnabled(true);
    }

    private void initLogic() {
        userBoard = UserBoard.getUserBoard(getApplicationContext());
        rewardingSystem = RewardingSystem.getRewardingSystem(userBoard , getApplicationContext());
    }

    private boolean checkIfRegistered() {
//        SharedPreferences sharedPreferences = getSharedPreferences(UserBoard.AppPREFERNCE, Context.MODE_PRIVATE);
//        String username = sharedPreferences.getString(UserBoard.Username, null);
        String username = userBoard.getUsername();

        if(username == null) {
            return false;
        }

        return true;
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(mainIntent);
        SplashScreen.this.finish();
    }
}
