package com.example.app.REIN;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


public class AppWidget extends AppWidgetProvider {

    public static final String str = "open";
    public static final String str2 = "open1";
    public static final String str4 = "open2";
    public static final String str5 = "open3";
    public static final String str6 = "open4";
    public static final String str3 = "onoff";

    private static boolean serviceRunning = false;
    private static boolean status = false;
    private static Intent serviceIntent;
    private ComponentName watchWidget;

    private Button one,two,three,four,five;

    RemoteViews remoteViews;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {



        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget);

            remoteViews.setViewVisibility(R.id.on, View.VISIBLE);
            //remoteViews.setViewVisibility(R.id.off, View.INVISIBLE);

            remoteViews.setOnClickPendingIntent(R.id.button, getPendingSelfIntent(context, str));
            remoteViews.setOnClickPendingIntent(R.id.button2, getPendingSelfIntent(context, str2));
            remoteViews.setOnClickPendingIntent(R.id.button3, getPendingSelfIntent(context, str4));
            remoteViews.setOnClickPendingIntent(R.id.button4, getPendingSelfIntent(context, str5));
            remoteViews.setOnClickPendingIntent(R.id.button5, getPendingSelfIntent(context, str6));
            remoteViews.setOnClickPendingIntent(R.id.off, getPendingSelfIntent(context, str3));
            remoteViews.setOnClickPendingIntent(R.id.on, getPendingSelfIntent(context, str3));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (str.equals(intent.getAction())) {
            Intent i = new Intent(context, BUTTON1_CLICKED.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if(str2.equals(intent.getAction())){
            Intent i = new Intent(context, BUTTON2_CLICKED.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if(str4.equals(intent.getAction())){
            Intent i = new Intent(context, BUTTON3_CLICKED.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if(str5.equals(intent.getAction())){
            Intent i = new Intent(context, BUTTON4_CLICKED.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if(str6.equals(intent.getAction())){
            Intent i = new Intent(context, BUTTON5_CLICKED.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else if(str3.equals(intent.getAction())){






            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            // Create a fresh intent
            Intent serviceIntent = new Intent(context, MyService.class);

            if(serviceRunning) {
                context.stopService(serviceIntent);
                remoteViews.setViewVisibility(R.id.on, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.off, View.INVISIBLE);
               Toast.makeText(context, "serviceStopped", Toast.LENGTH_SHORT).show();
            } else {
                context.startService(serviceIntent);
                remoteViews.setViewVisibility(R.id.off, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.on, View.INVISIBLE);
                Toast.makeText(context, "serviceStarted", Toast.LENGTH_SHORT).show();
            }
            serviceRunning=!serviceRunning;
            ComponentName componentName = new ComponentName(context, AppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
        super.onReceive(context, intent);
    }

}
