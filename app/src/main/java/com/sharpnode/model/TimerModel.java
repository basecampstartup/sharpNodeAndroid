package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 12/16/2016.
 */

public class TimerModel extends BaseModel{

    private String taskName = "";
    private String timerId = "";
    private String repeat = "";
    private String time = "";
    private String action = "";
    private String dateTime = "";

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTime() {
        return dateTime;
    }

    public void setTime(String dateTime) {
        this.dateTime = dateTime;
    }

    private String taskStatus = "";

    public String getTimerId() {
        return timerId;
    }

    public void setTimerId(String timerId) {
        this.timerId = timerId;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getInterval() {
        return time;
    }

    public void setInterval(String time) {
        this.time = time;
    }

    private ArrayList<TimerModel> taskList = new ArrayList<>();

    public ArrayList<TimerModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<TimerModel> taskList) {
        this.taskList = taskList;
    }
}
