package com.sharpnode;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.widget.RemoteViews;

public class AppWidgetConfigurationActivity extends Activity {
    int mAppWidgetId;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_configuration);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
       /* AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.example_appwidget);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);*/
    }
}
