package com.sharpnode.cloudcommunication;

import android.text.TextUtils;

import com.sharpnode.commons.Commons;
import com.sharpnode.model.DeviceModel;

import org.json.JSONObject;

/**
 * Created by admin on 12/1/2016.
 */

public class CloudResponseParser {

    public static DeviceModel parseDeviceStatusResponse(Object object){
        DeviceModel model = new DeviceModel();
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(object.toString());
            String id = jsonObject.optString("id");
            String name = jsonObject.optString(Commons.NAME);
            String lastIPAddress = jsonObject.optString("last_ip_address");
            String lastHeard = jsonObject.optString("last_heard");
            String connected = jsonObject.optString("connected");
            String platformId = jsonObject.optString("platform_Id");

            model.setDevice_id(id);
            model.setDevice_name(name);
            model.setLast_ip(lastIPAddress);
            model.setLast_heard(lastHeard);
            model.setPlatformId(platformId);

            if (TextUtils.isEmpty(connected))
                model.setDeviceStatus(false);
            else
                model.setDeviceStatus(Boolean.parseBoolean(connected));

            return model;
        }catch (Exception e){
            e.printStackTrace();
        }

        return model;
    }
}
