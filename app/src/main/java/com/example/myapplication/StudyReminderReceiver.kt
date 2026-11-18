package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.google.firebase.database.FirebaseDatabase

private const val STUDY_PREFS = "study_preferences"
private const val LAST_STUDY_DATE = "last_study_date"       // Для блокировки уведомлений
private const val COUNTER_DATE = "counter_date"             // Для сброса счетчика карточек
private const val LEARNED_TODAY_COUNT = "learned_today_count"
private const val NICKNAME_PREFS = "user_preferences"
private const val NICKNAME_KEY = "nickname"
private const val CHANNEL_ID = "study_reminders"
private const val NOTIFICATION_ID = 1001

fun markAsStudied(context: Context) {
    val today = todayString()
    context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putString(LAST_STUDY_DATE, today)
        .apply()
}

fun incrementLearnedToday(context: Context, flashcard: Flashcard) {
    val today = todayString()
    val preferences = context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)

    val savedDate = preferences.getString(COUNTER_DATE, null)
    
    // Получаем список слов (уникальных), которые уже учили сегодня
    val learnedWords = if (savedDate == today) {
        preferences.getStringSet("learned_words_set", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    } else {
        mutableSetOf()
    }

    // Создаем уникальный ID карточки из её сторон (чтобы не было багов с одинаковыми словами в разных колодах)
    val cardId = "${flashcard.front}_${flashcard.back}"
    
    // Если мы УЖЕ выучили это слово сегодня, просто выходим
    if (learnedWords.contains(cardId)) {
        return
    }

    // Если нет, добавляем в список
    learnedWords.add(cardId)
    val newCount = learnedWords.size

    preferences.edit()
        .putString(COUNTER_DATE, today)
        .putStringSet("learned_words_set", learnedWords)
        .putInt(LEARNED_TODAY_COUNT, newCount)
        .apply()

    // Если выучили 20 карточек - отключаем уведомления на сегодня!
    if (newCount >= 20) {
        markAsStudied(context)
    }

    // Синхронизируем прогресс с Firebase
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null)
    if (!nickname.isNullOrBlank()) {
        val updates = mapOf<String, Any>(
            "learned_today" to newCount,
            "last_active_date" to today
        )
        FirebaseDatabase.getInstance().getReference("users/$nickname").updateChildren(updates)
    }
}

fun getLearnedToday(context: Context): Int {
    val today = todayString()
    val preferences = context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)

    // Используем COUNTER_DATE, чтобы просто сбросить счетчик визуально
    if (preferences.getString(COUNTER_DATE, null) != today) {
        preferences.edit()
            .putString(COUNTER_DATE, today)
            .putInt(LEARNED_TODAY_COUNT, 0)
            .apply()
        return 0
    }

    return preferences.getInt(LEARNED_TODAY_COUNT, 0)
}

private fun todayString(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

class StudyReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val preferences = context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)
        val today = todayString()

        // Уведомление отменится ТОЛЬКО если LAST_STUDY_DATE совпадает с сегодня
        // (а это произойдет только при достижении 20 карточек в incrementLearnedToday)
        if (preferences.getString(LAST_STUDY_DATE, null) == today) {
            return
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Напоминания об учебе",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Время повторить карточки")
            .setContentText("Откройте приложение и продолжите обучение")
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}