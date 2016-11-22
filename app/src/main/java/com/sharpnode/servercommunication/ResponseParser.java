package com.sharpnode.servercommunication;

import com.sharpnode.commons.Commons;
import com.sharpnode.model.AccountModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 11/15/2016.
 */

public class ResponseParser {

    /**
     * Method for parse login response.
     * @param object
     * @return
     */
    public static AccountModel parseLoginResponse(Object object){
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
     * @param object
     * @return
     */
    public static AccountModel parseSignUpResponse(Object object){
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
}
