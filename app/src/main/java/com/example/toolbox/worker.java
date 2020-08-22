package com.example.toolbox;
import android.app.Service;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;

import io.palaima.smoothbluetooth.SmoothBluetooth;


public class worker extends Service {
    public static final String UPDATE = "update_states_send_to_arduino";
    public String tmp,tmmp;
    public String oldTmp,oldTmmp;
    switches func = new switches();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

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
        if(tmp.equals(oldTmmp) & tmmp.equals(oldTmmp)){
            changed = false;
        }else{
            changed = true;
        }
        return changed;
    }

    public void sendCommand(String command){
        //btConn bt = new btConn();
        //bt.send();
    }

}
