package com.example.taskschedulerproject;

import android.content.Context;
import android.content.SharedPreferences;

public class UserBoard extends Subject {
    private static UserBoard userBoard;

    private String username;

    private int level = 1;
    private int points = 0;

    private String badge = "";

    private Context appContext;
    private SharedPreferences sharedPreferences;

    public static final String AppPREFERNCE = "app";
    public static final String Username = "username";
    public static final String USER_LEVEL = "level";
    public static final String USER_POINTS = "points";


    private UserBoard(Context context) {
        this.appContext = context;
        sharedPreferences = appContext.getSharedPreferences(AppPREFERNCE, Context.MODE_PRIVATE);

        sharedPreferences = context.getSharedPreferences(AppPREFERNCE, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Username, null);
        level = sharedPreferences.getInt(USER_LEVEL, 1);
        points = sharedPreferences.getInt(USER_POINTS, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        this.username = mUsername;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Username, username);
        editor.commit();

        notifyAllObservers();
    }

    public void incrementPoints(int increments) {
        points += increments;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_POINTS, points);
        editor.commit();

        notifyAllObservers();
    }

    public void decrementPoints(int decrements) {
        points -= decrements;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_POINTS, points);
        editor.commit();

        notifyAllObservers();
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }


    public void setLevel(int level) {
        this.level = level;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_LEVEL, points);
        editor.commit();

        notifyAllObservers();
    }

    public void setBadge(String badge) {
        this.badge = badge;

        notifyAllObservers();
    }

    public String getBadge() {
        return badge;
    }

    public static UserBoard getUserBoard(Context context) {
        if(userBoard == null) {
            userBoard = new UserBoard(context);
        }

        return userBoard;
    }
}
