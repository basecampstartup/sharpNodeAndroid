package com.sharpnode.cloudcommunication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.ParticleEventVisibility;

/**
 * Created by admin on 12/1/2016.
 */

public class CloudCommunicator {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private HashMap<String, String> params;
    private String methodName;
    private String tag_json_obj = "json_obj_req";
    private Fragment fragmentContext;

    public CloudCommunicator(Context mContext, Fragment fragmentContext, String methodName, HashMap<String, String> params) {
        this.mContext = mContext;
        this.fragmentContext = fragmentContext;
        this.params = params;
        this.methodName = methodName;

        //network call for cloud api.
        if (CloudUtils.CLOUD_TEMP_HUMIDITY_PREFIX.equalsIgnoreCase(methodName)) {
            final String method = methodName;
            final HashMap<String, String> prms = params;
            //callPublishEventForTemp();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    subscribeEventForDevice(method, prms.get(Commons.CONFIGURED_DEVICE_ID));
                }
            }).start();
        } else if (CloudUtils.CLOUD_FUNCTION_DEVICE_STATUS.equalsIgnoreCase(methodName)) {
            callDeviceStatusCloudAPI();
        } else if (CloudUtils.GET_APPLIANCE_STATUS.equalsIgnoreCase(methodName)) {
            callApplianceStatusCloudAPI();
        } /*else if(){
            refreshApplianceStatus();
        }*/ else {
            callCloudAPI();
        }
    }

    /**
     * API call
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
    public void callDeviceStatusCloudAPI() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                + "/?" + Commons.ACCESS_TOKEN + "=" + params.get(Commons.ACCESS_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Logger.i(TAG, "VolleyResponse: " + response);
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Logger.i(TAG, "VolleyError: " + error);
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
        };
        SNApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * API call for fetch the particular device status.
     */
    public void callApplianceStatusCloudAPI() {
        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                + "/status?" + Commons.ACCESS_TOKEN + "=" + params.get(Commons.ACCESS_TOKEN);

        Logger.i(TAG, "callApplianceStatusCloudAPI URL=" + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Logger.i(TAG, "VolleyResponse: " + response);
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Logger.i(TAG, "VolleyError: " + error);
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
            /*@Override
            protected Map<String, String> getParams() {
                HashMap<String, String> p = new HashMap<>();
                p.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
                return p;
            }*/
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

    private void publishAnEvent() {
        try {
            ParticleCloudSDK.getCloud().publishEvent("event_from_app", "some_event_payload",
                    ParticleEventVisibility.PRIVATE, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for subscribe event for fetch the temperature from Cloud Server.
     *
     * @param eventPrefix
     * @param deviceId
     */
    private void subscribeEventForDevice(String eventPrefix, String deviceId) {
        try {
            long subscriptionId;  // save this for later, for unsubscribing
            ParticleCloud cloud = ParticleCloudSDK.getCloud();
            subscriptionId = cloud.subscribeToDeviceEvents(eventPrefix, deviceId, new ParticleEventHandler() {
                @Override
                public void onEvent(String eventName, ParticleEvent particleEvent) {
                    Logger.i(TAG, "onEvent eventName=" + eventName);
                    try {
//                        JSONObject data = new JSONObject(particleEvent.dataPayload.toString());
//                        String status = data.optString("status");
//                        String temperature = data.optString("temperature");
//                        String humidity = data.optString("humidity");
//                        String ip = data.optString("ip");
//                        AppSPrefs.setHumidity(humidity);
//                        AppSPrefs.setTemperature(temperature);
//                        mContext.sendBroadcast(new Intent(CloudUtils.REFRESH_DEVICE_DASHBOARD));
                        ((APIRequestCallbacak) fragmentContext).onSuccess(methodName, particleEvent.dataPayload.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onEventError(Exception e) {
                    Logger.i(TAG, "onEventError e=" + e);
                }
            });

            Logger.i(TAG, "subscriptionId=" + subscriptionId);
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callTempCloudAPI();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * API call for the get temperature by call REST api.
     */
    public void callTempCloudAPI() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                /*+ "/" + CloudUtils.CLOUD_EVENTS*/ + "/" + methodName + "?";

        Logger.i(TAG, "callTempCloudAPI URL=" + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, "callTempCloudAPI, VolleyResponse: " + response);
                        //((APIRequestCallbacak) fragmentContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "callTempCloudAPI, VolleyError: " + error);
                        ((APIRequestCallbacak) fragmentContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> p = new HashMap<>();
                p.put("params", "start");
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
     * API call for publish event using REST API.
     */
    public void callPublishEventForTemp() {

        //Log request parameters
        String URL = CloudUtils.CLOUD_BASE_URL + params.get(Commons.CONFIGURED_DEVICE_ID)
                + "/" + CloudUtils.CLOUD_EVENTS + "/" + methodName + "?"
                /*+ Commons.ACCESS_TOKEN +"="+AppSPrefs.getString(Commons.ACCESS_TOKEN) */;

        Logger.i(TAG, "callPublishEventForTemp URL=" + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, "callPublishEventForTemp, VolleyResponse: " + response);
                        callTempCloudAPI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "callPublishEventForTemp, VolleyError: " + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> p = new HashMap<>();
//                p.put("name", methodName);
                p.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
                return p;
            }

        };
        SNApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

//    ParticleEventHandler handler = new ParticleEventHandler() {
//        @Override
//        public void onEvent(String eventName, ParticleEvent particleEvent) {
//            Logger.i(TAG, "onEvent eventName="+eventName);
//        }
//
//        @Override
//        public void onEventError(Exception e) {
//            Logger.i(TAG, "onEventError e="+e);
//        }
//    };
}
