package com.sharpnode.cloudcommunication;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sharpnode.SNApplication;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 12/1/2016.
 */

public class CloudCommunicator {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private HashMap<String, String> params;
    private String methodName;
    private String tag_json_obj = "json_obj_req";

    public CloudCommunicator(Context mContext, String methodName, HashMap<String, String> params) {
        this.mContext = mContext;
        this.params = params;
        this.methodName = methodName;

        //network call for cloud api.
        if(CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX.equalsIgnoreCase(methodName)){
            callPublishEventForTemp();
        } else {
            callCloudAPI();
        }
    }

    /**
     *
     */
    public void callCloudAPI() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                + "/" + methodName + "?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, "VolleyResponse: " + response);
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "VolleyError: " + error);
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> p = new HashMap<>();
                p.put("params", params.get(CloudUtils.SWITCH_OPERATION_FOR_LED));
                p.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
                return p;
            }

        };
        SNApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     *
     */
    public void callTempCloudAPI() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                /*+ "/"
                + CloudUtils.CLOUD_EVENTS*/
                + "/" + methodName + "?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, "VolleyResponse: " + response);
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "VolleyError: " + error);
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> p = new HashMap<>();
                p.put("params", CloudUtils.START);
                p.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
                return p;
            }

        };
        SNApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void callPublishEventForTemp() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                + "/"
                + CloudUtils.CLOUD_EVENTS
                + "/" + methodName + "?"
                + Commons.ACCESS_TOKEN + "=" + AppSPrefs.getString(Commons.ACCESS_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, "VolleyResponse: " + response);
                        //((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                        callTempCloudAPI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "VolleyError: " + error);
                        //((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String, String> p = new HashMap<>();
//                p.put("params", params.get(Commons.TEMP_HUMIDITY_PREFIX));
//                p.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
//                return p;
//            }

        };
        SNApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String toString() {
        Logger.i(TAG, "Request url: " + CloudUtils.CLOUD_BASE_URL
                + "\nmethodName: " + methodName
                + "\nkeys: " + params.keySet()
                + "\nvalues: " + params.values());

        return super.toString();
    }
}
