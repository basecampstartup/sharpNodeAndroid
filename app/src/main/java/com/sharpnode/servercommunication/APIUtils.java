package com.sharpnode.servercommunication;

import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 11/15/2016.
 */
public class APIUtils {
    public static final String BASE_URL ="http://api.sharpnode.com/v1/";
    public static final String CMD_SIGN_IN = "user-signin";
    public static final String CMD_GET_DATA = "user-getdata";
    public static final String CMD_SIGN_UP = "user-signup";
    public static final String CMD_ADD_TO_FAV_DEVICE = "add-to-fav";
    public static final String CMD_FAV_DEVICE = "fav-device";
    public static final String CMD_GET_DEVICES = "user-devices";
    public static final String CMD_ADD_DEVICE = "device-add";
    public static final String CMD_REMOVE_DEVICE = "device-remove";
    public static final String CMD_RENAME_DEVICE = "device-rename";
    public static final String CMD_UPDATE_ACCOUNT = "update-account";
    public static final String CMD_UPDATE_PASSWORD = "update-password";
    public static final String CMD_SET_NEW_PASSWORD = "setnew-password";
    public static final String CMD_RESET_PASSWORD = "reset-password";

    public static final String CMD_TIMER_LIST= "timer-list";
    public static final String CMD_TIMER_ADD = "timer-add";
    public static final String CMD_TIMER_REMOVE = "timer-remove";

    public static final String CMD_SCHEDULER_DELETE = "task-remove";
    public static final String CMD_SCHEDULER_LIST= "task-list";
    public static final String CMD_SCHEDULER_ADD = "task-add";
    public static final String CMD_SCHEDULER_UPDATE = "task-update";
    public static final String CMD_USER_CONTACT = "user-contact";
    public static final String CMD_RENAME_APPLIANCE = "switch-rename";
    public static final String CMD_SECURITY_ON_OFF = "switch-pir";

    public static final String CMD_DEVICE_INFO= "device-info";
    public static final String CONTACT_US_URL ="http://sharpnode.com/contact";
    public static final String USER_MANNUAL_URL ="http://sharpnode.com/products#homeAutomation";

}
