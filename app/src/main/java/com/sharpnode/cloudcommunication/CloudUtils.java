package com.sharpnode.cloudcommunication;

import com.sharpnode.commons.Commons;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.utils.Logger;
import com.sharpnode.widget.WidgetUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/1/2016.
 */

public class CloudUtils {

    public static final String TAG_DEVICES = "devices/";
    public static final String CLOUD_BASE_URL ="https://api.particle.io/v1/"+TAG_DEVICES;
    public static final String CLOUD_FUNCTION_LED = "led";
    public static final String SWITCH_OPERATION_FOR_LED = "switch_led";
    public static final String START = "start";
    public static final String CLOUD_EVENTS = "events";
    public static final String CLOUD_TEMP_HUMIDITY_PREFIX = "sharpnode";
    public static final String CLOUD_FUNCTION_SECURITY = "led";
    public static final String CLOUD_FUNCTION_DEVICE_STATUS = "device_status";
    public static HashMap<String, Boolean> deviceStatus = new HashMap<>();
    public static final  String REFRESH_DEVICE_DASHBOARD ="refresh_device_dashboard";

    public static final  String GET_APPLIANCE_STATUS="get_appliance_status";

    public static String getFunctionByName(String name, boolean status){
        String id="";
        StringBuilder sb = new StringBuilder();
        if ("switch-one".equalsIgnoreCase(name)) {
            sb.append("l1"); // L1
        } else if ("switch-two".equalsIgnoreCase(name)) {
            sb.append("l2"); // L2
        } else if ("switch-three".equalsIgnoreCase(name)) {
            sb.append("l3"); // L3
        } else if ("switch-four".equalsIgnoreCase(name)) {
            sb.append("l4"); // L4
        } else if ("switch-five".equalsIgnoreCase(name)) {
            sb.append("l5"); // L5
        } else if ("switch-six".equalsIgnoreCase(name)) {
            sb.append("l6"); // L6
        }

        sb.append(",");
        sb.append((status)?"HIGH":"LOW"); // HIGH is for OFF, LOW is for ON.
        Logger.i("CloudUtils", "getFunctionByName: "+sb);

        return sb.toString();
    }
}
