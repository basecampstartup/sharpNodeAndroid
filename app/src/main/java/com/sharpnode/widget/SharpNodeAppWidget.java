package com.sharpnode.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.sharpnode.R;
import com.sharpnode.SNApplication;
import com.sharpnode.commons.Commons;
import com.sharpnode.model.ApplianceModel;
import com.sharpnode.sprefs.AppSPrefs;
import com.sharpnode.utils.Logger;
import com.sharpnode.utils.Utils;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class SharpNodeAppWidget extends AppWidgetProvider {
    private String TAG = getClass().getSimpleName();

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sharp_node_app_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

//        String label = "Control appliances of your ";
//        String device = AppSPrefs.getWidgetDevice();
//        Spannable labelSpan = new SpannableString(label+device);
//        labelSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#50d2ce")), label.length(), (label.length()+device.length()),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        remoteViews.setTextViewText(R.id.tvWidgetLbl2, labelSpan);
        remoteViews.setOnClickPendingIntent(R.id.btnCFLSwitch,
                getPendingSelfIntent(context, Commons.ACTION_CFL_SWITCH_CLICK));

        remoteViews.setOnClickPendingIntent(R.id.btnFanSwitch,
                getPendingSelfIntent(context, Commons.ACTION_FAN_SWITCH_CLICK));

        updateWidgetOfAppliances(WidgetUtils.getWidgetAppliances(), remoteViews);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void updateWidgetOfAppliances(ArrayList<ApplianceModel> appliances, RemoteViews remoteViews){
        for (int i = 0; i < appliances.size(); i++) {
            ApplianceModel model = appliances.get(i);
            switch (i){
                case 0:
                    remoteViews.setTextViewText(R.id.btnCFLSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnCFLSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnCFLSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
                case 1:
                    remoteViews.setTextViewText(R.id.btnFanSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnFanSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnFanSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
                case 2:
                    remoteViews.setTextViewText(R.id.btnLampSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnLampSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnLampSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
                case 3:
                    remoteViews.setTextViewText(R.id.btnTVSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnTVSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnTVSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
                case 4:
                    remoteViews.setTextViewText(R.id.btnMusicSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnMusicSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnMusicSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
                case 5:
                    remoteViews.setTextViewText(R.id.btnSixSwitch, model.getName());
                    if(model.isStatus()){
                        remoteViews.setTextColor(R.id.btnSixSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAccent));
                    }else{
                        remoteViews.setTextColor(R.id.btnSixSwitch,
                                SNApplication.snApp.getResources().getColor(R.color.colorAppWhite));
                    }
                    break;
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Logger.i(TAG, "onUpdate Called!");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Logger.i(TAG, "ACTION: "+intent.getAction());

        if (Commons.ACTION_CFL_SWITCH_CLICK.equalsIgnoreCase(intent.getAction())) {
            WidgetActionControl.switchClick("switch-one", WidgetUtils.getWidgetAppliances().get(0));
            //Toast.makeText(context, "CFL Button Clicked !", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, "Other Button Clicked !", Toast.LENGTH_SHORT).show();
        }
    }
    //wedget item click
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

