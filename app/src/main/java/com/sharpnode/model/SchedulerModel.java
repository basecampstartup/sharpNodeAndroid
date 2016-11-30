package com.sharpnode.model;

/**
 * Created by admin on 11/21/2016.
 */

public class SchedulerModel extends BaseModel{

    private String scheduleTaskName="";
    private String applianceName="";
    private boolean turnOnOff =false;
    private boolean isEveryDay=false;
    private String name="";
    private String weekDays="";
    private String time="";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScheduleTaskName() {
        return scheduleTaskName;
    }

    public void setScheduleTaskName(String scheduleTaskName) {
        this.scheduleTaskName = scheduleTaskName;
    }

    public String getApplianceName() {
        return applianceName;
    }

    public void setApplianceName(String applianceName) {
        this.applianceName = applianceName;
    }

    public boolean isTurnOnOff() {
        return turnOnOff;
    }

    public void setTurnOnOff(boolean turnOnOff) {
        this.turnOnOff = turnOnOff;
    }

    public boolean isEveryDay() {
        return isEveryDay;
    }

    public void setEveryDay(boolean everyDay) {
        isEveryDay = everyDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }
}
