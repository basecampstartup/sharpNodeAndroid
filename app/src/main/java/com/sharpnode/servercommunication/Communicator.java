package com.sharpnode.servercommunication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.callback.APIRequestCallbacak;
import com.sharpnode.commons.Commons;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11/15/2016.
 */
public class Communicator {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private HashMap<String, String> params;
    private String methodName;
    private String tag_json_obj = "json_obj_req";

    public Communicator(Context mContext, String methodName, HashMap<String, String> params) {
        this.mContext = mContext;
        this.params = params;
        this.methodName = methodName;

        //network call for api.
        callAPI();
    }

    /**
     * This method will be used to call server api and redirect the response to respective screen.
     */
    public void callAPI() {
        //Log request parameters
        toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIUtils.BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(APIUtils.CMD_SIGN_IN.equalsIgnoreCase(methodName)
                                || APIUtils.CMD_SIGN_UP.equalsIgnoreCase(methodName)){
                            AppSPrefs.setString(Commons.USER_ID, params.get(Commons.USER_ID));
                            AppSPrefs.setString(Commons.PASSWORD, params.get(Commons.PASSWORD));
                        }
                        ((APIRequestCallbacak) mContext).onSuccess(methodName, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((APIRequestCallbacak) mContext).onFailure(methodName, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Logger.i(TAG, "params: " + params);
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
        Logger.i(TAG, "Request url: " + APIUtils.BASE_URL
                + "\nmethodName: " + methodName
                + "\nkeys: " + params.keySet()
                + "\nvalues: " + params.values());

        return super.toString();
    }
}
