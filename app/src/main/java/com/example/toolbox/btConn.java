package com.example.toolbox;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

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
    byte[] readBuffer;
    int readBufferPosition;
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
                if(device.getName().equals("Redmi note 8t"))
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
            }
        });
        workerThread.start();
    }

    void updSett(String drlDefState,String drlDefDelay,String interDefState,String interDefDelay){
        if(mmSocket.isConnected()) {
            String msgA = "[INTERIOR STATE => " + interDefState + "] "+ "\n" + "[INTERIOR DELAY => " + interDefDelay + "]" + "\n" +" [DRL STATE => " + drlDefState + "]" + "\n" +"  [DRL DELAY => " + drlDefDelay + "]";
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
            String msg = "[" +  counter + "] DRL state -> [" + drl +  "] " + "\n" +" Interior state -> [" + inter + "]";
            msg += "\n";
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
}
