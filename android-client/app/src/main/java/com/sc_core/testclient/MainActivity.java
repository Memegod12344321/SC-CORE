package com.sc_core.localtest;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import java.net.*;
import java.io.*;

public class MainActivity extends Activity {
    TextView status, coins, diamonds, wheat;
    long money = 2147483647L;
    long gems = 2147483647L;
    int wheatCount = 25;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.rgb(235, 224, 190));

        TextView title = new TextView(this);
        title.setText("SC-CORE FARM");
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(80, 55, 25));
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("Local Hay-Day-style test world");
        sub.setTextSize(16);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.VERTICAL);
        stats.setPadding(20,20,20,20);
        stats.setBackgroundColor(Color.WHITE);

        coins = new TextView(this);
        diamonds = new TextView(this);
        wheat = new TextView(this);
        coins.setTextSize(19); diamonds.setTextSize(19); wheat.setTextSize(19);
        stats.addView(coins); stats.addView(diamonds); stats.addView(wheat);
        root.addView(stats);

        Button harvest = new Button(this);
        harvest.setText("HARVEST WHEAT  +5");
        root.addView(harvest);
        harvest.setOnClickListener(v -> {
            wheatCount += 5;
            refresh();
        });

        Button sell = new Button(this);
        sell.setText("SELL WHEAT  +100 COINS");
        root.addView(sell);
        sell.setOnClickListener(v -> {
            if (wheatCount >= 5) {
                wheatCount -= 5;
                money = Math.min(2147483647L, money + 100);
                refresh();
            }
        });

        Button connect = new Button(this);
        connect.setText("CONNECT TO SC-CORE");
        root.addView(connect);
        status = new TextView(this);
        status.setTextSize(17);
        status.setPadding(8, 12, 8, 8);
        root.addView(status);
        connect.setOnClickListener(v -> new Thread(this::test).start());

        setContentView(root);
        refresh();
        status.setText("Server: not connected");
    }

    void refresh() {
        coins.setText("🪙 Coins: " + money);
        diamonds.setText("💎 Diamonds: " + gems);
        wheat.setText("🌾 Wheat: " + wheatCount);
    }

    void test() {
        try {
            runOnUiThread(() -> status.setText("Connecting to 127.0.0.1:9339..."));
            Socket s = new Socket();
            s.connect(new InetSocketAddress("127.0.0.1", 9339), 5000);
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
