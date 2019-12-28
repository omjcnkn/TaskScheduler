package com.example.taskschedulerproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RewardingSystem implements TaskObserver {
    private NotificationManagerCompat manager;
    private UserBoard userBoard;
    private static RewardingSystem rewardingSystem;

    private int levelState;
    private int pointsState;

    private Context appContext;

    private SharedPreferences sharedPreferences;

    private int levelMaxPoints = 10;
    public static final String AppPREFERNCE = "app";
    public static final String LEVELMAXPOINTS = "levelMaxPoints";

    private RewardingSystem(UserBoard userBoard , Context context) {
        this.userBoard = userBoard;
        userBoard.subscribe(this);
        this.appContext = context;
        sharedPreferences = appContext.getSharedPreferences(AppPREFERNCE, Context.MODE_PRIVATE);

        levelState = userBoard.getLevel();
        pointsState = userBoard.getPoints();
        levelMaxPoints = sharedPreferences.getInt(LEVELMAXPOINTS , 10);
        manager = NotificationManagerCompat.from(appContext);

    }

    public void onNotify() {
        int currentPoints = userBoard.getPoints();
        int currentLevel = userBoard.getLevel();
        int levelMaxPoints = getLevelMaxPoints();

        Log.d("LEVEL_MAX_POINTS", levelMaxPoints + "");

        if(pointsState == currentPoints) {
            return;
        }
        else if (currentPoints/levelMaxPoints == 1)
        {
            //Update the Rewarding System current points
            pointsState = currentPoints;

            //Update The Rewarding System Level and set the level in the UserBoard itself
            userBoard.setLevel( currentLevel +1 );
            levelState = userBoard.getLevel();
            Log.d("LEVEL", levelState + "");

            //update the maxlevelpoints with the one corresponding to the current updated level
            updateLevelMaxPoints(levelState);

            Notification notification = new NotificationCompat.Builder(appContext, Notifications.Channel_1_ID)
                    .setSmallIcon(R.drawable.ic_level_up)
                    .setContentTitle("Hallelujah!! Level up")
                    .setContentText("You're now level "+levelState)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_STATUS)
                    .build();
            manager.notify(1,notification);

        }
        else
            pointsState = currentPoints;

        Log.e("POINTS", currentPoints + "");
        Log.e("LEVEL", currentLevel + "");


    }

    public void rewardCheckingTask(int priority) {
        userBoard.incrementPoints(priority);
    }

    public void unRewardCheckingTask(int priority) {
        userBoard.decrementPoints(priority);
    }

    public void setUserLevel() {

    }

    public int getLevelMaxPoints() { return this.levelMaxPoints;  }

    public void updateLevelMaxPoints( int levelState){
        this.levelMaxPoints = this.levelMaxPoints +(10 * levelState);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LEVELMAXPOINTS , levelMaxPoints);
        editor.commit();
    }

    public static RewardingSystem getRewardingSystem(UserBoard userBoard , Context context) {
        if(rewardingSystem == null) {
            rewardingSystem = new RewardingSystem(userBoard , context );
        }

        return rewardingSystem;
    }
}
