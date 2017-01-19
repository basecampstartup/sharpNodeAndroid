package com.sharpnode.servercommunication;

import android.text.TextUtils;

import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;
import com.sharpnode.model.BaseModel;
import com.sharpnode.model.ConfiguredDevices;
import com.sharpnode.model.DeviceInfoModel;
import com.sharpnode.model.TaskModel;
import com.sharpnode.model.TimerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/15/2016.
 */

public class ResponseParser {

    public static BaseModel parseResponse(Object object){
        JSONObject jsonObj = null;
        BaseModel model = new BaseModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
        }catch (Exception e){
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
    public static AccountModel parseLoginResponse(Object object) {
        JSONObject jsonObj = null;
        AccountModel model = new AccountModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            model.setResponseCode(jsonObj.optString(Commons.RESPONSE_CODE));
            model.setResponseMsg(jsonObj.optString(Commons.TXT));
            model.setUserId(jsonObj.optString(Commons.USER_ID));
            model.setPhoneNo(jsonObj.optString(Commons.PHONE));
            model.setPhoto(jsonObj.optString(Commons.IMAGE));
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
            if (object == null)
                return model;
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
        ConfiguredDevices model = new ConfiguredDevices();
        if (object == null)
            return model;

        CloudUtils.deviceStatus = new HashMap<>();
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
                    m.setSwitchs(jsonArr.optJSONObject(i).optString(Commons.SWITCHES));
                    m.setLastIP(jsonArr.optJSONObject(i).optString(Commons.LAST_IP));
                    m.setApplianceJSON(jsonArr.optJSONObject(i).optString("appliances"));
                    String security = jsonArr.optJSONObject(i).optString(Commons.SECURITY);
                    if(!TextUtils.isEmpty(security)){
                        m.setSecurity(false);
                    } else {
                        m.setSecurity(Boolean.parseBoolean(security));
                    }
                    CloudUtils.deviceStatus.put(m.getDeviceId().toLowerCase(), false);
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
            if (object == null)
                return model;
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
            if (object == null)
                return model;
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

    public static DeviceInfoModel parseDeviceInfoResponse(Object object){
        JSONObject jsonObj = null;
        DeviceInfoModel model = new DeviceInfoModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
            model.setDeviceId(jsonObj.optString(Commons.CONFIGURED_DEVICE_ID));
            model.setDeviceName(jsonObj.optString(Commons.CONFIGURED_DEVICE_NAME));
            model.setLastIP(jsonObj.optString(Commons.LAST_IP));
            model.setSwitches(jsonObj.optString(Commons.SWITCHES));
            String security = jsonObj.optString(Commons.SECURITY);
            if (TextUtils.isEmpty(security))
                model.setSecurity(false);
            else
                model.setSecurity(Boolean.parseBoolean(security));

            JSONObject appliances = jsonObj.optJSONObject("appliances");
            ArrayList list = new ArrayList();
            list.add(appliances.optString("switch-one"));
            list.add(appliances.optString("switch-two"));
            list.add(appliances.optString("switch-three"));
            list.add(appliances.optString("switch-four"));
            list.add(appliances.optString("switch-five"));
            list.add(appliances.optString("switch-six"));
            model.setApplianceList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static TaskModel parseGetTaskResponse(Object object){
        JSONObject jsonObj = null;
        TaskModel model = new TaskModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
            JSONArray arr = jsonObj.optJSONArray("tasklist");
            ArrayList<TaskModel> list = new ArrayList<>();
            if(arr!=null){
                for(int i=0;i<arr.length();i++){
                    JSONObject arrObj = arr.getJSONObject(i);
                    TaskModel m = new TaskModel();
                    m.setTaskId(arrObj.optString("task_id"));
                    m.setTaskName(arrObj.optString("task_name"));
                    m.setNextDate(arrObj.optString("next_date"));
                    m.setAction(arrObj.optString("action"));
                    m.setRepeat(arrObj.optString("repeat"));
                    m.setTime(arrObj.optString("time"));
                    m.setTaskStatus(arrObj.optString("task"));
                    list.add(m);
                }
                model.setTaskList(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static TimerModel parseGetTimerTaskResponse(Object object){
        JSONObject jsonObj = null;
        TimerModel model = new TimerModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
            JSONArray arr = jsonObj.optJSONArray("tasklist");
            ArrayList<TimerModel> list = new ArrayList<>();
            if(arr!=null){
                for(int i=0;i<arr.length();i++){
                    JSONObject arrObj = arr.getJSONObject(i);
                    TimerModel m = new TimerModel();
                    m.setTimerId(arrObj.optString("timer_id"));
                    m.setTaskStatus(arrObj.optString("task"));
                    m.setTaskName(arrObj.optString("name"));
                    m.setAction(arrObj.optString("action"));
                    m.setRepeat(arrObj.optString("repeat"));
                    m.setInterval(arrObj.optString("interval"));
                    m.setTime(arrObj.optString("time"));
                    list.add(m);
                }
                model.setTaskList(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static DeviceInfoModel parseGetFavDevicesResponse(Object object) {
        JSONObject jsonObj = null;
        DeviceInfoModel model = new DeviceInfoModel();
        try {
            if (object == null)
                return model;
            jsonObj = new JSONObject(object.toString());
            String id=jsonObj.optString(Commons.RESPONSE_CODE);
            String txt=jsonObj.optString(Commons.TXT);
            model.setResponseCode(id);
            model.setResponseMsg(txt);
            model.setDeviceId(jsonObj.optString(Commons.CONFIGURED_DEVICE_ID));
            model.setDeviceName(jsonObj.optString(Commons.CONFIGURED_DEVICE_NAME));
            model.setLastIP(jsonObj.optString(Commons.LAST_IP));
            model.setSwitches(jsonObj.optString(Commons.SWITCHES));
            String security = jsonObj.optString(Commons.SECURITY);
            if (TextUtils.isEmpty(security))
                model.setSecurity(false);
            else
                model.setSecurity(Boolean.parseBoolean(security));

            JSONObject appliances = jsonObj.optJSONObject("appliances");
            ArrayList list = new ArrayList();
            list.add(appliances.optString("switch-one"));
            list.add(appliances.optString("switch-two"));
            list.add(appliances.optString("switch-three"));
            list.add(appliances.optString("switch-four"));
            list.add(appliances.optString("switch-five"));
            list.add(appliances.optString("switch-six"));
            model.setApplianceList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

}
