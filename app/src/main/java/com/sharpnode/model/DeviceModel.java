package com.sharpnode.model;

/**
 * Created by admin on 12/5/2016.
 */

public class DeviceModel extends BaseModel{

    private String device_id;
    private String device_name;
    private String last_ip;
    private String security;
    private boolean deviceStatus;
    private String last_heard;

    public String getLast_heard() {
        return last_heard;
    }

    public void setLast_heard(String last_heard) {
        this.last_heard = last_heard;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public boolean isDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(boolean deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    private String platformId;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
