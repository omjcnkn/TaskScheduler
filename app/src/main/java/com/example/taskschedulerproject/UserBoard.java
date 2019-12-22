package com.example.taskschedulerproject;

import java.util.ArrayList;

public class UserBoard {
    private String username;
    private ArrayList<ItemList> lists;
    private int level;
    private int points;

    public UserBoard(String username) {
        this.username = username;
        lists = new ArrayList<>();
    }

    public void createNewNotesList(String listTitle) {
        lists.add(new NotesList(listTitle));
    }

    public void createNewCheckList(String listTitle) {
        lists.add(new CheckList(listTitle));
    }

    public void removeList(ItemList list) {
        lists.remove(list);
    }

    public ItemList getListByIndex(int index) {
        return lists.get(index);
    }

    public void incrementPoints() {
        points++;
    }

    public void decrementPoints() {
        points--;
    }

    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<ItemList> getLists() {
        return lists;
    }
}
