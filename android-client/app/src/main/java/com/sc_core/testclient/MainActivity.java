package com.sc_core.localtest;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.net.*;
import java.io.*;

public class MainActivity extends Activity {
    FarmView farm;
    TextView coins, gems, level, status;
    long money=2147483647L, diamonds=2147483647L;
    int wheat=25, corn=12, levelNum=1, xp=0;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(244,236,205));

        LinearLayout top=new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(18,12,18,12);
        top.setBackgroundColor(Color.rgb(76,175,80));
        TextView name=new TextView(this); name.setText("🌾 MY FARM"); name.setTextSize(25); name.setTextColor(Color.WHITE);
        top.addView(name,new LinearLayout.LayoutParams(0,70,1));
        coins=new TextView(this); gems=new TextView(this); level=new TextView(this);
        coins.setTextColor(Color.WHITE); gems.setTextColor(Color.WHITE); level.setTextColor(Color.WHITE);
        coins.setTextSize(14); gems.setTextSize(14); level.setTextSize(14);
        top.addView(coins); top.addView(gems); top.addView(level);
        root.addView(top);

        farm=new FarmView();
        root.addView(farm,new LinearLayout.LayoutParams(-1,0,1));

        LinearLayout bar=new LinearLayout(this); bar.setPadding(8,8,8,8); bar.setGravity(Gravity.CENTER);
        String[] labels={"🌾 Harvest","🌽 Plant","🛒 Sell","🏠 Build","🌐 Server"};
        for(String s:labels){ Button x=new Button(this); x.setText(s); bar.addView(x,new LinearLayout.LayoutParams(0,62,1));
            if(s.contains("Harvest")) x.setOnClickListener(v->{wheat+=5; addXP(5); farm.invalidate(); update();});
            else if(s.contains("Plant")) x.setOnClickListener(v->{corn+=3; addXP(3); farm.invalidate(); update();});
            else if(s.contains("Sell")) x.setOnClickListener(v->{if(wheat>=5){wheat-=5; money=Math.min(2147483647L,money+100); addXP(10); farm.invalidate(); update();}});
            else if(s.contains("Build")) x.setOnClickListener(v->{Toast.makeText(MainActivity.this,"Barn upgraded! 🏠",Toast.LENGTH_SHORT).show(); addXP(25); update();});
            else x.setOnClickListener(v->new Thread(()->testServer()).start());
        }
        root.addView(bar);
        status=new TextView(this); status.setText("Local farm • Server offline"); status.setGravity(Gravity.CENTER); status.setPadding(8,4,8,10);
        root.addView(status);
        setContentView(root); update();
    }
    void addXP(int n){xp+=n;if(xp>=100){levelNum++;xp=0;Toast.makeText(this,"Level up! ⭐",Toast.LENGTH_SHORT).show();}}
    void update(){coins.setText("🪙 "+money);gems.setText("💎 "+diamonds);level.setText("⭐ Lv."+levelNum);}

    void testServer(){try{
        runOnUiThread(()->status.setText("Connecting to SC-CORE..."));
        Socket s=new Socket();s.connect(new InetSocketAddress("127.0.0.1",9339),5000);
        byte[] p=new byte[]{0x27,0x74,0,0,0x1c,0,0,0,0,0,0x18,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        s.getOutputStream().write(p);s.getOutputStream().flush();
        byte[] r=new byte[4096];int n=s.getInputStream().read(r);
        boolean ok=n>=2&&(r[0]&255)==0x4e&&(r[1]&255)==0x84;
        runOnUiThread(()->status.setText(ok?"✓ SC-CORE connected • local world ready":"Connected • unexpected response"));s.close();
    }catch(Exception e){runOnUiThread(()->status.setText("✗ Server offline • "+e.getClass().getSimpleName()));}}

    class FarmView extends View{
        Paint p=new Paint(1);
        FarmView(){super(MainActivity.this);p.setTypeface(Typeface.DEFAULT);}
        protected void onDraw(Canvas c){
            super.onDraw(c); int w=getWidth(),h=getHeight();
            p.setColor(Color.rgb(135,198,92));c.drawRect(0,0,w,h,p);
            p.setColor(Color.rgb(91,157,68));c.drawRect(0,h-100,w,h,p);
            p.setColor(Color.rgb(190,224,137));c.drawCircle(w-70,70,42,p);
            p.setColor(Color.rgb(116,83,48));c.drawRect(30,70,165,155,p);
            p.setColor(Color.rgb(218,166,88));c.drawRect(45,90,150,155,p);
            p.setColor(Color.rgb(120,80,40));c.drawRect(80,125,112,155,p);
            p.setColor(Color.rgb(170,116,55));c.drawRect(0,h-145,w,h-100,p);
            for(int row=0;row<2;row++)for(int col=0;col<4;col++){
                float x=30+col*(w-60)/4f,y=190+row*130;
                p.setColor(Color.rgb(124,84,48));c.drawRoundRect(x,y,x+(w-80)/4f,y+95,12,12,p);
                p.setColor(Color.rgb(95,145,52));c.drawCircle(x+45,y+48,25,p);
                p.setColor(Color.WHITE);p.setTextSize(17);c.drawText((row==0?"🌾":"🌽"),x+28,y+57,p);
            }
            p.setColor(Color.rgb(70,55,35));p.setTextSize(20);c.drawText("Barn",32,45,p);
            p.setTextSize(15);c.drawText("Wheat "+wheat+"  •  Corn "+corn,210,45,p);
        }
    }
}