package com.example.taskschedulerproject;

import java.util.ArrayList;
import java.util.Date;

public class CheckList extends ItemList {
    public CheckList() {
        super();
    }

    public void addItem(String title,
                        String description,
                        Date date,
                        short priority,
                        short duration,
                        Date deadline) {
        ArrayList<Item> allTasks = getItems();
        allTasks.add(new TaskItem(title, description, date, priority, duration, deadline));
    }

    public void checkAllItems() {
        ArrayList<Item> allTasks = getItems();
        for(int i = 0; i < allTasks.size(); i++) {
            Item currentItem = allTasks.get(i);
            if(currentItem instanceof TaskItem) {
                TaskItem currentTask = (TaskItem) currentItem;
                currentTask.setChecked(true);
            }
        }
    }
}
