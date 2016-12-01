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
import com.sharpnode.servercommunication.APIUtils;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;

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
        callCloudAPI();
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
                        Logger.i(TAG, "response: " + response);
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "error: " + error);
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("params", methodName);
                params.put("access_token", AppSPrefs.getString(Commons.ACCESS_TOKEN));
                return params;
            }

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

//    private void onOffAppliance(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int resultCode = 0;
//                List<String> args = new ArrayList();
//                args.add("access_token:"+AppSPrefs.getString(Commons.ACCESS_TOKEN));
//                //args.add("led");
//                args.add("params: l1,"+methodName);
//                try {
//                    ParticleDevice device = ParticleCloudSDK.getCloud().getDevice("44002a000447343339373536");
//                    for (String funcName : device.getFunctions()) {
//                        Logger.i(TAG, "Device has function: " + funcName);
//                    }
//                    resultCode = device.callFunction("led", args);
//                } catch (ParticleCloudException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParticleDevice.FunctionDoesNotExistException e) {
//                    e.printStackTrace();
//                }
//
//                if(resultCode==1){
//
//                }
//            }
//        }).start();
//    }
}
