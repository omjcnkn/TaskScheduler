package com.example.taskschedulerproject;

import java.util.ArrayList;

public abstract class Subject {
    private ArrayList<TaskObserver> taskObservers;

    public Subject() {
        taskObservers = new ArrayList<>();
    }

    public void subscribe(TaskObserver taskObserver) {
        taskObservers.add(taskObserver);
    }

    public void unsubscribe(TaskObserver taskObserver) {
        taskObservers.remove(taskObserver);
    }

    public void notifyAllObservers() {
        for(int i = 0; i < taskObservers.size(); i++) {
            taskObservers.get(i).onNotify();
        }
    }
}
