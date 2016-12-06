package com.sharpnode.servercommunication;

import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;
import com.sharpnode.model.ConfiguredDevices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/15/2016.
 */

public class ResponseParser {

    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static AccountModel parseLoginResponse(Object object) {
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
            model.setUserId(jsonObj.optString(Commons.USER_ID));
            model.setPhoneNo(jsonObj.optString(Commons.PHONE));
            model.setPhoto(jsonObj.optString(Commons.PHOTO));
            model.setAccessToken(jsonObj.optString(Commons.ACCESS_TOKEN));
            model.setName(jsonObj.optString(Commons.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Method for parse SignUp response.
     *
     * @param object
     * @return
     */
    public static AccountModel parseSignUpResponse(Object object) {
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
            model.setUserId(jsonObj.optString(Commons.USER_ID));
            model.setPhoneNo(jsonObj.optString(Commons.PHONE));
            model.setPhoto(jsonObj.optString(Commons.PHOTO));
            model.setAccessToken(jsonObj.optString(Commons.ACCESS_TOKEN));
            model.setName(jsonObj.optString(Commons.NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Parse method for get devices response.
     * @param object
     * @return
     */
    public static ConfiguredDevices parseGetDevicesResponse(Object object) {
        JSONArray jsonArr = null;
        JSONObject jsonObj=null;
        ArrayList<ConfiguredDevices> devices = new ArrayList<>();
        ConfiguredDevices model = new ConfiguredDevices();;
        try {
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
            jsonArr = jsonObj.optJSONArray("devices");
            if(jsonArr!=null) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    ConfiguredDevices m = new ConfiguredDevices();
                    m.setDeviceId(jsonArr.optJSONObject(i).optString(Commons.CONFIGURED_DEVICE_ID));
                    m.setDeviceName(jsonArr.optJSONObject(i).optString(Commons.CONFIGURED_DEVICE_NAME));
                    devices.add(m);
                }
            }
            model.setDevicesList(devices);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static AccountModel parseUpdateAccountResponse(Object object) {
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }
    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static AccountModel parseUpdatePasswordResponse(Object object) {
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

}
