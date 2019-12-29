package com.example.taskschedulerproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TaskObserver {
    DrawerLayout drawer;
    private NavigationView navigationView;

    TextView username;
    TextView level;
    TextView points;
    ImageView currentbadge;

    View header;

    private UserBoard userBoard;

    ArrayList<Integer> badge = new ArrayList<>(Arrays.asList(R.drawable.b1 , R.drawable.b2 , R.drawable.b3 , R.drawable.b4,
            R.drawable.b5));


    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "id1")
                .setShortLabel("Home")
                .setLongLabel("List of Tasks")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_home_black_24dp))
                .setIntent(new Intent(MainActivity.this, MainActivity.class).setAction(Intent.ACTION_VIEW))
                .build();

        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut1));

        userBoard = UserBoard.getUserBoard(getApplicationContext());
        userBoard.subscribe(this);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        currentbadge = header.findViewById(R.id.badge);
        username = header.findViewById(R.id.username);
        level = header.findViewById(R.id.level);
        points = header.findViewById(R.id.points);


        username.setText(userBoard.getUsername());
        points.setText("Points: " +userBoard.getPoints() + "");
        level.setText("Level: " +userBoard.getLevel() + "");
        currentbadge.setImageResource( badge.get(userBoard.getLevel()-1));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_profile);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof ProfileFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onNotify() {

        username.setText(userBoard.getUsername());
        points.setText("Points: " +userBoard.getPoints() + "");
        level.setText("Level: " +userBoard.getLevel() + "");
        currentbadge.setImageResource( badge.get(userBoard.getLevel()-1));
    }


//    public class NavigationViewerObserver implements  TaskObserver{
//
//        private UserBoard userBoard;
//
//        ArrayList<Integer> badge = new ArrayList<>(Arrays.asList(R.drawable.b1 , R.drawable.b2 , R.drawable.b3 , R.drawable.b4,
//            R.drawable.b5));
//
//        private String username;
//        private int pointState;
//        private int levelState;
//
//        private Context context;
//
//        private SharedPreferences sharedPreferences;
//
////        public static final String APPPEREFERENCE = "app";
//
//
//
//        public NavigationViewerObserver(UserBoard userBoard) {
////            this.context = context;
//
//            this.userBoard = userBoard;
//            userBoard.subscribe(this);
//
//            pointState = userBoard.getPoints();
//
//        }
//
//        @Override
//        public void onNotify() {
//
//        }
//    }
}
