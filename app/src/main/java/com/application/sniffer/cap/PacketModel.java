/*
package com.application.sniffer.cap;

import android.database.Observable;

import com.jude.beam.model.AbsModel;

import org.jnetpcap.Pcap;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import java.util.Arrays;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PacketModel extends AbsModel {

    public static PacketModel getInstance() {
        return getInstance(PacketModel.class);
    }

    public Observable<PacketItem> getPacketsFromFile(String file){
        return Observable.create((Observable.OnSubscribe<PacketItem>) subscriber -> {
            Pcap pcap = Pcap.openOffline(file, new StringBuilder());
            PcapPacket detailsPacket = new PcapPacket(JMemory.POINTER);
            Tcp tcp = new Tcp();
            Udp udp = new Udp();
            Arp arp = new Arp();
            while (pcap.nextEx(detailsPacket)==Pcap.NEXT_EX_OK){
                //type
                PacketItem item = new PacketItem();
                byte[] data = null;
                if (detailsPacket.hasHeader(tcp)) {
                    tcp = detailsPacket.getHeader(tcp);
                    item.setSport(tcp.source());
                    item.setDport(tcp.destination());
                    data = tcp.getPayload();
                    if(tcp.source()==80 || tcp.destination()==80){
                        item.setType(PacketItem.HTTP);
                    }else if(tcp.source()==23){
                        item.setType(PacketItem.Telnet);
                    }else{
                        item.setType(PacketItem.TCP);
                    }

                } else if(detailsPacket.hasHeader(udp)) {
                    udp = detailsPacket.getHeader(udp);
                    item.setType(PacketItem.UDP);
                    item.setSport(udp.source());
                    item.setDport(udp.destination());
                    data = udp.getPayload();
                } else if(detailsPacket.hasHeader(arp)) {
                    item.setType(PacketItem.ARP);
                } else {
                    item.setType(PacketItem.UNKNOWN);
                }

                //address
                String sip=null, dip=null;
                Ip4 ip = new Ip4();
                Ip6 ip6 = new Ip6();
                if(detailsPacket.hasHeader(ip)) {
                    sip = FormatUtils.ip(ip.source());
                    dip = FormatUtils.ip(ip.destination());
                } else if(detailsPacket.hasHeader(ip6)) {
                    sip = FormatUtils.ip(ip6.source());
                    dip = FormatUtils.ip(ip6.destination());
                }
                item.setSip(sip);
                item.setDip(dip);
                item.setData(new String(data != null ? data : new byte[0]));

                //length
                item.setLength(detailsPacket.getTotalSize());

                //time
                item.setTime(detailsPacket.getCaptureHeader().timestampInMillis()/1000);
                subscriber.onNext(item);
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
*/
