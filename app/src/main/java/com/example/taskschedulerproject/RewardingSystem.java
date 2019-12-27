package com.example.taskschedulerproject;

import android.util.Log;

public class RewardingSystem implements TaskObserver {
    private UserBoard userBoard;
    private static RewardingSystem rewardingSystem;

    private int levelState;
    private int pointsState;

    private RewardingSystem(UserBoard userBoard) {
        this.userBoard = userBoard;
        userBoard.subscribe(this);

        levelState = userBoard.getLevel();
        pointsState = userBoard.getPoints();
    }

    public void onNotify() {
        int currentPoints = userBoard.getPoints();
        int currentLevel = userBoard.getLevel();

        if(pointsState == currentPoints) {
            Log.d("REWARD", "Return");
        }

        Log.e("POINTS", currentPoints + "");
        Log.e("LEVEL", currentLevel + "");
    }

    public static RewardingSystem getRewardingSystem(UserBoard userBoard) {
        if(rewardingSystem == null) {
            rewardingSystem = new RewardingSystem(userBoard);
        }

        return rewardingSystem;
    }
}
