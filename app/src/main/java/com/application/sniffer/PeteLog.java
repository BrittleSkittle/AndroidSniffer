package com.application.sniffer;
import com.application.sniffer.Fire;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;


public class PeteLog{
    public String type;
    public String name;
    public long time;
    public String message;
    int num;
    private String TAG = "peteLog";
    //public PeteLog(){}
    public PeteLog(String n, String t, String m){
        setName(n);
        setType(t);
        setMessage(m);
        setTime();
        num++;
        Fire.uploadFireLog(Msg(), this);
        uploadLog();
    }


    void uploadLog(){
        String mes = Msg();
        Map<String, Object> data = new HashMap<>();
        data.put(Integer.toString(1), mes);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PeteLog").add(data);
    }


    String Msg(){
        return "TAG: "+ getName()+"; Type: "+getType()+"; Message: "+getMessage()+"; Time: "+getTime()+";";
    }


    void setName(String n){
        this.name = n;
    }
    String getName(){
        return name;
    }
    void setMessage(String m){
        this.message = m;
    }
    String getMessage(){
        return message;
    }
    void setType(String t){
        this.type = t;
    }
    String getType(){
        return type;
    }
    private void setTime(){
        this.time = System.currentTimeMillis()-MainActivity.StartTime;
    }
    long getTime(){
        return time;
    }






  /*  private void uploaddb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String mes = Msg();
        Map<String, Object> data = new HashMap<>();
        data.put(Integer.toString(1), mes);
        myRef.setValue(data);
    }*/
}
