package com.application.sniffer;

import android.util.Log;

import com.application.sniffer.cap.PacketItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Fire {
    private static final String TAG = "fire";

    public static void Clear(){

    }

    public static void uploadFire(String collection, String DocName, PacketItem data) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
/*        Map<String, Object> data = new HashMap<>();
        data.put("Name",s);
        data.put("Type", "test");*/
        db.collection(collection).document(DocName)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        new PeteLog(TAG, "info", "data successfully sent");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        new PeteLog(TAG, "error", "data failed to send");
                        //logs.append(p.getMsg());
                    }
                });
    }
    public static void uploadFireMap(String collection, Map map) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Long time = System.currentTimeMillis()-MainActivity.StartTime;
        db.collection(collection).document(time.toString()).set(map);
        new PeteLog(TAG, "important", "attempted to send btinfo");
        Log.i(TAG, "uploadFire: attempted to send btinfo");
    }

    public static void uploadFireLog(String DocName, PeteLog p){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("logs").document(DocName)
                .set(p)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
