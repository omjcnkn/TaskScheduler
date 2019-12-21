package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_container) != null) {
            ItemListFragment itemListFragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, itemListFragment).commit();
        }
    }
}
