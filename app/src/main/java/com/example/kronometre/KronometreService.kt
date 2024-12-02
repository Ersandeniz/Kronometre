package com.example.kronometre

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat

class KronometreService:Service() {
    private val TAG ="KronometreService"
    private var elapsedTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val runnable =object:Runnable{
        override fun run() {
            elapsedTime++
            updateNotification(elapsedTime)
            Log.d(TAG , "kronometre... $elapsedTime second")
            handler.postDelayed(this , 1000)
        }

    }

    override fun onCreate() {
        createNotificationChannel()
        Log.d(TAG, "SErvis oncreatw")
        super.onCreate()
    }
    private fun  createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID , CHANNEL_NAME , NotificationManager.IMPORTANCE_HIGH    )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
            manager.createNotificationChannel(channel)
        }
        Log.d(TAG , "CREATE NOTOFOCATİON CHANNEL ÇAĞIRIDŞI")
    }

    private fun buildNotification(){
        val notification = NotificationCompat.Builder(this , CHANNEL_ID)
            .setContentTitle("Kronometre")
            .setContentText("Geçen süre : 0 saniye")
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_timer )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(FOREGROUND_ID, notification)
        Log.d(TAG, "Notification build çağırıldı")
    }
    private fun updateNotification(elapsedTime:Long){
        val notification = NotificationCompat.Builder(this , CHANNEL_ID)
            .setContentTitle("Kronometre")
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentText("Geçen süre: $elapsedTime saniye  ")
            .setSmallIcon(R.drawable.ic_timer )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        //sürekli olarak yeni bilgi güncellenecek
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        notificationManager.notify(FOREGROUND_ID , notification)

    }


    override fun onBind(intent: Intent?): IBinder? =null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

       handler.postDelayed(runnable,1000)
        Log.d(TAG , "OnStartComman başlatıldı...")
        buildNotification()
        return START_STICKY // bildirimin gitmesini istemediğimiz için
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        Log.d(TAG , "Service durduruldu")
    }
    companion object{
        private const val  CHANNEL_ID = "KronometreID"
        private const val CHANNEL_NAME = "KronometreName"
        private const val FOREGROUND_ID = 1
    }
}