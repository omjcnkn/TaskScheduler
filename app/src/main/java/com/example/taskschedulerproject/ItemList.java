package com.example.taskschedulerproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class ItemList implements Serializable {
    private String listTitle;
    private ArrayList<Item> items;
    private String listDate;

    public ItemList(String listTitle) {
        this.listTitle = listTitle;
        items = new ArrayList<>();
        listDate = new Date().toString();
    }

    public abstract void addItem(String title,
                                 String description,
                                 Date date,
                                 int priority,
                                 int duration,
                                 Date deadline);

    public void removeItemByIndex(int index) {
        if(index < items.size()) {
            items.remove(index);
        }
    }

    public void removeItemByTitle(String title) {
        for(int i = 0; i < items.size(); i++) {
                Item currentItem = items.get(i);
                if(currentItem.getTitle().equals(title)) {
                    items.remove(currentItem);
                }
        }
    }

    public Item getItemByIndex(int index) {
        if(index < items.size()) {
            return items.get(index);
        }

        return null;
    }

    public Item getItemByTitle(String title) {
        for(int i = 0; i < items.size(); i++) {
            Item currentItem = items.get(i);
            if(currentItem.getTitle().equals(title)) {
                return currentItem;
            }
        }

        return null;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getListTitle() {
        return listTitle;
    }
}
