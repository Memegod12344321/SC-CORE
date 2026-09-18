package com.sc_core.testclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import java.net.*;
import java.io.*;

public class MainActivity extends Activity {
    TextView status;
    public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(32,32,32,32);
        TextView title = new TextView(this);
        title.setText("SC-CORE Local Test Client");
        title.setTextSize(24);
        l.addView(title);
        status = new TextView(this);
        status.setText("Ready");
        status.setTextSize(18);
        l.addView(status);
        Button connect = new Button(this);
        connect.setText("CONNECT TO SC-CORE");
        l.addView(connect);
        connect.setOnClickListener(v -> new Thread(this::test).start());
        setContentView(l);
    }
    void test() {
        try {
            runOnUiThread(() -> status.setText("Connecting to 127.0.0.1:9339..."));
            Socket s = new Socket();
            s.connect(new InetSocketAddress("127.0.0.1",9339),5000);
            byte[] packet = new byte[] {0x27,0x74,0,0,0x1c,0,0,0,0,0,0x18,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
            s.getOutputStream().write(packet);
            s.getOutputStream().flush();
            byte[] response = new byte[4096];
            int n = s.getInputStream().read(response);
            boolean ok = n >= 2 && (response[0]&255)==0x4e && (response[1]&255)==0x84;
            runOnUiThread(() -> status.setText(ok ? "✓ SC-CORE connected — 20100 received" : "Connected, unexpected response"));
            s.close();
        } catch (Exception e) {
            runOnUiThread(() -> status.setText("✗ " + e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
    }
}
