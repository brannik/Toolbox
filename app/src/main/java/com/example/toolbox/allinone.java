package com.example.toolbox;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Implementation of App Widget functionality.
 */
public class allinone extends AppWidgetProvider {

    private static final String DRL_BTN_EVENT = "drlState";
    private static final String INTERIOR_BTN_EVENT = "interiorState";
    Context applicationContext = MainActivity.getContextOfApplication();
    RemoteViews remoteViews;
    switches switchFunc = new switches();
    String reader="";
    String readerTmp="";
    String btStat="";
    @SuppressLint("WrongConstant")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.allinone);
        // get new values and set text
        for (int widgetId : appWidgetIds) {


            Intent intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            remoteViews.setOnClickPendingIntent(R.id.btnDrl,
                    getPendingSelfIntent(context, DRL_BTN_EVENT));
            remoteViews.setOnClickPendingIntent(R.id.btnInterior,
                    getPendingSelfIntent(context, INTERIOR_BTN_EVENT));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            //Log.d("DEBUG", "Refresh");

        }
            reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
            readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
            btStat = switchFunc.getStates("bluetooth");
            status(readerTmp,reader,btStat);
    }


    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.allinone);
        if (DRL_BTN_EVENT.equals(intent.getAction())) {
            // your onClick action is here
            switchFunc.updateState(DRL_BTN_EVENT);
            reader = switchFunc.getStates(DRL_BTN_EVENT);

            //Toast.makeText(context, "DRL STATE: " + reader, Toast.LENGTH_SHORT).show();

            onUpdate(context);
            //Log.d("DEBUG", "BTN DRL CLICK");
        } else if (INTERIOR_BTN_EVENT.equals(intent.getAction())) {

            switchFunc.updateState(INTERIOR_BTN_EVENT);
            reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
            //Toast.makeText(context, "INTERIOR STATE: " + reader, Toast.LENGTH_SHORT).show();

            onUpdate(context);
            //Log.d("DEBUG", "BTN INTER CLICK");
        }else{
            onUpdate(context);
        }
    }


    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        //Log.d("DEBUG", ">>INTENT<<");
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
        //Log.d("DEBUG", ">>ON UPDATE <<");
    }

    public void status(String drl,String inter,String btStatus){
        int statusBarHeight = 0;
        String msg,msg2,msg3,msg4,msg5,msg6;
        int resourceId = applicationContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = applicationContext.getResources().getDimensionPixelSize(resourceId);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                statusBarHeight,
                WindowManager.LayoutParams.TYPE_TOAST,   // Allows the view to be on top of the StatusBar
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,    // Keeps the button presses from going to the background window and Draws over status bar
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.CENTER;
        parameters.width = 210;

        LinearLayout ll = new LinearLayout(applicationContext);
        ll.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParameteres);

        TextView tv = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(tvParameters);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        msg = "DRL ";
        tv.setText(msg);
        ll.addView(tv);

        TextView tv2 = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv2.setLayoutParams(tvParameters2);
        if(drl.equals("ON")){
            tv2.setTextColor(Color.GREEN);
        }else{
            tv2.setTextColor(Color.RED);
        }
        tv2.setGravity(Gravity.CENTER);
        msg2 =  drl;
        tv2.setText(msg2);
        ll.addView(tv2);

        TextView tv3 = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters3 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv3.setLayoutParams(tvParameters3);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        msg3 = "  INT ";
        tv3.setText(msg3);
        ll.addView(tv3);

        TextView tv4 = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters4 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv4.setLayoutParams(tvParameters4);
        if(inter.equals("ON")){
            tv4.setTextColor(Color.GREEN);
        }else{
            tv4.setTextColor(Color.RED);
        }
        tv4.setGravity(Gravity.CENTER);
        msg4 =  inter;
        tv4.setText(msg4);
        ll.addView(tv4);

        TextView tv6 = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters6 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv6.setLayoutParams(tvParameters6);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        msg6 = "  BT ";
        tv6.setText(msg6);
        ll.addView(tv6);

        TextView tv5 = new TextView(applicationContext);
        ViewGroup.LayoutParams tvParameters5 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv5.setLayoutParams(tvParameters5);
        if(btStatus.equals("ONLINE")){
            tv5.setTextColor(Color.GREEN);
        }else{
            tv5.setTextColor(Color.RED);
        }
        tv5.setGravity(Gravity.CENTER);
        msg5 =  btStatus;
        tv5.setText(msg5);
        ll.addView(tv5);

        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(ll, parameters);
    }

}

