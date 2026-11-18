package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createAdminNotificationChannel(this)
    }
}

fun createAdminNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(
                "admin_msg_v2",
                "Сообщения от разработчика",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }
}
