package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 11/22/2016.
 */

public class ConfiguredDevices extends BaseModel{

    private String deviceName="";
    private String deviceId="";
    private ArrayList<ConfiguredDevices> devicesList =  new ArrayList<>();
    private boolean security = false;
    private String switchs = "";
    private String applianceJSON = "";

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    private String lastIP="";

    public String getApplianceJSON() {
        return applianceJSON;
    }

    public void setApplianceJSON(String applianceJSON) {
        this.applianceJSON = applianceJSON;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public String getSwitchs() {
        return switchs;
    }

    public void setSwitchs(String switchs) {
        this.switchs = switchs;
    }

    public ArrayList<ConfiguredDevices> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(ArrayList<ConfiguredDevices> devicesList) {
        this.devicesList = devicesList;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
