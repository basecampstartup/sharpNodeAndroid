package com.sharpnode.model;

import java.util.ArrayList;

/**
 * Created by admin on 11/22/2016.
 */

public class ConfiguredDevices {

    private String deviceName="";
    private String deviceId="";
    private String responseCode="";
    private String responseMessage="";

    public ArrayList<ConfiguredDevices> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(ArrayList<ConfiguredDevices> devicesList) {
        this.devicesList = devicesList;
    }

    private ArrayList<ConfiguredDevices> devicesList =  new ArrayList<>();

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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
