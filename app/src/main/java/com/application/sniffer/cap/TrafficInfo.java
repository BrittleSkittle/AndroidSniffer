package com.application.sniffer.cap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficInfo {

    public static Map<String,Long> collectTxbyApps(Context context){
        Map<String,Long> info = new HashMap<>();
        List<ApplicationInfo> apps = context.getPackageManager()
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo app:apps) {
            long tx = TrafficStats.getUidTxBytes(app.uid);
            if(tx != 0)
                info.put(app.packageName, tx);
        }
        return info;
    }

    public static Map<String,Long> collectTxPackets(Context context){
        Map<String,Long> info = new HashMap<>();
        List<ApplicationInfo> apps = context.getPackageManager()
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo app:apps) {
            long tx = TrafficStats.getUidUdpRxPackets(app.uid);
            if(tx != 0)
                info.put(app.packageName, tx);
        }
        return info;
    }


}
