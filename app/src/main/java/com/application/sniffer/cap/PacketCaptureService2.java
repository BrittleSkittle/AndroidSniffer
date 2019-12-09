package com.application.sniffer.cap;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;


public class PacketCaptureService2 extends VpnService implements Runnable{
    public static final String TAG = "PacketCaptureService";
    public static final String KEY_CMD = "cmd";
    public static final String KEY_FILE = "file";
    public static final int cmdStart = 1;
    public static final int cmdStop = 2;


    private static String CurrentFile;
    private PendingIntent pendingIntent;
    private Thread thread;
    private ParcelFileDescriptor parcelFileDescriptor;

    private static Boolean isRunning;

    public static String getCurrentFile(){
        return CurrentFile;
    }

    public static boolean isRunning(){
        return isRunning;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG, "Created");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "Destroyed");
    }

    public void onRevoke(){
        super.onRevoke();
        Log.i(TAG, "Stopped");
    }
    public void Stop(){
        stopSelf();
        stopCapture();
        isRunning = false;
    }

    public int Start(Intent intent, int i, int j){
        if(this.thread!=null){
            this.thread.interrupt();
        }
        int cmd = intent.getIntExtra(KEY_CMD, cmdStart);
        switch(cmd){
            case cmdStart:
                if(isRunning()){
                    Log.e(TAG, "already running");
                }
                else{
                    CurrentFile = intent.getStringExtra(KEY_FILE);
                    this.thread=new Thread(this, "PacketCaptureThread");
                    this.thread.start();
                    isRunning = true;
                    Log.i(TAG, "Launched");
                }
                break;
            case cmdStop:
                Log.d(TAG, "Stopped");
                Stop();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public synchronized void run() {

        setUidCaptureStatus(false);
        setPCapFileName(CurrentFile);
        Builder builder = new Builder();
        builder.addAddress("10.8.0.1", 32);
        builder.addRoute("0.0.0.0", 0);
        builder.setSession("Capture Session");
        builder.setConfigureIntent(this.pendingIntent);
        this.parcelFileDescriptor = builder.establish();
        startCapture(this.parcelFileDescriptor.getFd());
        try {
            this.parcelFileDescriptor.close();
        } catch (Exception e) {
            Log.e(TAG, "run: " + e.getMessage());
        }
        this.parcelFileDescriptor = null;

    }


    private native void insertUid(int i);

    private native void setUidCaptureStatus(boolean z);

    public native void setPCapFileName(String str);

    public native void startCapture(int i);

    public native void stopCapture();
}
