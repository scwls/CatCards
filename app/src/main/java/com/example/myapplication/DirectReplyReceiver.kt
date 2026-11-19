package com.example.myapplication

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.RemoteInput
import com.google.firebase.database.FirebaseDatabase

class DirectReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.action != "REPLY_ACTION") return
        
        val recipientNickname = intent.getStringExtra("recipient_nickname") ?: return
        
        val remoteInput: Bundle = RemoteInput.getResultsFromIntent(intent) ?: return
        val replyText = remoteInput.getCharSequence("key_text_reply")?.toString()?.trim()
        
        if (!replyText.isNullOrBlank()) {
            val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
            val currentNickname = prefs.getString("nickname", null)
            val userAvatar = prefs.getString("user_avatar", "img") ?: "img"
            
            if (currentNickname != null) {
                // 1. Отправляем сообщение в Firebase (в inbox собеседника)
                val newMessage = mapOf(
                    "text" to replyText,
                    "timestamp" to System.currentTimeMillis()
                )
                FirebaseDatabase.getInstance().getReference("chat_inbox/$recipientNickname/$currentNickname")
                    .push().setValue(newMessage)
                    
                // 2. Инициируем отправку пуша собеседнику (через новую функцию смарт-проверки)
                val notificationData = mapOf(
                    "action" to "CHAT_MESSAGE",
                    "sender" to currentNickname,
                    "message" to replyText,
                    "avatarId" to userAvatar
                )
                sendSmartNotification(context, recipientNickname, notificationData)
                    
                // 3. Сохраняем в локальную историю чата
                val history = loadChatHistory(context, recipientNickname).toMutableList()
                history.add(ChatMessage(replyText, isFromMe = true))
                saveChatHistory(context, recipientNickname, history)
                
                // 4. Гасим уведомление, на которое ответил пользователь
                val notificationId = intent.getIntExtra("notification_id", 0)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }
        }
    }
}