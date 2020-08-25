package com.example.toolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class switches{
    public String state;
    public String stateGetter;

    Context applicationContext = MainActivity.getContextOfApplication();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

    public String drlState,interState,drlDelay,interDelay,drlDeffState,interDeffState,btState;
    private void checkPrefs(){

        if (prefs.contains("drlState") & prefs.contains("bluetoothState")) {
            drlState = prefs.getString("drlState",null);
            interState = prefs.getString("interState",null);
            drlDelay = prefs.getString("drlDelay",null);
            interDelay = prefs.getString("interDelay",null);
            drlDeffState = prefs.getString("drlDeffState",null);
            interDeffState = prefs.getString("interDeffState",null);
            btState = prefs.getString("bluetoothState",null);
        } else {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("drlState", "OFF");
            editor.putString("interState","OFF");
            editor.putString("drlDelay","10");
            editor.putString("interDelay","36");
            editor.putString("drlDeffState","OFF");
            editor.putString("interDeffState","OFF");
            editor.putString("bluetoothState","OFFLINE");
            editor.apply();
            editor.commit();
        }
    }

    public void updateState(String element){
        checkPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        switch (element){
            case "drlButton":
                if(drlState.equals("OFF")){
                    drlState = "ON";
                }else if(drlState.equals("ON")){
                    drlState = "OFF";
                }
                break;
            case "interiorBtn":
                if(interState.equals("ON")){
                    interState = "OFF";
                }else if(interState.equals("OFF")){
                    interState = "ON";
                }
                break;
            default:
                state = "error";
        }
        editor.putString("drlState", drlState);
        editor.putString("interState",interState);
        editor.apply();
        //return state;
    }

    public void updateBluethhothState(Boolean st){
        SharedPreferences.Editor editor = prefs.edit();
        if(st){
            editor.putString("bluetoothState","ONLINE");
        }else{
            editor.putString("bluetoothState","OFFLINE");
        }
        editor.apply();
    }

    public String getStates(String elem/*elements: drlState/interiorState/bluetooth/drlDeffState/interiorDeffState/drlDelay/interiorDelay*/){
        checkPrefs();

        switch (elem){
            case "drlState":
                stateGetter = drlState;
                break;
            case "interiorState":
                stateGetter = interState;
                break;
            case "bluetooth":
                stateGetter = btState;
                break;
            case "drlDeffState":
                stateGetter = drlDeffState;
                break;
            case "interiorDeffState":
                stateGetter = interDeffState;
                break;
            case "drlDelay":
                stateGetter = drlDelay;
                break;
            case "interiorDelay":
                stateGetter = interDelay;
                break;
            default:
                stateGetter = "error";
        }
        return stateGetter;
    }



}
