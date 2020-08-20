package com.example.toolbox;
import android.content.Context;
import io.palaima.smoothbluetooth.SmoothBluetooth;

public class bluetoothConn extends SmoothBluetooth {

    public bluetoothConn(Context context, ConnectionTo connectionTo, Connection connection, Listener listener) {
        super(context, connectionTo, connection, listener);
    }


}
