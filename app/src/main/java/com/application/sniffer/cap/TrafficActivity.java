package com.application.sniffer.cap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.application.sniffer.R;
import com.application.sniffer.cap.TrafficInfo;

import java.util.Map;

public class TrafficActivity extends Activity {

    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv1.setText("Bytes:\n");
        for (Map.Entry<String,Long> entry: TrafficInfo.collectTxbyApps(this).entrySet()) {
            tv1.append(entry.toString()+"\n");
        }
    }
}
