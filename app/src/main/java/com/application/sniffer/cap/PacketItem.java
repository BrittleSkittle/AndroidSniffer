package com.application.sniffer.cap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class PacketItem {

    public static final int TCP = 1;
    public static final int UDP = 2;
    public static final int ARP = 3;
    public static final int HTTP = 4;
    public static final int Telnet = 5;
    public static final int UNKNOWN = 6;
    @IntDef({TCP, UDP,ARP,HTTP,Telnet, UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    @TYPE
    private int type;
    private String sip;
    private String dip;
    private int sport;
    private int dport;
    private int length;
    private long time;
    private String data;


    public int getType() {
        return type;
    }

    public void setType(@TYPE int type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getSip() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip = sip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDport() {
        return dport;
    }

    public void setDport(int dport) {
        this.dport = dport;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
