package com.example.toolbox;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownServiceException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.util.Set;
import java.util.UUID;


public class btConn extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    volatile boolean stopWorker;
    Thread workerThread;
    int counter;
    switches sw = new switches();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new btListener();
        new Thread(runnable).start();
    }

    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Log.d("DEBUG","Bluetooth not found");
            sw.updateBluethhothState(false);

        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))
                {
                    mmDevice = device;
                    break;
                }else{
                    sw.updateBluethhothState(false);
                }
            }
        }
        Log.d("DEBUG","Bluetooth found > " + mmDevice.getName() + " <> " + mmDevice.getAddress());
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();
        //listenBt();
        sw.updateBluethhothState(true);
        Log.d("DEBUG","Bluetooth OPENED");

    }
    void beginListenForData() {
        workerThread = new Thread(new Runnable() {
            public void run() {
                // read mmInputStream and get data from it
                byte[] bytes = new byte[1000];
                StringBuilder x = new StringBuilder();

                int numRead = 0;
                while (mmSocket.isConnected()) {
                    try {
                        if (!((numRead = mmInputStream.read(bytes)) >= 0)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    x.setLength(0);
                    x.append(new String(bytes, 0, numRead));
                    //Log.d("DEBUG","[BLUETOOTH]=> " + x);
                    decodeMsg(x.toString());
                }
            }
        });
        workerThread.start();
    }

    void updSett(String drlDefState,String drlDefDelay,String interDefState,String interDefDelay){
        if(mmSocket.isConnected()) {
            String msgA = "drdst%"+drlDefState+"drdel"+drlDefDelay+"indst"+interDefState+"inddel"+interDefDelay;
            msgA += "\n";
            try {
                mmOutputStream.write(msgA.getBytes());
            } catch (Exception ext) {
                Log.d("DEBUG", "Error UPDATE DATA >> " + ext);
            }
            Log.d("DEBUG", "DATA HAS UPDATED");
        }else{
            findBT();
            try {
                closeBT();
                openBT();
            } catch (IOException ignored) {}
        }
    }
    void sendData(String drl,String inter) {
        if(mmSocket.isConnected()) {
            String msg = "drl%" + drl + "&int%" + inter;
            msgA += "\n";
            try {
                mmOutputStream.write(msg.getBytes());
            } catch (Exception ex) {
                Log.d("DEBUG", "Error SendData >> " + ex);
            }
            Log.d("DEBUG", "DATA SENT");
        }else{
            findBT();
            try {
                closeBT();
                openBT();
            } catch (IOException ignored) {}
        }

    }


    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        sw.updateBluethhothState(false);
        Log.d("DEBUG","Bluetooth closed");
    }

    Boolean isItConnected(){
        return mmSocket.isConnected();
    }

    void decodeMsg(String msg){
        switch(msg){
            case "imConnected": // arduino is connected -> start functions

                break;
            case "sendSettings": // arduino ask for default settings

                break;
            default:
                Log.d("DEBUG","FUNCTION DECODE_MSG ERROR");

        }
    }
}
