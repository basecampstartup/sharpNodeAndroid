package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 12/15/2016.
 */

public class TaskModel extends BaseModel{
    private String taskId="";
    private String taskName="";
    private String nextDate="";
    private String action="";
    private String repeat="";
    private String time="";

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    private String taskStatus="";
    private ArrayList<TaskModel> taskList = new ArrayList<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<TaskModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<TaskModel> taskList) {
        this.taskList = taskList;
    }
}
