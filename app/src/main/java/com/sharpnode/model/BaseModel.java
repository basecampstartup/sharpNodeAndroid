package com.sharpnode.model;

/**
 * Created by admin on 11/25/2016.
 */

public class BaseModel {
    private String responseCode = "";
    private String responseMsg = "";

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
