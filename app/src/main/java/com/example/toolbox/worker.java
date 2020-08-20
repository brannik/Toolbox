package com.example.toolbox;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;


public class worker extends Service {
    public String tmp,tmmp;
    switches func = new switches();
    bluetoothConn bt = new bluetoothConn();
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
                                          tmp = func.getStates("drlButton");
                                          tmmp = func.getStates("interiorBtn");
                                          /*
                                          try {
                                              bt.sendData();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }*/
                                         // Log.d("DEBUG",">> Timer tick");
                                      }
                                  },
                0, 1000);   // 1000 Millisecond  = 1 second
    }
    private static class workerFunctions{
        private String drlState,interState;
        public void checkStates(){

        }
    }

}
