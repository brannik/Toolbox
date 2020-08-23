package com.example.toolbox;
import android.util.Log;

public class btListener implements Runnable{

    @Override
    public void run() {
        Log.d("DEBUG","Background task");
    }
}
