package com.example.taskschedulerproject;

import java.util.Date;

public class TaskItem extends Item {
    private short priority;
    private short duration;
    private Date deadline;
    private boolean checked;

    public TaskItem(String title,
                    String description,
                    Date date,
                    short priority,
                    short duration,
                    Date deadline) {
        super(title, description, date);
        this.priority = priority;
        duration = duration;
        deadline = deadline;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Date getDeadline() {
        return deadline;
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
