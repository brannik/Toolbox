package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    public static Context contextOfApplication;
    public String drlState,interState,drlDelay,interDelay,drlDeffState,interDeffState;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        startService(new Intent(this, worker.class));

        checkPrefs();
        TextView txtDrlDelay = findViewById(R.id.editDrlDelay);
        TextView txtInterDelay = findViewById(R.id.editInterDelay);
        ToggleButton tglDrlState = findViewById(R.id.tglDrlState);
        ToggleButton tglInterState = findViewById(R.id.tglInterState);
        Button btnDrlSave = findViewById(R.id.btnDrlDelay);
        Button btnInterSave = findViewById(R.id.btnInterDelay);

        tglDrlState.setOnClickListener(this);
        tglInterState.setOnClickListener(this);
        btnDrlSave.setOnClickListener(this);
        btnInterSave.setOnClickListener(this);

        txtDrlDelay.setText(drlDelay);
        txtInterDelay.setText(interDelay);
        if(drlDeffState.equals("ON")){
            tglDrlState.setChecked(true);
        }else{
            tglDrlState.setChecked(false);
        }

        if(interDeffState.equals("ON")){
            tglInterState.setChecked(true);
        }else{
            tglInterState.setChecked(false);
        }
        // get data from shared preferences


        applyStatusBar("Testing notification services",1);
        //status("ON","OFF");
        // Get teh view params

    }
    public void status(String drl,String inter){
        int statusBarHeight = 0;
        String msg;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                statusBarHeight,
                WindowManager.LayoutParams.TYPE_TOAST,   // Allows the view to be on top of the StatusBar
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,    // Keeps the button presses from going to the background window and Draws over status bar
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.CENTER;

        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParameteres);

        TextView tv = new TextView(this);
        ViewGroup.LayoutParams tvParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(tvParameters);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        msg = "DRL " + drl + " / Interior " + inter;
        tv.setText(msg);
        ll.addView(tv);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(ll, parameters);
    }


    private void applyStatusBar(String iconTitle, int notificationId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(iconTitle);
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, notification);
    }

    private void checkPrefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contextOfApplication);
        if (prefs.contains("drlState") & prefs.contains("interDeffState")) {
            drlState = prefs.getString("drlState",null);
            interState = prefs.getString("interState",null);
            drlDelay = prefs.getString("drlDelay",null);
            interDelay = prefs.getString("interDelay",null);
            drlDeffState = prefs.getString("drlDeffState",null);
            interDeffState = prefs.getString("interDeffState",null);
        } else {
            // build default values on 1-st use
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("drlState", "OFF");
            editor.putString("interState","OFF");
            editor.putString("drlDelay","10");
            editor.putString("interDelay","36");
            editor.putString("drlDeffState","OFF");
            editor.putString("interDeffState","OFF");
            editor.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDrlDelay:
                //Toast.makeText(this,"DEBUG DRL SAVE FUNCTION",Toast.LENGTH_SHORT).show();
                TextView tmpDrl = findViewById(R.id.editDrlDelay);
                drlDelay = tmpDrl.getText().toString();
                updateValues();
                break;
            case R.id.btnInterDelay:
                //Toast.makeText(this,"DEBUG INTERIOR SAVE FUNCTION",Toast.LENGTH_SHORT).show();
                TextView tmpInter = findViewById(R.id.editInterDelay);
                interDelay = tmpInter.getText().toString();
                updateValues();
                break;
            case R.id.tglDrlState:
                ToggleButton tmpTglDrl = findViewById(R.id.tglDrlState);
                //Toast.makeText(this,"DEBUG DRL TOGGLE FUNCTION",Toast.LENGTH_SHORT).show();
                if(drlDeffState.equals("OFF")){
                    drlDeffState = "ON";
                    tmpTglDrl.setChecked(true);
                }else{
                    drlDeffState = "OFF";
                    tmpTglDrl.setChecked(false);
                }
                updateValues();
                break;
            case R.id.tglInterState:
                ToggleButton tmpTglInter = findViewById(R.id.tglInterState);
                //Toast.makeText(this,"DEBUG INTERIOR TOGGLE FUNCTION",Toast.LENGTH_SHORT).show();
                if(interDeffState.equals("OFF")){
                    interDeffState = "ON";
                    tmpTglInter.setChecked(true);
                }else{
                    interDeffState = "OFF";
                    tmpTglInter.setChecked(false);
                }
                updateValues();
                break;
            default:
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateValues(){
        //Toast.makeText(this,"NEW VALUE FOR ELEMENT " + element + " IS " + value,Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contextOfApplication);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("drlState", drlState);
        editor.putString("interState",interState);
        editor.putString("drlDelay",drlDelay);
        editor.putString("interDelay",interDelay);
        editor.putString("drlDeffState",drlDeffState);
        editor.putString("interDeffState",interDeffState);
        editor.commit();
        //status(drlState,interState);
    }
}

