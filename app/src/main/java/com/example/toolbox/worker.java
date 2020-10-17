package com.example.toolbox;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.palaima.smoothbluetooth.SmoothBluetooth;


public class worker extends Service {
    public static final String UPDATE = "update_states_send_to_arduino";
    public static final String UPDATE_DEFAULTS = "update_DEFAULTS_send_to_arduino";
    public String tmp,tmmp;
    public String oldTmp,oldTmmp;
    Boolean firstRun = true;
    Context applicationContext = MainActivity.getContextOfApplication();
    public boolean checkedF = false;


    public String drlDelay,interDelay,drlDeffState,interDeffState;
    public String oldDrlDef,oldInterDef,oldDrlDelay,oldInterDelay;
    switches func = new switches();
    btConn bt = new btConn();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        oldTmp = func.getStates("drlButton");
        oldTmmp = func.getStates("interiorBtn");
        checkPrefs();
        bt.findBT();
        try {
            bt.openBT();
        } catch (Exception e) {
            Log.d("DEBUG", "WORKER>> " + e);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          // Magic here
                                              if(checkStates()){
                                                  sendCommand(UPDATE);
                                              }
                                              if(checkDefaults()){
                                                  sendCommand(UPDATE_DEFAULTS);
                                              }
                                      }
                                  },
                0, 5000);   // 1000 Millisecond  = 1 second
        if(firstRun){
            Intent intent = new Intent(this, allinone.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), allinone.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            sendBroadcast(intent);
            firstRun = false;
        }
    }
    public Boolean checkStates(){
        Boolean changed = false;
        checkPrefs();
        if(tmp.equals(oldTmp) && tmmp.equals(oldTmmp)){
            changed = false;
        }else{
            oldTmp = tmp;
            oldTmmp = tmmp;
            changed = true;
        }
        return changed;
    }

    public Boolean checkDefaults(){
        checkPrefs();
        Boolean checked = false;
        if(drlDeffState.equals(oldDrlDef) && drlDelay.equals(oldDrlDelay) && interDeffState.equals(oldInterDef) && interDelay.equals(oldInterDelay)){
            checked = false;
        }else{
            oldDrlDef = drlDeffState;
            oldInterDef = interDeffState;
            oldDrlDelay = drlDelay;
            oldInterDelay = interDelay;
            checked = true;
        }
        return checked;
    }

    public void sendCommand(String command){
        //btConn bt = new btConn();
        //bt.send();
        switch(command){
            case UPDATE:
                String tempOne,tempTwo;
                tempOne = func.getStates("drlState");
                tempTwo = func.getStates("interiorState");
                if(bt.mmSocket.isConnected()) {
                    try {
                        bt.sendData(tempOne, tempTwo);
                    } catch (Exception ex) {
                        Log.d("DEBUG", "ERROR >> " + ex);
                    }
                }else{
                    bt.findBT();
                    try{
                        bt.closeBT();
                        bt.openBT();
                    }catch (Exception ignored){}
                }
                break;
            case UPDATE_DEFAULTS:
                checkPrefs();
                if(bt.mmSocket.isConnected()) {
                    try {
                        bt.updSett(drlDeffState,drlDelay,interDeffState,interDelay);
                    } catch (Exception ex) {
                        Log.d("DEBUG", "ERROR >> " + ex);
                    }
                }else{
                    bt.findBT();
                    try{
                        bt.closeBT();
                        bt.openBT();
                    }catch (Exception ignored){}
                }
                break;
            default:
                Log.d("DEBUG","ERROR SEND COMMAND FUNCTION");
        }


    }

    private void checkPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        drlDelay = prefs.getString("drlDelay", null);
        interDelay = prefs.getString("interDelay", null);
        drlDeffState = prefs.getString("drlDeffState", null);
        interDeffState = prefs.getString("interDeffState", null);

        tmp = prefs.getString("drlState",null);
        tmmp = prefs.getString("interState",null);
        if(checkedF == false){
            oldTmp = tmp;
            oldTmmp = tmmp;
            checkedF = true;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            bt.closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            bt.closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
