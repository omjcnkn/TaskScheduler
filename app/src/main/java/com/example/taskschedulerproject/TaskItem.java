package com.example.taskschedulerproject;

import java.io.Serializable;
import java.util.Date;

public class TaskItem extends Item implements Serializable {
    private int priority;
    private int duration;
    private Date deadline;
    private boolean checked;

    public TaskItem(String title,
                    String description,
                    Date date,
                    int priority,
                    int duration,
                    Date deadline) {
        super(title, description, date);
        this.priority = priority;
        this.duration = duration;
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
