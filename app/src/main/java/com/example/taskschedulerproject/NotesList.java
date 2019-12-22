package com.example.taskschedulerproject;

import java.util.ArrayList;
import java.util.Date;

public class NotesList extends ItemList {
    public NotesList(String listTitle) {
        super(listTitle);
    }

    public void addItem(String title,
                        String description,
                        Date date,
                        short priority,
                        short duration,
                        Date deadline) {
        ArrayList<Item> allNotes = getItems();
        allNotes.add(new NoteItem(title, description, date));
    }
}
