package com.sharpnode.model;

/**
 * Created by admin on 12/7/2016.
 */

public class ApplianceModel extends BaseModel{
    private String name="";
    private boolean status=false;
    private String switchIndex="";

    public String getSwitchId() {
        return switchId;
    }

    public void setSwitchId(String switchId) {
        this.switchId = switchId;
    }

    private String switchId = "";

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private boolean isConnected = false;

    public String getName() {
        if(name==null)
            name="";

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSwitchIndex() {
        return switchIndex;
    }

    public void setSwitchIndex(String switchIndex) {
        this.switchIndex = switchIndex;
    }
}
