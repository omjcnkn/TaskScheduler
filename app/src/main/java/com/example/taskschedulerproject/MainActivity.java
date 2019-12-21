package com.example.taskschedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ItemListFragment.OnListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_container) != null) {
            ItemListFragment itemListFragment = new ItemListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, itemListFragment).commit();
        }
    }

    public void onListClick(int position) {
        ItemsFragment itemsFragment = new ItemsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, itemsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
