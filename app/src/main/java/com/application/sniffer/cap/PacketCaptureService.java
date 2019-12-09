package com.application.sniffer.cap;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.application.sniffer.MainActivity;
import com.application.sniffer.PeteLog;


public class PacketCaptureService extends VpnService implements Runnable {
    public static final String KEY_CMD = "cmd";
    public static final String KEY_FILE = "file";
    public static final int CMD_STARTVPN = 1;
    public static final int CMD_STOPVPN = 2;

    private static String mLogFileName;
    private PendingIntent a;
    private Thread mThread;
    private ParcelFileDescriptor mParcelFileDescriptor;

    private static boolean isRunning;

    static {
        System.loadLibrary("tPacketCapture");
    }

    public static String getCurFile() {
        return mLogFileName;
    }

    public static boolean isRunning(){
        return isRunning;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("VPNService","onCreate");
    }

    public void onDestroy() {
        Log.i("VPNService","onDestroy");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
    }

    public void onRevoke() {
        Log.i("VPNService","passive stop");
        stopVPN();
    }

    public void stopVPN(){
        Log.i("VPNService","stop "+ (System.currentTimeMillis()- MainActivity.StartTime));
        stopSelf();
        stopCapture();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
        int cmd = intent.getIntExtra(KEY_CMD,CMD_STARTVPN);
        switch (cmd){
            case CMD_STARTVPN:
                    isRunning=true;
                    mLogFileName = intent.getStringExtra(KEY_FILE);
                    this.mThread = new Thread(this, "PacketCaptureThread");
                    this.mThread.start();
                    Log.i("VPNService","launch "+(System.currentTimeMillis()- MainActivity.StartTime));
                break;
            case CMD_STOPVPN:
                isRunning=false;
                Log.i("VPNService","active stop");
                stopVPN();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    public synchronized void run() {
        new PeteLog("Packetcaptureservice", "info", "running");
        try {
            //setUidCaptureStatus(false);
            //setPCapFileName(mLogFileName);
            Builder builder = new Builder();
            builder.addAddress("10.8.0.1", 32);
            builder.addRoute("0.0.0.0", 0);
            builder.setSession("Interceptor Session");
            builder.setConfigureIntent(this.a);
            this.mParcelFileDescriptor = builder.establish();
            new PeteLog("Packetcaptureservice", "packet", this.mParcelFileDescriptor.toString());
            startCapture(this.mParcelFileDescriptor.getFd());
            new PeteLog("Packetcaptureservice", "packet", this.mParcelFileDescriptor.toString());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e) {
                Log.i("A", e.getMessage());
            }
            this.mParcelFileDescriptor = null;
        } catch (Exception e2) {
            Log.i("B", e2.getMessage());
            new StringBuilder("Got ").append(e2.toString());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e3) {
                Log.i("E",e3.getMessage());
            }
            this.mParcelFileDescriptor = null;
        } catch (Throwable th) {
            Log.i("C",th.getMessage());
            try {
                this.mParcelFileDescriptor.close();
            } catch (Exception e4) {
                Log.i("D",e4.getMessage());
            }
            this.mParcelFileDescriptor = null;
        }


    }


    private native void insertUid(int i);

    private native void setUidCaptureStatus(boolean z);

    public native void setPCapFileName(String str);

    public native void startCapture(int i);

    public native void stopCapture();
}