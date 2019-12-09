package com.application.sniffer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.sniffer.cap.FileManager;
import com.application.sniffer.cap.PacketCaptureService;
import com.application.sniffer.cap.PacketItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity{
    public static final long StartTime = System.currentTimeMillis();
    private static final String TAG = "MainActivity";
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getperms();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Intent intent = new Intent(this, Report.class);
        /*intent2.putExtra(PacketCaptureService.KEY_FILE, FileManager.createNewPacketFile().getPath());
        intent2.putExtra(PacketCaptureService.KEY_CMD,
                PacketCaptureService.isRunning()?PacketCaptureService.cmdStop:PacketCaptureService.cmdStart);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signInAnonymously(mAuth);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Viewing reports", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setContentView(R.layout.activity_pete_log_view);
                startActivity(intent);
            }
        });

        final Intent intent2 = new Intent(this, Devices.class);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
        //uploadLog();


    }

    private void getperms() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 3);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            new PeteLog("Perms", "info", "Permission Granted");
        }
        else{
            new PeteLog("Perms", "warning", "Permission NOT Granted");
        }

        FileManager.initFileManager();
    }
    public static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Map<String, Object> btInfo = new HashMap<>();
            String action = intent.getAction();
            new PeteLog("MainActivity BR", "BTinfo", "mReciever called");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btInfo.put("Name", device.getName() );
                btInfo.put("Address", device.getAddress());
                btInfo.put("Type", device.getType());
                btInfo.put("Connection", "Discovered");
                btInfo.put("Time", System.currentTimeMillis()-MainActivity.StartTime);
                new PeteLog("MainActivity", "info", "Broadcast action found");
                Fire.uploadFireMap("BlueTooth", btInfo);
            }

        }
    };


    public PacketItem Sniff() {
        PacketItem item = new PacketItem();
        item.setTime(System.currentTimeMillis());
        return item;
    }




    private void signInAnonymously(FirebaseAuth mAuth) {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.i(TAG, "onSuccess: signed in");
                new PeteLog(TAG, "info", "Signed in");
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        new PeteLog(TAG, "error", "Failed to sign in");
                    }
                });
    }



    @SuppressLint("SetTextI18n")
    public void start(View view) {
        new PeteLog("MainActivity", "info", "start button pressed");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!running) {
            result();


            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter);
            bluetoothAdapter.startDiscovery();
            //BlueTooth.start(this);


            TextView oldStatus = findViewById(R.id.start_text);
            oldStatus.setText("");
            TextView newStatus = findViewById(R.id.status);
            newStatus.setText("sniffing...");
            Log.i(TAG, "Started Sniffer method");
            Button oldStart = findViewById(R.id.Start_button);
            oldStart.setText("STOP");

            running = true;
        } else {
            unregisterReceiver(mReceiver);
            bluetoothAdapter.cancelDiscovery();
            TextView oldStatus = findViewById(R.id.start_text);
            oldStatus.setText("");
            TextView newStatus = findViewById(R.id.status);
            newStatus.setText(R.string.message_start);
            Log.i(TAG, "Stopped sniffing");
            Button oldStart = findViewById(R.id.Start_button);
            oldStart.setText("START");
            running = false;
        }
    }



protected void result(){
    Intent intent2 = new Intent(this, PacketCaptureService.class);
    intent2.putExtra(PacketCaptureService.KEY_FILE, FileManager.createNewPacketFile().getPath());
    intent2.putExtra(PacketCaptureService.KEY_CMD, PacketCaptureService.isRunning()?PacketCaptureService.CMD_STOPVPN:PacketCaptureService.CMD_STARTVPN);
    startService(intent2);

}
















/* public void upload(String s){
            // do your stuff

            // Uri file = Uri.fromFile(new File("/storage/self/primary/Pictures/Screenshots/Screenshot_20191014-174026.png"));
            byte[] bytes;
            bytes = s.getBytes();
            StorageReference sr = mStorageRef.child("CurData");
            sr.putBytes(bytes);
    }*/


   /* public void uploaddb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }*/


}


