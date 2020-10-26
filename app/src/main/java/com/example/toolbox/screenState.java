package com.example.toolbox;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class screenState extends Service {
    Context applicationContext = MainActivity.getContextOfApplication();
    private static final String DRL_BTN_EVENT = "drlState";
    private static final String INTERIOR_BTN_EVENT = "interiorState";

    switches switchFunc = new switches();
    String reader="";
    String readerTmp="";
    String btStat="";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    OnClickListener newListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //Log.d("DEBUG", "NEW BUTTON CLICKED > " + view.getId());
            if(view.getId() == 1) {
                switchFunc.updateState(DRL_BTN_EVENT);
                reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
                readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
                btStat = switchFunc.getStates("bluetooth");
                status(readerTmp,reader,btStat);
            }else if(view.getId() == 2) {
                switchFunc.updateState(INTERIOR_BTN_EVENT);
                reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
                readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
                btStat = switchFunc.getStates("bluetooth");
                status(readerTmp,reader,btStat);
            }else if(view.getId() == 3) {
                Toast.makeText(applicationContext, "DVR Button test", Toast.LENGTH_SHORT).show();
                reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
                readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
                btStat = switchFunc.getStates("bluetooth");
                status(readerTmp,reader,btStat);
            }else{
                reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
                readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
                btStat = switchFunc.getStates("bluetooth");
                status(readerTmp,reader,btStat);
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        reader = switchFunc.getStates(INTERIOR_BTN_EVENT);
        readerTmp = switchFunc.getStates(DRL_BTN_EVENT);
        btStat = switchFunc.getStates("bluetooth");
        status(readerTmp,reader,btStat);
        // if screen is on - start worker.service else stop worker.service
    }
    public void status(String drl,String inter,String btStatus){
        int statusBarHeight = 270;
        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                statusBarHeight,
                WindowManager.LayoutParams.TYPE_TOAST,   // Allows the view to be on top of the StatusBar
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,     // Keeps the button presses from going to the background window and Draws over status bar
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.BOTTOM | Gravity.LEFT;
        parameters.width = 220;



        LinearLayout ll = new LinearLayout(applicationContext);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(150,200);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.setLayoutParams(layoutParameteres);
        ll.setPadding(10,10,10,10);
        Button btn = new Button(applicationContext);
        btn.setShadowLayer(15,0,0,Color.BLUE);
        if(drl.equals("ON")){
            btn.setText("DRL");
            btn.setTextColor(Color.CYAN);
            Drawable img = btn.getContext().getResources().getDrawable( R.drawable.icn_on );
            btn.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        }else{
            btn.setText("DRL");
            btn.setTextColor(Color.CYAN);
            Drawable img = btn.getContext().getResources().getDrawable( R.drawable.icn_off );
            btn.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        }

        btn.setWidth(50);
        btn.setHeight(65);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn.setId(1);
        btn.setGravity(Gravity.LEFT | Gravity.CENTER);
        btn.setOnClickListener(newListener);
        ll.addView(btn);

        Button btn1 = new Button(applicationContext);
        btn1.setShadowLayer(15,0,0,Color.BLUE);
        if(inter.equals("ON")){
            btn1.setText("INT");
            btn1.setTextColor(Color.CYAN);
            Drawable img = btn1.getContext().getResources().getDrawable( R.drawable.icn_on );
            btn1.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        }else{
            btn1.setText("INT");
            btn1.setTextColor(Color.CYAN);
            Drawable img = btn1.getContext().getResources().getDrawable( R.drawable.icn_off );
            btn1.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        }

        btn1.setWidth(50);
        btn1.setHeight(65);
        btn1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn1.setId(2);
        btn1.setGravity(Gravity.LEFT | Gravity.CENTER);
        btn1.setOnClickListener(newListener);
        ll.addView(btn1);

        Button btn2 = new Button(applicationContext);
        btn2.setShadowLayer(15,0,0,Color.BLUE);
        btn2.setText("DVR ON");
        btn2.setTextColor(Color.CYAN);
        btn2.setWidth(50);
        btn2.setHeight(65);
        btn2.setId(3);
        btn2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        btn2.setGravity(Gravity.LEFT | Gravity.CENTER);
        btn2.setOnClickListener(newListener);
        ll.addView(btn2);

        Button btn3 = new Button(applicationContext);

        if(btStatus.equals("ONLINE")){
            btn3.setText(btStatus);
            btn3.setTextColor(Color.GREEN);
            btn3.setShadowLayer(15,0,0,Color.GREEN);
        }else{
            btn3.setText(btStatus);
            btn3.setTextColor(Color.RED);
            btn3.setShadowLayer(15,0,0,Color.RED);
        }

        btn3.setWidth(50);
        btn3.setHeight(65);
        btn3.setId(4);
        btn3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        btn3.setGravity(Gravity.LEFT | Gravity.CENTER);
        btn3.setOnClickListener(newListener);
        ll.addView(btn3);

        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(ll, parameters);
    }
}
