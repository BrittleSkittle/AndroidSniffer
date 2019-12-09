package com.application.sniffer.cap;

import android.os.Environment;


import com.application.sniffer.PeteLog;

import java.io.File;

public class FileManager {
    private static final File Directory = new File(Environment.getExternalStorageDirectory(),"PacketCap");

    public static void initFileManager(){
        if (!Directory.isDirectory()){
            Directory.delete();
            Directory.mkdir();
        }
    }

    public static File createNewPacketFile() {
        String name = createNewFileName();
        new PeteLog("FILE", "IMPORTANT", name);
        String path = " "+Environment.getExternalStorageDirectory().getPath()+" ";
        new PeteLog("storage", "important", "storage emulated 0");
        return new File(Directory,name);
    }

    public static File[] listPacketFiles(){
        return Directory.listFiles();
    }

    private static String createNewFileName(){
        long time = (System.currentTimeMillis()/1000);
        return (time +".pcap");
    }
}
