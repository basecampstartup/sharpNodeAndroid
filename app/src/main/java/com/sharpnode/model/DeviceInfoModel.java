package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 12/6/2016.
 */

public class DeviceInfoModel extends BaseModel{
    private String deviceId = "";
    private String deviceName = "";
    private boolean connected = false;
    private String lastIP = "";
    private boolean security = false;
    private String switches = "";
    private ArrayList<String> applianceList = new ArrayList();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public String getSwitches() {
        return switches;
    }

    public void setSwitches(String switches) {
        this.switches = switches;
    }

    public ArrayList<String> getApplianceList() {
        return applianceList;
    }

    public void setApplianceList(ArrayList<String> applianceList) {
        this.applianceList = applianceList;
    }
}
