package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService as FcmService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FcmService() {
    private companion object {
        const val PREFS = "user_preferences"
        const val NICKNAME = "nickname"
    }

    override fun onCreate() {
        super.onCreate()
        createAdminNotificationChannel(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: "CatCards"
        val body = message.notification?.body ?: message.data["message"] ?: return
        showAdminMessageNotification(this, body, title)
    }

    override fun onNewToken(token: String) {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val nickname = prefs.getString(NICKNAME, null)
        if (!nickname.isNullOrBlank()) {
            com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("users/$nickname/fcmToken")
                .setValue(token)
                .addOnFailureListener { error ->
                    Log.e("FCM", "Не удалось сохранить обновлённый FCM-токен", error)
                }
        }
    }
}
