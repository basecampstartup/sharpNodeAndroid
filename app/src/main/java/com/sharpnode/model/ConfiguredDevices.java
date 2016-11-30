package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 11/22/2016.
 */

public class ConfiguredDevices extends BaseModel{

    private String deviceName="";
    private String deviceId="";
    private ArrayList<ConfiguredDevices> devicesList =  new ArrayList<>();

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
