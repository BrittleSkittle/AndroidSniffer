package com.application.sniffer;
import android.app.Activity;
import android.bluetooth.*;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class BlueTooth extends AppCompatActivity{

    public static void start(Activity activity) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            new PeteLog("Bluetooth", "info", "bluetooth not supported");
            bluetoothAdapter.enable();
        }
        else{
            bluetoothAdapter.startDiscovery();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            Map<String, Object> btInfo = new HashMap<>();
            TextView textView = activity.findViewById(R.id.deviceList);
            for(BluetoothDevice bt : pairedDevices) {
                btInfo.put("Name", bt.getName());
                btInfo.put("Address", bt.getAddress());
                btInfo.put("Type", bt.getType());
                btInfo.put("Connection", "Paired");
                btInfo.put("Time", System.currentTimeMillis()-MainActivity.StartTime);
                Fire.uploadFireMap("BlueTooth", btInfo);
                textView.append(btInfo.toString()+"\n\n");
                btInfo.clear();
            }
            Fire.Clear();
        }
    }

/*    if(bluetoothAdapter.isenabled){
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        Integer REQUEST_ENABLE_BT = 1;
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }*/

}
