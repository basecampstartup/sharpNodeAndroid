package com.sharpnode.widget;

import android.widget.Toast;

import com.sharpnode.DeviceDashboardActivity;
import com.sharpnode.HomeDashboardActivity;
import com.sharpnode.R;
import com.sharpnode.cloudcommunication.CloudCommunicator;
import com.sharpnode.cloudcommunication.CloudUtils;
import com.sharpnode.commons.Commons;
import com.sharpnode.context.ContextHelper;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.network.CheckNetwork;
import com.sharpnode.servercommunication.Communicator;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/21/2016.
 */

public class WidgetActionControl {
    private String TAG = getClass().getSimpleName();

    public static void switchClick(String switchId, ApplianceModel model){
        Logger.i("WidgetActionControl", "switchId: " + switchId);
        Logger.i("WidgetActionControl", "Before Status: " + model.isStatus());
        if(WidgetUtils.isWidgetDeviceConnected){
            if(CheckNetwork.isInternetAvailable(ContextHelper.getContext())){
                ArrayList<ApplianceModel> list = WidgetUtils.getWidgetAppliances();
                for(int i=0;i<list.size();i++){
                    ApplianceModel tempModel = list.get(i);
                    if(tempModel.getName().equalsIgnoreCase(model.getName())){
                        ApplianceModel m = new ApplianceModel();
                        m.setStatus(!model.isStatus());
                        m.setSwitchIndex(model.getSwitchIndex());
                        m.setName(model.getName());
                        m.setConnected(model.isConnected());
                        list.remove(i);
                        list.add(i, m);
                        break;
                    }
                }
                Logger.i("WidgetActionControl", "name: " + model.getName());
                Logger.i("WidgetActionControl", "switchIndex: " + model.getSwitchIndex());
                Logger.i("WidgetActionControl", "After Status: " + model.isStatus());

                //String value = CloudUtils.getFunctionByName(switchId, model.isStatus());
                //Logger.i("WidgetActionControl", "Switch: value=" + value);

                makeSwitchOnOff(switchId);
            } else {
                Toast.makeText(ContextHelper.getContext(), ContextHelper.getContext().getString(R.string.check_for_internet_connectivity),
                        Toast.LENGTH_LONG).show();
            }
        } else {
//            Toast.makeText(ContextHelper.getContext(), ContextHelper.getContext().getString(R.string.set_device_on_widget),
//                    Toast.LENGTH_LONG).show();
            Toast.makeText(ContextHelper.getContext(), "Widget device is offline",
                    Toast.LENGTH_LONG).show();
        }
    }

    private static void makeSwitchOnOff(String value){
        if(WidgetUtils.isWidgetDeviceConnected){
            HashMap<String, String> params = new HashMap<>();
            params.put(Commons.COMMAND, "appliances");
            params.put(Commons.SWITCH_ID, value);
            params.put(Commons.CONFIGURED_DEVICE_ID, AppSPrefs.getWidgetDeviceId());
            params.put(Commons.ACCESS_TOKEN, AppSPrefs.getString(Commons.ACCESS_TOKEN));

            Logger.i("TAG", "Context: "+ContextHelper.getContext());
            //Call Cloud API Request after check internet connection
            //new CloudCommunicator(HomeDashboardActivity.homeActivity, null, CloudUtils.CLOUD_FUNCTION_LED, params);
            new Communicator(ContextHelper.getContext(), null, CloudUtils.CLOUD_FUNCTION_LED, params);
        } else {
            Toast.makeText(ContextHelper.getContext(), ContextHelper.getContext().getString(R.string.device_is_offline), Toast.LENGTH_LONG).show();
        }
    }
}
