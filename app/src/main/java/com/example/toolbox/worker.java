package com.example.toolbox;
import android.app.Service;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.palaima.smoothbluetooth.SmoothBluetooth;


public class worker extends Service {
    public static final String UPDATE = "update_states_send_to_arduino";
    public String tmp,tmmp;
    public String oldTmp,oldTmmp;
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
        btRecieve reciv = new btRecieve();

        bt.findBT();
        try {
            bt.openBT();
            reciv.run();
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
                                      }
                                  },
                0, 5000);   // 1000 Millisecond  = 1 second
    }
    public Boolean checkStates(){
        Boolean changed = false;
        tmp = func.getStates("drlButton");
        tmmp = func.getStates("interiorBtn");
        if(tmp.equals(oldTmp) && tmmp.equals(oldTmmp)){
            changed = false;
        }else{
            oldTmp = tmp;
            oldTmmp = tmmp;
            changed = true;
        }
        return changed;
    }

    public void sendCommand(String command){
        //btConn bt = new btConn();
        //bt.send();
        String tempOne,tempTwo;
        tempOne = func.getStates("drlButton");
        tempTwo = func.getStates("interiorBtn");
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

    }

}
