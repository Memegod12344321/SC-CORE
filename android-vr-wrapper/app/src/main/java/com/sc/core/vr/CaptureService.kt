package com.sc.core.vr

import android.app.*
import android.content.*
import android.media.projection.MediaProjection
import android.os.*

class CaptureService:Service(){
 private var projection:MediaProjection?=null
 override fun onStartCommand(i:Intent?,flags:Int,id:Int):Int{
  val channel="hayday-vr"; if(Build.VERSION.SDK_INT>=26){val n=getSystemService(NotificationManager::class.java);n.createNotificationChannel(NotificationChannel(channel,"Hay Day VR",NotificationManager.IMPORTANCE_LOW));startForeground(7,Notification.Builder(this,channel).setContentTitle("Hay Day VR").setContentText("Screen capture is active").setSmallIcon(android.R.drawable.ic_menu_view).build())}
  val code=i?.getIntExtra("resultCode",0)?:0; val data=i?.getParcelableExtra<Intent>("data"); val pm=getSystemService(MEDIA_PROJECTION_SERVICE) as android.media.projection.MediaProjectionManager
  if(data!=null) projection=pm.getMediaProjection(code,data)
  return START_STICKY
 }
 override fun onBind(i:Intent?)=null
 override fun onDestroy(){projection?.stop();super.onDestroy()}
}
