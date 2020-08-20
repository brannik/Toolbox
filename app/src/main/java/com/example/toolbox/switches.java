package com.example.toolbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class switches{
    public String state;
    public String stateGetter;

    Context applicationContext = MainActivity.getContextOfApplication();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);

    public String drlState,interState,drlDelay,interDelay,drlDeffState,interDeffState;
    private void checkPrefs(){

        if (prefs.contains("drlState") & prefs.contains("interDeffState")) {
            drlState = prefs.getString("drlState",null);
            interState = prefs.getString("interState",null);
            drlDelay = prefs.getString("drlDelay",null);
            interDelay = prefs.getString("interDelay",null);
            drlDeffState = prefs.getString("drlDeffState",null);
            interDeffState = prefs.getString("interDeffState",null);
        } else {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("drlState", "OFF");
            editor.putString("interState","OFF");
            editor.putString("drlDelay","10");
            editor.putString("interDelay","36");
            editor.putString("drlDeffState","OFF");
            editor.putString("interDeffState","OFF");
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
        editor.commit();
        //return state;
    }



    public String getStates(String elem){
        checkPrefs();

        switch (elem){
            case "drlButton":
                stateGetter = drlState;
                break;
            case "interiorBtn":
                stateGetter = interState;
                break;
            default:
                stateGetter = "error";
        }
        return stateGetter;
    }



}
