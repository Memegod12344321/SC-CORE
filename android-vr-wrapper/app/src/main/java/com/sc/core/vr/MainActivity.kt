package com.sc.core.vr

import android.app.*
import android.content.*
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.view.*
import android.widget.*

class MainActivity: Activity(){
 private val req=42
 override fun onCreate(b:Bundle?){super.onCreate(b); val box=LinearLayout(this);box.orientation=LinearLayout.VERTICAL;box.setPadding(32,32,32,32)
  val title=TextView(this);title.text="Hay Day VR Wrapper";title.textSize=28f;box.addView(title)
  val info=TextView(this);info.text="Uses the Hay Day app already installed on your phone. This wrapper does not modify or redistribute Hay Day.";box.addView(info)
  val overlay=Button(this);overlay.text="1. Allow display over other apps";overlay.setOnClickListener{startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))};box.addView(overlay)
  val capture=Button(this);capture.text="2. Start VR capture";capture.setOnClickListener{val m=getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager;startActivityForResult(m.createScreenCaptureIntent(),req)};box.addView(capture)
  val launch=Button(this);launch.text="Launch Hay Day";launch.setOnClickListener{packageManager.getLaunchIntentForPackage("com.supercell.hayday")?.let{startActivity(it)}?:Toast.makeText(this,"Install Hay Day first",Toast.LENGTH_LONG).show()};box.addView(launch)
  setContentView(box)
 }
 override fun onActivityResult(r:Int,c:Int,d:Intent?){super.onActivityResult(r,c,d);if(r==req&&c==RESULT_OK&&d!=null){val i=Intent(this,CaptureService::class.java).apply{putExtra("resultCode",c);putExtra("data",d)};if(Build.VERSION.SDK_INT>=26)startForegroundService(i) else startService(i);Toast.makeText(this,"Capture service started",Toast.LENGTH_SHORT).show()}}
}
