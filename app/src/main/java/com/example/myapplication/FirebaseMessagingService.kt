package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val title = message.data["title"] ?: message.notification?.title ?: "CatCards"
        val body = message.data["message"] ?: message.notification?.body ?: return
        val avatar = message.data["avatar"] ?: "img"
        
        // Получаем наш текущий статус "онлайн" или то, с кем мы в активном чате
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val currentNickname = prefs.getString(NICKNAME, null)
        
        if (currentNickname != null) {
            // Читаем из Firebase статус, так как сервис выполняется в отдельном потоке
            FirebaseDatabase.getInstance()
                .getReference("users/$currentNickname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val isOnline = snapshot.child("is_online").getValue(Boolean::class.java) ?: false
                        val activeChatWith = snapshot.child("active_chat_with").getValue(String::class.java) ?: ""

                        // ВАЖНО: Если мы ОНЛАЙН, то In-App баннер покажет само приложение (в MainActivity).
                        // FirebaseMessagingService вообще не должен выводить системный пуш в таком случае.
                        if (isOnline) {
                            Log.d("FCM", "Уведомление заблокировано: пользователь онлайн (отрисуется In-App баннер)")
                            return
                        }
                        
                        // Если мы ОФФЛАЙН, но почему-то activeChatWith совпадает (например приложение крашнулось и не сбросило статус)
                        if (activeChatWith == title) {
                            Log.d("FCM", "Уведомление заблокировано: пользователь в диалоге с отправителем")
                            return
                        }

                        // Если всё в порядке (мы оффлайн и не в чате) - показываем системное уведомление
                        showAdminMessageNotification(this@FirebaseMessagingService, body, title, avatar)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // При ошибке БД на всякий случай показываем уведомление
                        showAdminMessageNotification(this@FirebaseMessagingService, body, title, avatar)
                    }
                })
        } else {
            // Если мы не авторизованы, просто показываем пуш
            showAdminMessageNotification(this, body, title, avatar)
        }
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
