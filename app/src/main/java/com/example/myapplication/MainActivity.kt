package com.example.myapplication

import android.Manifest
import android.R
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import android.provider.OpenableColumns
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.lerp
import kotlin.math.abs
import kotlin.math.min
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.draw.clip
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Switch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.graphics.Brush
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import androidx.core.app.NotificationCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import java.nio.charset.StandardCharsets
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.key
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import java.io.Serializable
import java.lang.IllegalStateException
import kotlin.math.floor
import kotlin.math.sqrt

private const val STUDY_PREFS = "study_preferences"
private const val LAST_STUDY_DATE = "last_study_date"
private const val NICKNAME_PREFS = "user_preferences"
private const val NICKNAME_KEY = "nickname"
private const val FIREBASE_STORAGE_BUCKET = "gs://card-eng.firebasestorage.app"
private const val ERROR_REPORT_MAX_BYTES = 5L * 1024L * 1024L

object XpSystem {
    private const val XP_PREFS = "xp_prefs"
    private const val TOTAL_XP = "total_xp"
    private const val CURRENT_STREAK = "current_streak"
    private const val LAST_STREAK_DATE = "last_streak_date"
    private const val CARDS_STUDIED_TODAY_QUEST = "cards_studied_today_quest"
    private const val DAILY_QUEST_TARGET = 30

    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    fun awardXp(context: Context, amount: Int) {
        if (amount == 0) return
        val prefs = context.getSharedPreferences(XP_PREFS, Context.MODE_PRIVATE)
        val currentXp = prefs.getInt(TOTAL_XP, 0)
        val newXp = currentXp + amount
        prefs.edit().putInt(TOTAL_XP, newXp).apply()

        val nicknamePrefs = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        val nickname = nicknamePrefs.getString(NICKNAME_KEY, null)
        if (!nickname.isNullOrEmpty()) {
            val dbRef = FirebaseDatabase.getInstance().getReference("users/$nickname/xp")
            dbRef.setValue(newXp)
        }
    }

    private var comboCounter = 0

    fun processCardStudiedForXp(context: Context, cardText: String, mode: String, isCorrect: Boolean): Pair<Int, Int> {
        val today = getTodayString()
        val prefs = context.getSharedPreferences(XP_PREFS, Context.MODE_PRIVATE)
        val todayPrefs = context.getSharedPreferences("xp_today_prefs", Context.MODE_PRIVATE)
        val lastTrackingDate = prefs.getString("last_study_date_tracking", "")
        
        if (today != lastTrackingDate) {
            // Очищаем историю ошибок и наград, так как начался новый день
            todayPrefs.edit().clear().apply()
            prefs.edit()
                .putString("last_study_date_tracking", today)
                .putInt(CARDS_STUDIED_TODAY_QUEST, 0)
                .apply()
        }

        var awardedComboBonus = 0
        var awardedBaseXp = 0
        val cardHash = "${cardText.hashCode()}_$mode"
        
        if (isCorrect) {
            comboCounter++
            if (comboCounter > 0 && comboCounter % 10 == 0) {
                awardedComboBonus = 20
                awardXp(context, awardedComboBonus)
            }

            val alreadyAwarded = todayPrefs.getBoolean("awarded_$cardHash", false)

            if (!alreadyAwarded) {
                todayPrefs.edit().putBoolean("awarded_$cardHash", true).apply()

                val mistakes = todayPrefs.getInt("mistakes_$cardHash", 0)
                
                val baseXP = when (mode.uppercase(Locale.US)) {
                    "SWIPE" -> 2
                    "TILES" -> 4
                    "INPUT" -> 10
                    else -> 2
                }
                
                val finalXP = when (mistakes) {
                    0 -> baseXP
                    1 -> baseXP / 2 // Половина опыта, если была одна ошибка
                    else -> 0       // 0 опыта, если было 2 и более ошибок
                }

                if (finalXP > 0) {
                    awardXp(context, finalXP)
                    awardedBaseXp = finalXP
                }

                val currentQuestCount = prefs.getInt(CARDS_STUDIED_TODAY_QUEST, 0)
                if (currentQuestCount < DAILY_QUEST_TARGET) {
                    val newQuestCount = currentQuestCount + 1
                    prefs.edit().putInt(CARDS_STUDIED_TODAY_QUEST, newQuestCount).apply()

                    if (newQuestCount == DAILY_QUEST_TARGET) {
                        processStreakLogic(context)
                    }
                }
            }
        } else {
            comboCounter = 0
            val currentMistakes = todayPrefs.getInt("mistakes_$cardHash", 0)
            todayPrefs.edit().putInt("mistakes_$cardHash", currentMistakes + 1).apply()
        }
        return Pair(awardedBaseXp, awardedComboBonus)
    }

    private fun processStreakLogic(context: Context) {
        val today = getTodayString()
        val prefs = context.getSharedPreferences(XP_PREFS, Context.MODE_PRIVATE)
        val lastStreakDate = prefs.getString(LAST_STREAK_DATE, "")
        
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)

        var streak = prefs.getInt(CURRENT_STREAK, 0)

        if (lastStreakDate == yesterday) {
            streak++
        } else if (lastStreakDate != today) {
            streak = 1
        }
        
        prefs.edit()
            .putInt(CURRENT_STREAK, streak)
            .putString(LAST_STREAK_DATE, today)
            .apply()

        // Sync streak to Firebase
        val nicknamePrefs = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        val nickname = nicknamePrefs.getString(NICKNAME_KEY, null)
        if (!nickname.isNullOrEmpty()) {
            FirebaseDatabase.getInstance().getReference("users/$nickname/streak").setValue(streak)
        }
        
        val streakBonus = when {
            streak == 1 -> 20
            streak == 2 -> 0
            streak == 3 -> 50
            streak == 5 -> 80
            streak == 10 -> 150
            streak % 30 == 0 -> 300
            streak == 365 -> 1000
            streak == 730 -> 2000
            else -> 10
        }
        
        if (streakBonus > 0) {
            awardXp(context, streakBonus)
        }
    }

    fun getLevel(xp: Int): Int {
        return floor(sqrt(xp / 20.0)).toInt() + 1
    }

    fun getXpForLevel(level: Int): Int {
        val base = level - 1
        return base * base * 20
    }
}

enum class MainTab { PROFILE, DECKS, SETTINGS, CHAT }

data class UserActivity(val learned: Long, val wrong: Long, val avatar: String = "img")

fun incrementWrongToday(context: Context) {
    val prefs = context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val count = if (prefs.getString("wrong_date", "") == today) prefs.getInt("wrong_value", 0) + 1 else 1
    prefs.edit().putString("wrong_date", today).putInt("wrong_value", count).apply()
    context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString(NICKNAME_KEY, null)?.let {
        val updates = mapOf<String, Any>(
            "wrong_today" to count,
            "last_active_date" to today
        )
        FirebaseDatabase.getInstance().getReference("users/$it").updateChildren(updates)
    }
}

// Функция настройки ежедневных будильников на 14:00 и 20:00
fun scheduleDailyAlarms(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarmForHour(hour: Int, requestCode: Int) {
        val intent = Intent(context, StudyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    setAlarmForHour(14, 14)
    setAlarmForHour(20, 20)
}

@Composable
fun AvatarImage(avatar: String, modifier: Modifier = Modifier, size: Dp = 24.dp) {
    val context = LocalContext.current
    val resId = remember(avatar) {
        context.resources.getIdentifier(avatar, "drawable", context.packageName)
    }
    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    } else {
        Text(
            text = if (avatar.isNotBlank()) avatar else "🐱",
            fontSize = (size.value * 0.7).sp
        )
    }
}

fun textToBitmap(text: String): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }
    val baseline = -paint.ascent()
    val width = (paint.measureText(text) + 20f).toInt().coerceAtLeast(1)
    val height = (baseline + paint.descent() + 20f).toInt().coerceAtLeast(1)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawText(text, width / 2f, baseline + 10f, paint)
    return bitmap
}

fun getAvatarBitmap(context: Context, avatar: String): Bitmap {
    val resId = context.resources.getIdentifier(avatar, "drawable", context.packageName)
    if (resId != 0) {
        val drawable = ContextCompat.getDrawable(context, resId)
        if (drawable != null) {
            val width = drawable.intrinsicWidth.coerceAtLeast(100)
            val height = drawable.intrinsicHeight.coerceAtLeast(100)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
    return textToBitmap(if (avatar.isNotBlank()) avatar else "img")
}

fun showAdminMessageNotification(
    context: Context,
    message: String,
    title: String = "CatCards",
    avatar: String = "img"
) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "admin_msg_v2"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId,
                "Сообщения", 
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val avatarBitmap = getAvatarBitmap(context, avatar)
    val userIcon = IconCompat.createWithBitmap(avatarBitmap)
    
    val senderPerson = Person.Builder()
        .setName(title)
        .setIcon(userIcon)
        .build()

    val messagingStyle = NotificationCompat.MessagingStyle(senderPerson)
        .addMessage(message, System.currentTimeMillis(), senderPerson)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_dialog_info)
        .setStyle(messagingStyle)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(
                    "admin_msg_v2",
                    "Сообщения от разработчика",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        // Запускаем будильники при старте
        scheduleDailyAlarms(this)
        
        // Уведомления доставляются через Firebase Cloud Messaging.
        setContent {
            val context = LocalContext.current
            
            // Читаем сохраненную тему из настроек, чтобы передать ее в тему приложения
            val prefs = context.getSharedPreferences(NICKNAME_PREFS, MODE_PRIVATE)
            var pinkTheme by remember {
                mutableStateOf(prefs.getBoolean("is_pink_theme", false))
            }

            // Запрос разрешения на уведомления для Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { _ -> }
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            MyApplicationTheme(pinkTheme = pinkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        var hasNickname by remember {
                            mutableStateOf(FirebaseAuth.getInstance().currentUser != null ||
                                !context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
                                    .getString(NICKNAME_KEY, null).isNullOrBlank())
                        }
                        // Keep the system Back button from finishing the activity on screens
                        // outside the study session, including registration.
                        BackHandler(enabled = !hasNickname) {
                            // Intentionally stay on the current screen.
                        }
                        if (!hasNickname) {
                            AuthScreen(
                                onAuthenticated = { uid, email, username, isSignUpAction ->
                                    if (isSignUpAction) {
                                        val shortId = String.format(Locale.US, "%04d", (0..9999).random())
                                        // При регистрации сохраняем и username, и email, и сгенерированный ID
                                        context.getSharedPreferences(NICKNAME_PREFS,
                                            MODE_PRIVATE
                                        )
                                            .edit()
                                            .putString(NICKNAME_KEY, username)
                                            .putString("email", email)
                                            .putString("short_id", shortId)
                                            .apply()
                                        
                                        FirebaseDatabase.getInstance().getReference("users/$username").apply {
                                            child("email").setValue(email)
                                            child("learned_today").setValue(0L)
                                            child("short_id").setValue(shortId)
                                        }
                                    } else {
                                        // При входе берем никнейм из базы, так как мы его не знаем
                                        FirebaseDatabase.getInstance().getReference("users").orderByChild("email").equalTo(email)
                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    val userNode = snapshot.children.firstOrNull()
                                                    val foundUsername = userNode?.key
                                                    val foundShortId = userNode?.child("short_id")?.getValue(String::class.java) ?: ""
                                                    if (foundUsername != null) {
                                                        context.getSharedPreferences(NICKNAME_PREFS,
                                                            MODE_PRIVATE
                                                        )
                                                            .edit()
                                                            .putString(NICKNAME_KEY, foundUsername)
                                                            .putString("email", email)
                                                            .putString("short_id", foundShortId)
                                                            .apply()
                                                    }
                                                }
                                                override fun onCancelled(error: DatabaseError) {}
                                            })
                                    }
                                    hasNickname = true
                                }
                            )
                        } else {
                            // Передаем OnThemeChange в FlashcardApp, чтобы она могла поменять тему
                            FlashcardApp(
                                pinkTheme = pinkTheme,
                                onThemeChange = { 
                                    pinkTheme = it 
                                    prefs.edit().putBoolean("is_pink_theme", it).apply()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthScreen(
    onAuthenticated: (String, String, String, Boolean) -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") } // Новое поле для имени пользователя
    var error by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetMessage by remember { mutableStateOf<String?>(null) }
    val hasEnoughLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasSpecialCharacter = password.any { !it.isLetterOrDigit() }
    val passwordScore = listOf(hasEnoughLength, hasUppercase, hasSpecialCharacter).count { it }
    val passwordProgress by animateFloatAsState(
        targetValue = passwordScore / 3f,
        animationSpec = tween(durationMillis = 300),
        label = "password-strength"
    )
    val passwordColor by animateColorAsState(
        targetValue = when (passwordScore) {
            3 -> Color(0xFF2E7D32)
            2 -> Color(0xFFF9A825)
            else -> MaterialTheme.colorScheme.error
        },
        animationSpec = tween(durationMillis = 300),
        label = "password-strength-color"
    )
    val passwordStatus = listOf(
        hasEnoughLength to "Не менее 8 символов",
        hasUppercase to "Заглавная буква",
        hasSpecialCharacter to "Специальный знак"
    )
    var isWaitingForVerification by remember { mutableStateOf(false) }

    // Автоматическая проверка статуса верификации, если мы ждем подтверждения
    LaunchedEffect(isWaitingForVerification) {
        if (isWaitingForVerification) {
            while (true) {
                delay(3000) // Проверяем каждые 3 секунды
                val user = auth.currentUser
                if (user != null) {
                    user.reload().addOnSuccessListener {
                        if (user.isEmailVerified) {
                            isWaitingForVerification = false
                            onAuthenticated(user.uid, user.email ?: "", username.trim(), isSignUp)
                        }
                    }
                }
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        
        Text(
            "Добро пожаловать в CatCards! \uD83D\uDC31", 
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            SegmentedButton(
                selected = !isSignUp,
                onClick = { isSignUp = false; error = null },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                modifier = Modifier.weight(1f)
            ) { Text("Вход") }
            SegmentedButton(
                selected = isSignUp,
                onClick = { isSignUp = true; error = null },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                modifier = Modifier.weight(1f)
            ) { Text("Регистрация") }
        }

        if (isSignUp) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            )
        }
        OutlinedTextField(email, { email = it }, label = { Text("Электронная почта") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            singleLine = true,
            visualTransformation = if (showPassword) androidx.compose.ui.text.input.VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        if (isSignUp) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp)) {
                LinearProgressIndicator(
                    progress = { passwordProgress },
                    color = passwordColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth().height(6.dp)
                )
                Text(
                    text = when (passwordScore) {
                        3 -> "Надежность пароля: высокая"
                        2 -> "Надежность пароля: средняя"
                        1 -> "Надежность пароля: низкая"
                        else -> "Надежность пароля: отсутствует"
                    },
                    color = passwordColor,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                passwordStatus.forEach { (passed, requirement) ->
                    Text(
                        text = "${if (passed) "✓" else "○"} $requirement",
                        color = if (passed) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }
        Button(enabled = !busy, onClick = {
            val normalizedEmail = email.trim()
            val normalizedUsername = username.trim()
            if (isSignUp && normalizedUsername.isBlank()) {
                error = "Пожалуйста, введите имя пользователя"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) {
                error = "Введите корректную электронную почту"
            } else if (isSignUp && passwordScore < 3) {
                error = "Пароль должен содержать 8 символов, заглавную букву и специальный знак"
            } else {
                busy = true
                error = null
                val task = if (isSignUp) auth.createUserWithEmailAndPassword(normalizedEmail, password)
                else auth.signInWithEmailAndPassword(normalizedEmail, password)
                
                task.addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        if (isSignUp) {
                            user.sendEmailVerification()
                            error = "Аккаунт создан! Мы отправили ссылку на $normalizedEmail. Приложение автоматически откроется после подтверждения."
                            isWaitingForVerification = true // Запускаем цикл проверки
                            // Убрали auth.signOut(), чтобы reload() работал и мы могли проверять статус
                        } else {
                            // Пытается войти
                            if (user.isEmailVerified) {
                                // Если это вход (не регистрация), передаем пустую строку, так как мы берем ник из БД
                                onAuthenticated(user.uid, normalizedEmail, "", isSignUp)
                            } else {
                                user.sendEmailVerification() // На всякий случай отправляем еще раз
                                error = "Email не подтвержден. Мы отправили ссылку на $normalizedEmail. Приложение автоматически откроется после подтверждения."
                                isWaitingForVerification = true
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    error = when {
                        exception.message?.contains("CONFIGURATION_NOT_FOUND", ignoreCase = true) == true -> 
                            "Firebase Authentication не настроена. Включите Email/Password в Firebase Console."
                        exception.message?.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) == true ||
                        exception.message?.contains("INVALID_PASSWORD", ignoreCase = true) == true ||
                        exception.message?.contains("USER_NOT_FOUND", ignoreCase = true) == true ||
                        exception.message?.contains("credential is incorrect", ignoreCase = true) == true -> 
                            "Неверный email или пароль"
                        exception.message?.contains("EMAIL_EXISTS", ignoreCase = true) == true ->
                            "Пользователь с такой почтой уже существует"
                        else -> 
                            exception.message ?: "Не удалось выполнить операцию"
                    }
                }.addOnCompleteListener { busy = false }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(if (busy) "Подождите…" else if (isSignUp) "Зарегистрироваться" else "Войти")
        }
        
        if (!isSignUp) {
            TextButton(onClick = { resetMessage = null; showResetDialog = true }) {
                Text("Забыли пароль?")
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { if (!busy) showResetDialog = false },
            title = { Text("Восстановление пароля") },
            text = {
                Column {
                    Text("Введите email. Мы отправим письмо со ссылкой для создания нового пароля.")
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; resetMessage = null },
                        label = { Text("Электронная почта") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    )
                    resetMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
                }
            },
            confirmButton = {
                TextButton(enabled = !busy, onClick = {
                    val normalizedEmail = email.trim()
                    if (!Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) {
                        resetMessage = "Введите корректную электронную почту"
                    } else {
                        busy = true
                        auth.setLanguageCode("ru")
                        auth.sendPasswordResetEmail(normalizedEmail)
                            .addOnSuccessListener {
                                resetMessage = "Письмо для восстановления отправлено"
                            }
                            .addOnFailureListener { exception ->
                                resetMessage = exception.message ?: "Не удалось отправить письмо"
                            }
                            .addOnCompleteListener { busy = false }
                    }
                }) { Text("Отправить") }
            },
            dismissButton = {
                TextButton(enabled = !busy, onClick = { showResetDialog = false }) { Text("Закрыть") }
            }
        )
    }
}

@Composable
fun GuestProfileScreen(
    userNickname: String,
    onBack: () -> Unit,
    onMessageClick: () -> Unit
) {
    BackHandler(enabled = true) {
        onBack()
    }

    var userDescription by remember { mutableStateOf("") }
    var userAvatar by remember { mutableStateOf("img") }
    var userShortId by remember { mutableStateOf("") }
    var userLearned by remember { mutableStateOf(0L) }
    var userWrong by remember { mutableStateOf(0L) }
    var userXp by remember { mutableStateOf(0) }
    var userStreak by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var showSendDeckDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userNickname) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userNickname")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userDescription = snapshot.child("description").getValue(String::class.java) ?: "Пользователь пока ничего не рассказал о себе."
                userAvatar = snapshot.child("avatar").getValue(String::class.java) ?: "🐱"
                userShortId = snapshot.child("short_id").getValue(String::class.java) ?: ""
                userXp = (snapshot.child("xp").value as? Number)?.toInt() ?: 0
                userStreak = (snapshot.child("streak").value as? Number)?.toInt() ?: 0
                
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val lastActive = snapshot.child("last_active_date").getValue(String::class.java)
                if (lastActive == today) {
                    userLearned = (snapshot.child("learned_today").value as? Number)?.toLong() ?: 0L
                    userWrong = (snapshot.child("wrong_today").value as? Number)?.toLong() ?: 0L
                }
                
                isLoading = false
            }
            override fun onCancelled(error: DatabaseError) {
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("←", fontSize = 24.sp)
            }
            Spacer(Modifier.width(8.dp))
            Text("Профиль пользователя", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AvatarImage(avatar = userAvatar, size = 96.dp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userNickname + if (userShortId.isNotBlank()) " #$userShortId" else "",
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold
                )
                if (userStreak > 0) {
                    Spacer(Modifier.width(8.dp))
                    Text("🔥 $userStreak", style = MaterialTheme.typography.titleMedium, color = Color(0xFFE65100))
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val currentLevel = XpSystem.getLevel(userXp)
            val nextLevel = currentLevel + 1
            val currentLevelXp = XpSystem.getXpForLevel(currentLevel)
            val nextLevelXp = XpSystem.getXpForLevel(nextLevel)
            val xpProgress = if (nextLevelXp > currentLevelXp) {
                (userXp - currentLevelXp).toFloat() / (nextLevelXp - currentLevelXp).toFloat()
            } else 1f
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Уровень $currentLevel", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$userXp / $nextLevelXp XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(userLearned.toString(), style = MaterialTheme.typography.headlineSmall, color = Color(0xFF2E7D32))
                    Text("Изучено за сутки", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(userWrong.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error)
                    Text("Ошибок за сутки", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("О себе", fontWeight = FontWeight.Bold)
                    Text(
                        text = userDescription,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onMessageClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Написать", maxLines = 1)
                }
                
                OutlinedButton(
                    onClick = { showSendDeckDialog = true },
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("Отправить колоду", maxLines = 1, fontSize = 12.sp)
                }
            }
        }
    }

    if (showSendDeckDialog) {
        SendDeckDialog(
            targetUserNickname = userNickname,
            onDismiss = { showSendDeckDialog = false }
        )
    }
}

@Composable
fun SendDeckDialog(
    targetUserNickname: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val myBlocks = remember { loadBlocksFromStorage(context) ?: emptyList() }
    var isSending by remember { mutableStateOf(false) }
    var sendStatusMessage by remember { mutableStateOf<String?>(null) }
    
    val csvPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isSending = true
            val name = getFileName(context, uri).removeSuffix(".csv")
            val cards = loadFlashcardsFromUri(context, uri)
            if (cards.isNotEmpty()) {
                sendDeckToUser(
                    context = context,
                    targetUserNickname = targetUserNickname,
                    deckName = name,
                    cards = cards,
                    onSuccess = {
                        isSending = false
                        Toast.makeText(context, "Колода «$name» отправлена $targetUserNickname!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    onFailure = { err ->
                        isSending = false
                        sendStatusMessage = err
                    }
                )
            } else {
                isSending = false
                sendStatusMessage = "Не удалось прочитать карточки из файла"
            }
        }
    }

    Dialog(onDismissRequest = { if (!isSending) onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Отправить колоду", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Выберите колоду для $targetUserNickname", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
                
                if (isSending) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    if (myBlocks.isEmpty()) {
                        Text("У вас пока нет сохраненных колод", color = Color.Gray, modifier = Modifier.padding(vertical = 12.dp))
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                            items(myBlocks) { block ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            isSending = true
                                            sendDeckToUser(
                                                context = context,
                                                targetUserNickname = targetUserNickname,
                                                deckName = block.name,
                                                cards = block.cards,
                                                onSuccess = {
                                                    isSending = false
                                                    Toast.makeText(context, "Колода «${block.name}» отправлена $targetUserNickname!", Toast.LENGTH_SHORT).show()
                                                    onDismiss()
                                                },
                                                onFailure = { err ->
                                                    isSending = false
                                                    sendStatusMessage = err
                                                }
                                            )
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(block.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text("${block.cards.size} шт.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedButton(
                        onClick = { csvPicker.launch("*/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Из файла (.csv)")
                    }
                }
                
                sendStatusMessage?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                TextButton(
                    onClick = onDismiss,
                    enabled = !isSending,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена")
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
    val nickname = prefs.getString(NICKNAME_KEY, "Пользователь") ?: "Пользователь"
    val shortId = prefs.getString("short_id", "") ?: ""
    
    var profileToView by remember { mutableStateOf<String?>(null) }
    var selectedChatUser by remember { mutableStateOf<String?>(null) }

    if (profileToView != null) {
        GuestProfileScreen(
            userNickname = profileToView!!,
            onBack = { profileToView = null },
            onMessageClick = {
                selectedChatUser = profileToView
                profileToView = null
            }
        )
    } else if (selectedChatUser != null) {
        ChatDetailScreen(
            recipient = selectedChatUser!!,
            onBack = { selectedChatUser = null },
            onViewProfile = { profileToView = selectedChatUser }
        )
    } else {
        var description by remember { mutableStateOf(prefs.getString("user_description", "") ?: "") }
    var isEditingDescription by remember { mutableStateOf(false) }
    
    var selectedAvatar by remember { mutableStateOf(prefs.getString("user_avatar", "img") ?: "img") }
    var showAvatarDialog by remember { mutableStateOf(false) }
    
    val availableAvatars = listOf("img", "img_1")
    
    val learnedToday = getLearnedToday(context)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val wrongToday = if (context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE).getString("wrong_date", "") == today) {
        context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE).getInt("wrong_value", 0)
    } else {
        0
    }

    var showFriendsDialog by remember { mutableStateOf(false) }
    var hasIncomingRequests by remember { mutableStateOf(false) }

    LaunchedEffect(nickname) {
        FirebaseDatabase.getInstance().getReference("friend_requests/$nickname")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hasIncomingRequests = snapshot.childrenCount > 0
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Профиль", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { showFriendsDialog = true }) {
                Text("Друзья")
                if (hasIncomingRequests) {
                    Box(modifier = Modifier.padding(start = 8.dp).size(8.dp).background(Color.Red, CircleShape))
                }
            }
        }
        
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                .clickable { showAvatarDialog = true },
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(avatar = selectedAvatar, size = 96.dp)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Изменить аватар",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val xpPrefs = context.getSharedPreferences("xp_prefs", Context.MODE_PRIVATE)
        val totalXp = xpPrefs.getInt("total_xp", 0)
        val streak = xpPrefs.getInt("current_streak", 0)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nickname + if (shortId.isNotBlank()) " #$shortId" else "",
                style = MaterialTheme.typography.titleLarge, 
                fontWeight = FontWeight.Bold
            )
            if (streak > 0) {
                Spacer(Modifier.width(8.dp))
                Text("🔥 $streak", style = MaterialTheme.typography.titleMedium, color = Color(0xFFE65100))
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        val currentLevel = XpSystem.getLevel(totalXp)
        val nextLevel = currentLevel + 1
        val currentLevelXp = XpSystem.getXpForLevel(currentLevel)
        val nextLevelXp = XpSystem.getXpForLevel(nextLevel)
        val xpProgress = if (nextLevelXp > currentLevelXp) {
            (totalXp - currentLevelXp).toFloat() / (nextLevelXp - currentLevelXp).toFloat()
        } else 1f
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Уровень $currentLevel", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { xpProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$totalXp / $nextLevelXp XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(learnedToday.toString(), style = MaterialTheme.typography.headlineSmall, color = Color(0xFF2E7D32))
                Text("Изучено за сутки", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(wrongToday.toString(), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error)
                Text("Ошибок за сутки", style = MaterialTheme.typography.bodySmall)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("О себе", fontWeight = FontWeight.Bold)
                    IconButton(onClick = { 
                        if (isEditingDescription) {
                            prefs.edit().putString("user_description", description).apply()
                            FirebaseDatabase.getInstance().getReference("users/$nickname/description").setValue(description)
                        }
                        isEditingDescription = !isEditingDescription 
                    }) {
                        Icon(
                            if (isEditingDescription) Icons.Filled.Check else Icons.Filled.Edit,
                            contentDescription = "Редактировать"
                        )
                    }
                }
                
                if (isEditingDescription) {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Напишите что-нибудь о себе...") },
                        maxLines = 5
                    )
                } else {
                    Text(
                        text = if (description.isNotBlank()) description else "Здесь пока ничего нет. Расскажите о себе!",
                        color = if (description.isNotBlank()) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
        }
    }
    
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Выберите аватарку") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(availableAvatars) { avatar ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    if (avatar == selectedAvatar) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) 
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    CircleShape
                                )
                                .clickable {
                                    selectedAvatar = avatar
                                    prefs.edit().putString("user_avatar", avatar).apply()
                                    FirebaseDatabase.getInstance().getReference("users/$nickname/avatar").setValue(avatar)
                                    showAvatarDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            AvatarImage(avatar = avatar, size = 52.dp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) { Text("Закрыть") }
            }
        )
    }

    if (showFriendsDialog) {
        FriendsDialog(
            currentNickname = nickname, 
            onDismiss = { showFriendsDialog = false },
            onFriendClick = { profileToView = it }
        )
    }
}
}

@Composable
fun FriendsDialog(
    currentNickname: String,
    onDismiss: () -> Unit,
    onFriendClick: (String) -> Unit = {}
) {
    var currentTab by remember { mutableStateOf(0) } // 0 = Мои, 1 = Поиск, 2 = Запросы
    var friendIdInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    
    var incomingRequests by remember { mutableStateOf<List<String>>(emptyList()) }
    var friendsList by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) } // nickname to avatar
    
    var searchResultNickname by remember { mutableStateOf<String?>(null) }
    var searchResultAvatar by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    // Поиск по 4-значному ID
    LaunchedEffect(friendIdInput) {
        if (friendIdInput.length == 4) {
            isSearching = true
            statusMessage = null
            searchResultNickname = null
            searchResultAvatar = null
            val db = FirebaseDatabase.getInstance()
            db.getReference("users").orderByChild("short_id").equalTo(friendIdInput)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        isSearching = false
                        val targetNode = snapshot.children.firstOrNull()
                        if (targetNode != null) {
                            val targetNickname = targetNode.key ?: ""
                            if (targetNickname.equals(currentNickname, ignoreCase = true)) {
                                statusMessage = "Это ваш собственный ID"
                            } else {
                                searchResultNickname = targetNickname
                                searchResultAvatar = targetNode.child("avatar").getValue(String::class.java) ?: "🐱"
                            }
                        } else {
                            statusMessage = "Пользователь не найден"
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        isSearching = false
                        statusMessage = "Ошибка: ${error.message}"
                    }
                })
        } else {
            searchResultNickname = null
            searchResultAvatar = null
        }
    }
    
    // Загрузка входящих запросов и списка друзей
    LaunchedEffect(currentNickname) {
        val db = FirebaseDatabase.getInstance()
        
        // Входящие запросы
        db.getReference("friend_requests/$currentNickname")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    incomingRequests = snapshot.children.mapNotNull { it.key }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            
        // Список друзей
        db.getReference("friends/$currentNickname")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friendIds = snapshot.children.mapNotNull { it.key }
                    if (friendIds.isEmpty()) {
                        friendsList = emptyList()
                        return
                    }
                    db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(usersSnap: DataSnapshot) {
                            friendsList = friendIds.mapNotNull { fId ->
                                val avatar = usersSnap.child(fId).child("avatar").getValue(String::class.java) ?: "🐱"
                                fId to avatar
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Друзья", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    SegmentedButton(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0; statusMessage = null },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                    ) { Text("Мои", fontSize = 12.sp) }
                    SegmentedButton(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1; statusMessage = null },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                    ) { Text("Поиск", fontSize = 12.sp) }
                    SegmentedButton(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2; statusMessage = null },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                    ) { Text("Запросы (${incomingRequests.size})", fontSize = 12.sp, maxLines = 1) }
                }
                
                when (currentTab) {
                    0 -> { // Мои друзья
                        if (friendsList.isEmpty()) {
                            Text(
                                "У вас пока нет друзей", 
                                color = Color.Gray, 
                                modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
                            )
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 240.dp)) {
                                items(friendsList) { (friendNick, avatar) ->
                                    var showRemoveDialog by remember { mutableStateOf(false) }
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f).clickable {
                                                onDismiss()
                                                onFriendClick(friendNick)
                                            },
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) { AvatarImage(avatar = avatar, size = 36.dp) }
                                            Spacer(Modifier.width(12.dp))
                                            Text(friendNick, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        }
                                        
                                        IconButton(onClick = { showRemoveDialog = true }) {
                                            Icon(Icons.Filled.Close, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                    
                                    if (showRemoveDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showRemoveDialog = false },
                                            title = { Text("Удалить из друзей?") },
                                            text = { Text("Вы уверены, что хотите удалить $friendNick из списка друзей?") },
                                            confirmButton = {
                                                TextButton(onClick = {
                                                    val db = FirebaseDatabase.getInstance()
                                                    db.getReference("friends/$currentNickname/$friendNick").removeValue()
                                                    db.getReference("friends/$friendNick/$currentNickname").removeValue()
                                                    showRemoveDialog = false
                                                }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { showRemoveDialog = false }) { Text("Отмена") }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    1 -> { // Поиск (Добавить)
                        OutlinedTextField(
                            value = friendIdInput,
                            onValueChange = { newValue -> 
                                if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                                    friendIdInput = newValue
                                    if (newValue.length < 4) statusMessage = null
                                }
                            },
                            label = { Text("ID друга (4 цифры)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (isSearching) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else if (searchResultNickname != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val target = searchResultNickname!!
                                        FirebaseDatabase.getInstance().getReference("friend_requests/$target/$currentNickname").setValue(true)
                                            .addOnSuccessListener {
                                                // Отправляем пуш-уведомление пользователю
                                                // Игнорируем ошибку, используем стандартную аватарку, 
                                                // так как внутри LaunchedEffect не всегда есть доступ к context
                                                val userAvatar = "🐱"
                                                val notificationData = mapOf(
                                                    "action" to "FRIEND_REQUEST",
                                                    "sender" to currentNickname,
                                                    "message" to "Хочет добавить вас в друзья!",
                                                    "avatarId" to userAvatar
                                                )
                                                FirebaseDatabase.getInstance()
                                                    .getReference("notificationRequests/$target")
                                                    .push()
                                                    .setValue(notificationData)
                                                    
                                                statusMessage = "Запрос отправлен!"
                                                friendIdInput = ""
                                                searchResultNickname = null
                                            }
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) { AvatarImage(avatar = searchResultAvatar ?: "img", size = 36.dp) }
                                        Spacer(Modifier.width(12.dp))
                                        Text(searchResultNickname!!, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    }
                                    Icon(Icons.Filled.PersonAdd, contentDescription = "Добавить", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        
                        statusMessage?.let {
                            Text(it, color = if (it.contains("отправлен")) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally))
                        }
                    }
                    2 -> { // Запросы
                        if (incomingRequests.isEmpty()) {
                            Text("Нет новых запросов", color = Color.Gray, modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally))
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(incomingRequests) { sender ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(sender, fontWeight = FontWeight.Bold)
                                        Row {
                                            IconButton(onClick = {
                                                val db = FirebaseDatabase.getInstance()
                                                db.getReference("friends/$currentNickname/$sender").setValue(true)
                                                db.getReference("friends/$sender/$currentNickname").setValue(true)
                                                db.getReference("friend_requests/$currentNickname/$sender").removeValue()
                                                
                                                // Уведомляем отправителя, что мы приняли его заявку
                                                val userAvatar = "🐱" // Аналогично, упрощаем до дефолтной аватарки в этом скоупе
                                                val notificationData = mapOf(
                                                    "action" to "FRIEND_ACCEPT",
                                                    "sender" to currentNickname,
                                                    "message" to "Принял вашу заявку в друзья!",
                                                    "avatarId" to userAvatar
                                                )
                                                db.getReference("notificationRequests/$sender")
                                                    .push()
                                                    .setValue(notificationData)
                                            }) {
                                                Icon(Icons.Filled.Check, contentDescription = "Принять", tint = Color(0xFF2E7D32))
                                            }
                                            IconButton(onClick = {
                                                FirebaseDatabase.getInstance().getReference("friend_requests/$currentNickname/$sender").removeValue()
                                            }) {
                                                Icon(Icons.Filled.Close, contentDescription = "Отклонить", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Закрыть")
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    pinkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var showDeveloperDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newNickname by remember { mutableStateOf("") }
    var renameError by remember { mutableStateOf<String?>(null) }
    var isRenaming by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val prefs = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
    // Используем состояние для мгновенного обновления UI после смены ника
    var currentNickname by remember { mutableStateOf(prefs.getString(NICKNAME_KEY, "") ?: "") }
        
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/zip")) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { createBackup(context, it) }
            Toast.makeText(context, "Резервная копия сохранена!", Toast.LENGTH_SHORT).show()
        }
    }
    
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            context.contentResolver.openInputStream(uri)?.use { restoreBackup(context, it) }
            Toast.makeText(context, "Данные восстановлены! Пожалуйста, перезапустите приложение.", Toast.LENGTH_LONG).show()
        }
    }
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Настройки", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))
        
        val currentEmail = prefs.getString("email", "") ?: ""
        if (currentEmail.isNotBlank()) {
            Text(
                text = "Email: $currentEmail",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ваш ник: ${if (currentNickname.isNotBlank()) currentNickname else "не указан"}",
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { 
                newNickname = currentNickname
                renameError = null
                showRenameDialog = true 
            }) {
                Text("Изменить")
            }
        }
        
        Text("Тема", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        
        var localThemeState by remember(pinkTheme) { mutableStateOf(pinkTheme) }
        
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            SegmentedButton(
                selected = !localThemeState,
                onClick = { 
                    localThemeState = false
                    onThemeChange(false) 
                },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                modifier = Modifier.weight(1f)
            ) { Text("BLUE") }
            SegmentedButton(
                selected = localThemeState,
                onClick = { 
                    localThemeState = true
                    onThemeChange(true) 
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                modifier = Modifier.weight(1f)
            ) { Text("ROSE") }
        }
        
        Text("Резервное копирование", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Text("Сохраните ваши колоды и переписки в файл .zip. При сохранении вы можете выбрать Google Диск, чтобы файл сохранился в вашем облаке.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Row(Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { 
                val date = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
                exportLauncher.launch("catcards_backup_$date.zip") 
            }, modifier = Modifier.weight(1f)) {
                Text("Экспорт")
            }
            OutlinedButton(onClick = { importLauncher.launch("application/zip") }, modifier = Modifier.weight(1f)) {
                Text("Импорт")
            }
        }
        
        Spacer(Modifier.weight(1f))
        
        Button(
            onClick = { showDeveloperDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Режим разработчика") }
    }
    
    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { if (!isRenaming) showRenameDialog = false },
            title = { Text("Сменить ник") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newNickname,
                        onValueChange = { newValue ->
                            if (newValue.all { it in 'a'..'z' || it in 'A'..'Z' || it in '0'..'9' || it == '_' }) {
                                newNickname = newValue
                                renameError = null 
                            }
                        },
                        label = { Text("Новый ник") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Используйте только латинские буквы, цифры и знак подчеркивания",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    renameError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(enabled = !isRenaming && newNickname.isNotBlank() && newNickname != currentNickname, onClick = {
                    isRenaming = true
                    val db = FirebaseDatabase.getInstance()
                    
                    // 1. Проверяем, свободен ли новый ник
                    db.getReference("users/$newNickname").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                renameError = "Этот ник уже занят!"
                                isRenaming = false
                            } else {
                                // 2. Копируем все данные со старого узла на новый
                                db.getReference("users/$currentNickname").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(oldDataSnapshot: DataSnapshot) {
                                        val data = oldDataSnapshot.value
                                        
                                        // 3. Записываем в новый узел и удаляем старый
                                        db.getReference("users/$newNickname").setValue(data).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                db.getReference("users/$currentNickname").removeValue()
                                                
                                                // Обновляем локальные настройки
                                                prefs.edit().putString(NICKNAME_KEY, newNickname).apply()
                                                currentNickname = newNickname
                                                showRenameDialog = false
                                                isRenaming = false
                                                Toast.makeText(context, "Ник успешно изменен", Toast.LENGTH_SHORT).show()
                                            } else {
                                                renameError = "Не удалось сохранить новый ник"
                                                isRenaming = false
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        renameError = "Ошибка базы данных: ${error.message}"
                                        isRenaming = false
                                    }
                                })
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            renameError = "Ошибка базы данных: ${error.message}"
                            isRenaming = false
                        }
                    })
                }) { Text(if (isRenaming) "Сохранение..." else "Сохранить") }
            },
            dismissButton = {
                TextButton(enabled = !isRenaming, onClick = { showRenameDialog = false }) { Text("Отмена") }
            }
        )
    }
    
    if (showDeveloperDialog) DeveloperDialog(onDismiss = { showDeveloperDialog = false })
}

data class ChatMessage(val text: String, val isFromMe: Boolean, val timestamp: Long = System.currentTimeMillis()) : Serializable

fun formatMessageTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}

fun saveChatHistory(context: Context, partner: String, messages: List<ChatMessage>) {
    try {
        val file = File(context.filesDir, "chat_${partner}.dat")
        ObjectOutputStream(file.outputStream()).use { it.writeObject(messages.toList()) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Suppress("UNCHECKED_CAST")
fun loadChatHistory(context: Context, partner: String): List<ChatMessage> {
    return try {
        val file = File(context.filesDir, "chat_${partner}.dat")
        if (file.exists()) {
            ObjectInputStream(file.inputStream()).use { it.readObject() as? List<ChatMessage> } ?: emptyList()
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

@Composable
fun ChatDetailScreen(
    recipient: String,
    onBack: () -> Unit,
    onViewProfile: () -> Unit
) {
    BackHandler(enabled = true) {
        onBack()
    }

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val context = LocalContext.current
    val currentNickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null)
        
    var recipientAvatar by remember { mutableStateOf("🐱") }

    LaunchedEffect(recipient) {
        FirebaseDatabase.getInstance().getReference("users/$recipient/avatar")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    recipientAvatar = snapshot.getValue(String::class.java) ?: "🐱"
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // Загрузка локальной истории и прослушивание новых входящих сообщений
    DisposableEffect(recipient, currentNickname) {
        messages.clear()
        messages.addAll(loadChatHistory(context, recipient))
        
        var listener: ChildEventListener? = null
        var inboxRef: DatabaseReference? = null
        
        if (currentNickname != null) {
            inboxRef = FirebaseDatabase.getInstance().getReference("chat_inbox/$currentNickname/$recipient")
            listener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val text = snapshot.child("text").getValue(String::class.java)
                    val ts = snapshot.child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()
                    if (text != null) {
                        messages.add(ChatMessage(text, isFromMe = false, timestamp = ts))
                        saveChatHistory(context, recipient, messages)
                        snapshot.ref.removeValue() // Удаляем из облака после получения
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }
            inboxRef.addChildEventListener(listener)
        }
        
        onDispose {
            if (inboxRef != null && listener != null) {
                inboxRef.removeEventListener(listener)
            }
        }
    }

    Column(Modifier.fillMaxSize().imePadding()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("←", fontSize = 24.sp)
            }
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.weight(1f).clickable { onViewProfile() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AvatarImage(avatar = recipientAvatar, size = 36.dp)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    recipient, 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        LazyColumn(
            Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (msg.text.startsWith("DECK:::")) {
                        val parts = msg.text.split(":::")
                        val deckName = parts.getOrNull(1) ?: "Колода"
                        val cardCount = parts.getOrNull(2) ?: "0"
                        var isDownloading by remember { mutableStateOf(false) }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (msg.isFromMe) MaterialTheme.colorScheme.primaryContainer 
                                                 else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎴", fontSize = 28.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(text = "Колода «$deckName»", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "$cardCount карточек", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                }
                                
                                if (!msg.isFromMe) {
                                    Spacer(Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            isDownloading = true
                                            downloadSpecificSharedDeck(
                                                context = context,
                                                deckName = deckName,
                                                onSuccess = { name ->
                                                    isDownloading = false
                                                    Toast.makeText(context, "Колода «$name» сохранена!", Toast.LENGTH_SHORT).show()
                                                },
                                                onFailure = { err ->
                                                    isDownloading = false
                                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        },
                                        enabled = !isDownloading,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (isDownloading) {
                                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                                        } else {
                                            Text("Скачать", fontSize = 12.sp)
                                        }
                                    }
                                }

                                Text(
                                    text = formatMessageTime(msg.timestamp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                                )
                            }
                        }
                    } else {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (msg.isFromMe) MaterialTheme.colorScheme.primaryContainer 
                                                 else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(msg.text)
                                Text(
                                    text = formatMessageTime(msg.timestamp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        var showSendDeckDialog by remember { mutableStateOf(false) }

        Column(
            Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { 
                        if (it.length <= 500) {
                            messageText = it 
                        }
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Сообщение...") },
                    maxLines = 4,
                    supportingText = {
                        Text(
                            text = "${messageText.length}/500",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
                Spacer(Modifier.width(8.dp))

                if (messageText.isBlank()) {
                    OutlinedButton(
                        onClick = { showSendDeckDialog = true },
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text("Колода", maxLines = 1, fontSize = 12.sp)
                    }
                    Spacer(Modifier.width(4.dp))
                }

                Button(
                    onClick = {
                        if (messageText.isNotBlank() && currentNickname != null) {
                            // 1. Сохраняем локально у себя
                            messages.add(ChatMessage(messageText, isFromMe = true))
                            saveChatHistory(context, recipient, messages)
                            
                            // 2. Отправляем собеседнику в его облачный "почтовый ящик"
                            val newMessage = mapOf(
                                "text" to messageText,
                                "timestamp" to System.currentTimeMillis()
                            )
                            FirebaseDatabase.getInstance().getReference("chat_inbox/$recipient/$currentNickname")
                                .push().setValue(newMessage)
                                
                            // Также отправляем как уведомление (существующая логика), 
                            // но добавим имя отправителя, чтобы было понятно от кого
                            val userAvatar = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString("user_avatar", "img") ?: "img"
                            val notificationData = mapOf(
                                "action" to "CHAT_MESSAGE",
                                "sender" to currentNickname,
                                "message" to messageText,
                                "avatarId" to userAvatar
                            )
                            FirebaseDatabase.getInstance().getReference("notificationRequests/$recipient")
                                .push().setValue(notificationData)
                                
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank(),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Text(">", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        if (showSendDeckDialog) {
            SendDeckDialog(
                targetUserNickname = recipient,
                onDismiss = { showSendDeckDialog = false }
            )
        }
    }
}

@Composable
fun ChatScreen() {
    var users by remember { mutableStateOf<Map<String, UserActivity>>(emptyMap()) }
    var usersError by remember { mutableStateOf<String?>(null) }
    var selectedChatUser by remember { mutableStateOf<String?>(null) }
    var profileToView by remember { mutableStateOf<String?>(null) }
    var inboxSnapshot by remember { mutableStateOf<DataSnapshot?>(null) }
    
    BackHandler(enabled = profileToView != null) {
        profileToView = null
    }

    BackHandler(enabled = profileToView == null && selectedChatUser != null) {
        selectedChatUser = null
    }
    
    val context = LocalContext.current
    val currentNickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null)
        
    DisposableEffect(currentNickname) {
        var inboxRef: DatabaseReference? = null
        var listener: ValueEventListener? = null
        if (!currentNickname.isNullOrBlank()) {
            inboxRef = FirebaseDatabase.getInstance().getReference("chat_inbox/$currentNickname")
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    inboxSnapshot = snapshot
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            inboxRef.addValueEventListener(listener!!)
        }
        onDispose {
            if (inboxRef != null && listener != null) {
                inboxRef!!.removeEventListener(listener!!)
            }
        }
    }
        
    LaunchedEffect(currentNickname) {
        if (currentNickname == null) return@LaunchedEffect
        usersError = null
        val db = FirebaseDatabase.getInstance()
        
        // Сначала получаем список друзей
        db.getReference("friends/$currentNickname").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(friendsSnapshot: DataSnapshot) {
                val friendIds = friendsSnapshot.children.mapNotNull { it.key }
                if (friendIds.isEmpty()) {
                    users = emptyMap()
                    return
                }
                
                // Затем загружаем информацию только о друзьях
                db.getReference("users").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                        val loadedUsers = mutableMapOf<String, UserActivity>()
                        for (friendId in friendIds) {
                            val userSnap = usersSnapshot.child(friendId)
                            if (userSnap.exists()) {
                                val lastActive = userSnap.child("last_active_date").value as? String
                                val isToday = lastActive == today
                                val avatar = userSnap.child("avatar").getValue(String::class.java) ?: "🐱"
                                loadedUsers[friendId] = UserActivity(
                                    if (isToday) (userSnap.child("learned_today").value as? Number)?.toLong().orZero() else 0L,
                                    if (isToday) (userSnap.child("wrong_today").value as? Number)?.toLong().orZero() else 0L,
                                    avatar
                                )
                            }
                        }
                        users = loadedUsers
                    }

                    override fun onCancelled(error: DatabaseError) {
                        usersError = error.message
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                usersError = error.message
            }
        })
    }

    if (profileToView != null) {
        GuestProfileScreen(
            userNickname = profileToView!!,
            onBack = { profileToView = null },
            onMessageClick = {
                selectedChatUser = profileToView
                profileToView = null
            }
        )
    } else if (selectedChatUser != null) {
        ChatDetailScreen(
            recipient = selectedChatUser!!,
            onBack = { selectedChatUser = null },
            onViewProfile = { profileToView = selectedChatUser }
        )
    } else {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Чат", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
            
            usersError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            
            if (users.isEmpty()) {
                Text(
                    "У вас пока нет друзей в чате. Отправьте запрос из своего профиля, чтобы начать общаться!",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(users.toList()) { (user, activity) ->
                        val friendInbox = inboxSnapshot?.child(user)
                        val unreadCount = friendInbox?.childrenCount?.toInt() ?: 0

                        val unreadLastTs = friendInbox?.children?.mapNotNull { it.child("timestamp").getValue(Long::class.java) }?.maxOrNull()
                        val localLastTs = remember(user, inboxSnapshot) { loadChatHistory(context, user).lastOrNull()?.timestamp }
                        val displayTime = listOfNotNull(unreadLastTs, localLastTs).maxOrNull()

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedChatUser = user },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AvatarImage(avatar = activity.avatar, size = 36.dp)
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Text(user, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    if (displayTime != null && displayTime > 0) {
                                        Text(
                                            text = formatMessageTime(displayTime),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                    if (unreadCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                                .padding(horizontal = 8.dp, vertical = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = unreadCount.toString(),
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeveloperDialog(onDismiss: () -> Unit) {
    var users by remember { mutableStateOf<Map<String, UserActivity>>(emptyMap()) }
    var selectedUser by remember { mutableStateOf<String?>(null) }
    var userToDelete by remember { mutableStateOf<String?>(null) }
    var deletingUser by remember { mutableStateOf<String?>(null) }
    var uploadMessage by remember { mutableStateOf<String?>(null) }
    var usersError by remember { mutableStateOf<String?>(null) }
    var errorReports by remember { mutableStateOf<List<String>>(emptyList()) }
    var errorReportsMessage by remember { mutableStateOf<String?>(null) }
    var reportToSave by remember { mutableStateOf<Pair<String, ByteArray>?>(null) }
    val context = LocalContext.current
    val currentNickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null)
    val saveReportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        val report = reportToSave
        reportToSave = null
        if (uri != null && report != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { it.write(report.second) }
            }.onFailure { errorReportsMessage = "Не удалось сохранить файл: ${it.message}" }
        }
    }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        val user = selectedUser ?: return@rememberLauncherForActivityResult
        val csvUris = uris.filter { getFileName(context, it).endsWith(".csv", true) }
        if (csvUris.isEmpty()) {
            uploadMessage = "Выберите хотя бы одну CSV-колоду"
            return@rememberLauncherForActivityResult
        }
        var completed = 0
        var uploaded = 0
        csvUris.forEach { uri ->
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            if (bytes == null) {
                completed++
                uploadMessage = "Не удалось прочитать одну из выбранных колод"
                return@forEach
            }
            FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
                    .reference
                    .child("decks/$user/${getFileName(context, uri)}")
                    .putBytes(bytes)
                    .addOnSuccessListener {
                        uploaded++
                        FirebaseDatabase.getInstance()
                            .getReference("users/$user/available_decks_count")
                            .runTransaction(object : Transaction.Handler {
                                override fun doTransaction(currentData: MutableData): Transaction.Result {
                                    val count = currentData.getValue(Long::class.java) ?: 0L
                                    currentData.value = count + 1L
                                    return Transaction.success(currentData)
                                }

                                override fun onComplete(
                                    error: DatabaseError?,
                                    committed: Boolean,
                                    currentData: DataSnapshot?
                                ) = Unit
                            })
                    }
                    .addOnFailureListener { error ->
                        uploadMessage = "Ошибка отправки: ${error.message ?: error.javaClass.simpleName}"
                    }
                    .addOnCompleteListener {
                        completed++
                        if (completed == csvUris.size) {
                            if (uploaded > 0) {
                                val notificationData = mapOf(
                                    "action" to "DEVELOPER_MESSAGE",
                                    "sender" to "Разработчик",
                                    "message" to "Отправил вам $uploaded ${if (uploaded == 1) "новую колоду" else "новые колоды"}. Откройте «Из облака», чтобы скачать их.",
                                    "avatarId" to "🧑‍💻"
                                )
                                FirebaseDatabase.getInstance()
                                    .getReference("notificationRequests/$user")
                                    .push()
                                    .setValue(notificationData)
                            }
                            uploadMessage = "Отправлено колод: $uploaded из ${csvUris.size}"
                        }
                    }
        }
    }
    val loadUsers = {
        usersError = null
        // Экран разработчика запрашивает актуальный список только при открытии.
        FirebaseDatabase.getInstance().getReference("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                    users = snapshot.children
                        .filter { it.key != currentNickname }
                        .associate {
                            val lastActive = it.child("last_active_date").value as? String
                            val isToday = lastActive == today
                            val avatar = it.child("avatar").getValue(String::class.java) ?: "🐱"
                            it.key.orEmpty() to UserActivity(
                                if (isToday) (it.child("learned_today").value as? Number)?.toLong().orZero() else 0L,
                                if (isToday) (it.child("wrong_today").value as? Number)?.toLong().orZero() else 0L,
                                avatar
                            )
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    usersError = error.message
                }
            })
    }

    LaunchedEffect(Unit) {
        loadUsers()
    }

    LaunchedEffect(selectedUser) {
        errorReports = emptyList()
        errorReportsMessage = null
        val user = selectedUser
        if (!user.isNullOrBlank()) {
            FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
                .reference.child("error_reports/$user")
                .listAll()
                .addOnSuccessListener { result ->
                    errorReports = result.items.map { it.name }.sortedDescending()
                }
                .addOnFailureListener { error ->
                    errorReportsMessage = "Не удалось загрузить список ошибок: ${error.message}"
                }
        }
    }
    
    // Вложенный диалог для подтверждения удаления
    if (userToDelete != null) {
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text("Удалить пользователя?") },
            text = { Text("Вы действительно хотите удалить пользователя $userToDelete и все его данные из БД?") },
            confirmButton = {
                TextButton(onClick = {
                    val user = userToDelete!!
                    deletingUser = user
                    FirebaseDatabase.getInstance().getReference("users/$user").removeValue()
                        .addOnSuccessListener {
                            users = users - user
                            userToDelete = null
                        }
                        .addOnFailureListener { error ->
                            usersError = "Не удалось удалить пользователя: ${error.message ?: "ошибка Firebase"}"
                        }
                        .addOnCompleteListener { deletingUser = null }
                }, enabled = deletingUser == null) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) { Text("Отмена") }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Режим разработчика") },
        text = {
            Column {
                usersError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = selectedUser?.let { "Получатель: $it" } ?: "Выберите пользователя",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.height(240.dp)) {
                    items(users.toList()) { (user, activity) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { selectedUser = user },
                                        onLongPress = { userToDelete = user }
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedUser == user) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(user, fontWeight = FontWeight.Bold)
                                    Text("Правильных ответов: ${activity.learned}")
                                    Text("Неправильных ответов: ${activity.wrong}")
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { picker.launch("text/*") },
                    enabled = selectedUser != null,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    Text("Выбрать колоды и отправить")
                }

                uploadMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
                if (selectedUser != null) {
                    Text(
                        "Файлы ошибок",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    errorReportsMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    if (errorReports.isEmpty()) {
                        Text("Нет доступных файлов")
                    } else {
                        errorReports.forEach { fileName ->
                            TextButton(
                                onClick = {
                                    FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
                                        .reference.child("error_reports/$selectedUser/$fileName")
                                        .getBytes(ERROR_REPORT_MAX_BYTES)
                                        .addOnSuccessListener { bytes ->
                                            reportToSave = fileName to bytes
                                            saveReportLauncher.launch(fileName)
                                        }
                                        .addOnFailureListener {
                                            errorReportsMessage = "Не удалось скачать файл: ${it.message}"
                                        }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Скачать $fileName")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) { Text("Закрыть") }
        }
    )
}

private fun Long?.orZero(): Long = this ?: 0L

@Composable
fun ComboAnimationOverlay(amount: Int, onDismiss: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(amount) {
        visible = true
        delay(1500)
        visible = false
        delay(300) // wait for exit animation
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut(tween(300)) + slideOutVertically(targetOffsetY = { -it }),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp)
        ) {
            Text(
                text = "+$amount XP",
                color = Color(0xFFFFB300),
                style = MaterialTheme.typography.headlineMedium, // Сделали текст поменьше
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
                // Убрали shadowElevation, чтобы точно не было никаких артефактов "фона"
            )
        }
    }
}

@Composable
fun FlashcardApp(
    pinkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val blocks = remember {
        val savedBlocks = loadBlocksFromStorage(context)
        if (savedBlocks != null) {
            mutableStateListOf(*savedBlocks.toTypedArray())
        } else {
            val initial = emptyList<FlashcardBlock>()
            saveBlocksToStorage(context, initial)
            mutableStateListOf(*initial.toTypedArray())
        }
    }

    var activeSession by rememberSaveable { mutableStateOf<StudySession?>(null) }
    var pausedSession by rememberSaveable { mutableStateOf<StudySession?>(null) }
    var blockForDialog by rememberSaveable { mutableStateOf<FlashcardBlock?>(null) }
    var isReverseMode by rememberSaveable { mutableStateOf(true) }
    var availableCloudDecks by remember { mutableStateOf(0) }
    var currentTab by rememberSaveable { mutableStateOf(MainTab.DECKS) }
    var comboBonusToAnimate by remember { mutableStateOf<Int?>(null) }
    var hasUnreadChatMessages by remember { mutableStateOf(false) }
    
    val xpPrefs = context.getSharedPreferences("xp_prefs", Context.MODE_PRIVATE)
    var currentSessionXp by remember { mutableStateOf(xpPrefs.getInt("total_xp", 0)) }

    // После переустановки восстанавливаем колоды из базы по авторизованному аккаунту.
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (!uid.isNullOrBlank()) {
            FirebaseDatabase.getInstance().getReference("users/$uid/flashcards")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val cloudBlocks = snapshot.children.mapNotNull { blockSnapshot ->
                            val name = blockSnapshot.child("name").getValue(String::class.java) ?: return@mapNotNull null
                            val cards = blockSnapshot.child("cards").children.mapNotNull { cardSnapshot ->
                                val front = cardSnapshot.child("front").getValue(String::class.java) ?: return@mapNotNull null
                                val back = cardSnapshot.child("back").getValue(String::class.java) ?: return@mapNotNull null
                                Flashcard(front, back, cardSnapshot.child("isKnown").getValue(Boolean::class.java))
                            }
                            FlashcardBlock(name, cards)
                        }
                        if (cloudBlocks.isNotEmpty() || snapshot.exists()) {
                            blocks.clear()
                            blocks.addAll(cloudBlocks)
                            saveBlocksToStorage(context, blocks, syncCloud = false)
                        } else if (blocks.isNotEmpty()) {
                            saveBlocksToStorage(context, blocks)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) = Unit
                })
        }
    }
    
    // Получаем текущую тему и функцию ее изменения из контекста, 
    // чтобы передать их в BottomTools, который теперь будет жить здесь
    val prefs = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
    var pinkTheme by remember {
        mutableStateOf(prefs.getBoolean("is_pink_theme", false))
    }

    // Слушатель уведомлений от разработчика
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString(NICKNAME_KEY, null)
    LaunchedEffect(nickname) {
        if (!nickname.isNullOrBlank()) {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token: String ->
                    FirebaseDatabase.getInstance()
                        .getReference("users/$nickname/fcmToken")
                        .setValue(token)
                        .addOnFailureListener { error ->
                            Log.e("FCM", "Не удалось сохранить FCM-токен", error)
                        }
                }
                .addOnFailureListener { error ->
                    Log.e("FCM", "Не удалось получить FCM-токен", error)
                }
        }
    }
    DisposableEffect(nickname) {
        var decksRef: com.google.firebase.database.DatabaseReference? = null
        var listener: ValueEventListener? = null
        if (!nickname.isNullOrBlank()) {
            decksRef = FirebaseDatabase.getInstance()
                .getReference("users/$nickname/available_decks_count")
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    availableCloudDecks = snapshot.getValue(Long::class.java)?.toInt()?.coerceAtLeast(0) ?: 0
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            decksRef.addValueEventListener(listener!!)
        }
        onDispose {
            if (decksRef != null && listener != null) {
                decksRef!!.removeEventListener(listener!!)
            }
        }
    }

    DisposableEffect(nickname) {
        var inboxRef: DatabaseReference? = null
        var listener: ValueEventListener? = null
        if (!nickname.isNullOrBlank()) {
            inboxRef = FirebaseDatabase.getInstance().getReference("chat_inbox/$nickname")
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalUnread = 0L
                    for (senderSnap in snapshot.children) {
                        totalUnread += senderSnap.childrenCount
                    }
                    hasUnreadChatMessages = totalUnread > 0
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            inboxRef.addValueEventListener(listener!!)
        }
        onDispose {
            if (inboxRef != null && listener != null) {
                inboxRef!!.removeEventListener(listener!!)
            }
        }
    }
    val closeActiveSession = {
        val session = activeSession
        session?.takeIf { it.isErrorsOnly }?.let { errorSession ->
            errorSession.deferredErrorCards = errorSession.cards.filter { it.isKnown == null }
            errorSession.deferredErrorCards.forEach { it.isKnown = false }
        }
        if (session != null && session.hasUnfinishedContent()) {
            pausedSession = session
        }
        saveBlocksToStorage(context, blocks)
        activeSession = null
    }

    var showExitConfirmationDialog by remember { mutableStateOf(false) }

    // 1. Перехватываем системную кнопку "Назад" на главных вкладках и переходим на вкладку "Колоды"
    BackHandler(enabled = activeSession == null) {
        if (currentTab != MainTab.DECKS) {
            currentTab = MainTab.DECKS
        }
    }

    // 2. В режиме обучения перехватываем системную кнопку "Назад" и вызываем диалог подтверждения
    BackHandler(enabled = activeSession != null) {
        showExitConfirmationDialog = true
    }

    val requestExitSession = {
        showExitConfirmationDialog = true
    }

    if (showExitConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmationDialog = false },
            title = { Text("Выйти из тренировки?") },
            text = { Text("Ваш текущий прогресс будет сохранен. Вы сможете продолжить обучение позже.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirmationDialog = false
                    closeActiveSession()
                }) {
                    Text("Выйти", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmationDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = getFileName(context, uri)
            if (name.endsWith(".csv", ignoreCase = true)) {
                val cards = loadFlashcardsFromUri(context, uri)
                if (cards.isNotEmpty()) {
                    val displayName = name.replace(".csv", "", ignoreCase = true)
                    blocks.add(FlashcardBlock(displayName, cards))
                    saveBlocksToStorage(context, blocks)
                }
            }
        }
    }

    if (activeSession == null) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentTab == MainTab.PROFILE,
                        onClick = { currentTab = MainTab.PROFILE },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Профиль") },
                        label = { Text("Профиль") }
                    )
                    NavigationBarItem(
                        selected = currentTab == MainTab.DECKS,
                        onClick = { currentTab = MainTab.DECKS },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Колоды") },
                        label = { Text("Колоды") }
                    )
                    NavigationBarItem(
                        selected = currentTab == MainTab.SETTINGS,
                        onClick = { currentTab = MainTab.SETTINGS },
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Настройки") },
                        label = { Text("Настройки") }
                    )
                    NavigationBarItem(
                        selected = currentTab == MainTab.CHAT,
                        onClick = { currentTab = MainTab.CHAT },
                        icon = {
                            Box {
                                Icon(Icons.Filled.Email, contentDescription = "Чат")
                                if (hasUnreadChatMessages) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.Red, CircleShape)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }
                        },
                        label = { Text("Чат") }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = modifier.padding(paddingValues).fillMaxSize()) {
                when (currentTab) {
                    MainTab.PROFILE -> ProfileScreen()
                    MainTab.DECKS -> {
                        BlockSelectionScreen(
                            blocks = blocks,
                            isReverseMode = isReverseMode,
                            onModeChange = { isReverseMode = it },
                            onBlockSelected = { block -> blockForDialog = block },
                            onImportClick = { filePickerLauncher.launch("*/*") },
                            onDownloadClick = { downloadSharedDecks(context, blocks) },
                            availableCloudDecks = availableCloudDecks,
                            onDeleteBlock = { block ->
                                val index = blocks.indexOfFirst { it === block }.takeIf { it >= 0 } ?: blocks.indexOf(block)
                                if (index >= 0) blocks.removeAt(index)
                                saveBlocksToStorage(context, blocks)
                            },
                            onRenameBlock = { block, newName ->
                                val index = blocks.indexOf(block)
                                if (index != -1) {
                                    blocks[index] = block.copy(name = newName)
                                    saveBlocksToStorage(context, blocks)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    MainTab.SETTINGS -> {
                        SettingsScreen(
                            pinkTheme = pinkTheme,
                            onThemeChange = onThemeChange
                        )
                    }
                    MainTab.CHAT -> ChatScreen()
                }
            }
        }
    } else {
        if (activeSession!!.mode == StudyMode.SWIPE) {
            FlashcardScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = requestExitSession,
                onExportErrors = { cards, title, onSent -> uploadErrorReport(context, title, cards, onSent) },
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                    if (correct != null && card != null) {
                        val (_, comboAwarded) = XpSystem.processCardStudiedForXp(context, card.front, "SWIPE", correct)
                        currentSessionXp = xpPrefs.getInt("total_xp", 0)
                        if (comboAwarded > 0) comboBonusToAnimate = comboAwarded
                    }
                },
                currentXp = currentSessionXp,
                modifier = modifier
            )
        } else if (activeSession!!.mode == StudyMode.TILES) {
            TilesScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = requestExitSession,
                onExportErrors = { cards, title, onSent -> uploadErrorReport(context, title, cards, onSent) },
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                    if (correct != null && card != null) {
                        val (_, comboAwarded) = XpSystem.processCardStudiedForXp(context, card.front, "TILES", correct)
                        currentSessionXp = xpPrefs.getInt("total_xp", 0)
                        if (comboAwarded > 0) comboBonusToAnimate = comboAwarded
                    }
                },
                currentXp = currentSessionXp,
                modifier = modifier
            )
        } else {
            TypingScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = requestExitSession,
                onExportErrors = { cards, title, onSent -> uploadErrorReport(context, title, cards, onSent) },
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                    if (correct != null && card != null) {
                        val (_, comboAwarded) = XpSystem.processCardStudiedForXp(context, card.front, "INPUT", correct)
                        currentSessionXp = xpPrefs.getInt("total_xp", 0)
                        if (comboAwarded > 0) comboBonusToAnimate = comboAwarded
                    }
                },
                currentXp = currentSessionXp,
                modifier = modifier
            )
        }
    }

    if (blockForDialog != null) {
        val latestBlock = blocks.find { it.cards == blockForDialog!!.cards } ?: blockForDialog!!
        val mistakesCount = latestBlock.cards.count { it.isKnown == false }
        val pausedForBlock = pausedSession?.takeIf {
            it.sourceBlockName == latestBlock.name && it.hasUnfinishedContent()
        }
        var selectedMode by remember { mutableStateOf(StudyMode.SWIPE) }

        Dialog(onDismissRequest = { blockForDialog = null }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = latestBlock.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                        val options = listOf("Свайпы", "Плиточки", "Ввод")
                        options.forEachIndexed { index, label ->
                            val mode = when (index) {
                                0 -> StudyMode.SWIPE
                                1 -> StudyMode.TILES
                                else -> StudyMode.INPUT
                            }
                            SegmentedButton(
                                selected = selectedMode == mode,
                                onClick = { selectedMode = mode },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                                modifier = Modifier.weight(1f)
                            ) { Text(text = label, maxLines = 1, fontSize = 12.sp) }
                        }
                    }

                    if (pausedForBlock != null) {
                        Button(
                            onClick = {
                                pausedForBlock.deferredErrorCards.forEach { it.isKnown = null }
                                pausedForBlock.deferredErrorCards = emptyList()
                                
                                // Конвертируем прогресс, если поменялся режим игры (Свайпы <-> Плиточки)
                                val newPosition = if (pausedForBlock.mode == selectedMode) {
                                    pausedForBlock.currentPosition
                                } else if (selectedMode == StudyMode.TILES) {
                                    pausedForBlock.currentPosition / 4 // 4 карточки = 1 группа плиточек
                                } else if (pausedForBlock.mode == StudyMode.TILES) {
                                    pausedForBlock.currentPosition * 4
                                } else {
                                    pausedForBlock.currentPosition
                                }
                                
                                activeSession = pausedForBlock.copy(
                                    mode = selectedMode, 
                                    currentPosition = newPosition
                                )
                                pausedSession = null
                                blockForDialog = null
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) { Text("Продолжить (${pausedForBlock.progressLabel()})") }
                    }

                    Button(
                        onClick = {
                            latestBlock.cards.forEach { it.isKnown = null }
                            saveBlocksToStorage(context, blocks)
                            pausedSession = null
                            activeSession = StudySession(latestBlock.name, latestBlock.cards.shuffled(), selectedMode)
                            blockForDialog = null
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) { Text("Всю колоду с начала") }

                    if (mistakesCount > 0) {
                        Button(
                            onClick = {
                                val mistakes = latestBlock.cards.filter { it.isKnown == false }.shuffled()
                                mistakes.forEach { it.isKnown = null }
                                saveBlocksToStorage(context, blocks)
                                pausedSession = null
                                activeSession = StudySession(
                                    title = "${latestBlock.name} (Ошибки)",
                                    cards = mistakes,
                                    mode = selectedMode,
                                    isErrorsOnly = true,
                                    sourceBlockName = latestBlock.name
                                )
                                blockForDialog = null
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) { Text("Только ошибки ($mistakesCount)") }
                    }

                    TextButton(
                        onClick = { blockForDialog = null },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Отмена", color = Color.Gray) }
                }
            }
        }
    }
    
    comboBonusToAnimate?.let { amount ->
        ComboAnimationOverlay(
            amount = amount,
            onDismiss = { comboBonusToAnimate = null }
        )
    }
}

@Composable
fun BlockSelectionScreen(
    blocks: List<FlashcardBlock>,
    isReverseMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    onBlockSelected: (FlashcardBlock) -> Unit,
    onImportClick: () -> Unit,
    onDownloadClick: () -> Unit,
    availableCloudDecks: Int,   
    onDeleteBlock: (FlashcardBlock) -> Unit,
    onRenameBlock: (FlashcardBlock, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val learnedToday = getLearnedToday(context)
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    var blockForAction by remember { mutableStateOf<FlashcardBlock?>(null) }
    var blockToDelete by remember { mutableStateOf<FlashcardBlock?>(null) }
    var blockToRename by remember { mutableStateOf<FlashcardBlock?>(null) }
    var newBlockName by remember { mutableStateOf("") }

    val headerControls = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onImportClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Импорт (.csv)", textAlign = TextAlign.Center, maxLines = 1, fontSize = 12.sp)
                }

                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = onDownloadClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Из облака", textAlign = TextAlign.Center, maxLines = 1, fontSize = 12.sp)
                    }
                    if (availableCloudDecks > 0) {
                        Text(
                            text = availableCloudDecks.toString(),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(MaterialTheme.colorScheme.error, MaterialTheme.shapes.small)
                                .padding(horizontal = 7.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "Выберите колоду",
                style = if (isLandscape) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "/ᐠ - ˕ -マ",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Рус -> Англ",
                    color = if (isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = if (isReverseMode) FontWeight.Bold else FontWeight.Normal
                )
                Switch(
                    checked = !isReverseMode,
                    onCheckedChange = { onModeChange(!it) },
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "Англ -> Рус",
                    color = if (!isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = if (!isReverseMode) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

    val footerStats = @Composable {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Выучено за сегодня: $learnedToday",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp, bottom = 2.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Created by scwls",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
            )
        }
    }

    val listContent = @Composable { listModifier: Modifier ->
        LazyColumn(
            modifier = listModifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = blocks,
                key = { block -> System.identityHashCode(block) }
            ) { block ->
                val correctCount = block.cards.count { it.isKnown == true }
                val wrongCount = block.cards.count { it.isKnown == false }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onBlockSelected(block) },
                                onLongPress = { blockForAction = block }
                            )
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(88.dp).padding(16.dp)
                    ) {
                        Text(
                            text = block.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterStart).padding(end = 72.dp)
                        )
                        Text(
                            text = "${block.cards.size} карточек",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                        Text(
                            text = "Верно: $correctCount · Ошибки: $wrongCount",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }
            }
        }
    }

    if (isLandscape) {
        Row(
            modifier = modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.45f).fillMaxHeight().verticalScroll(rememberScrollState()).padding(end = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                headerControls()
                Spacer(modifier = Modifier.weight(1f))
                footerStats()
            }
            listContent(Modifier.weight(0.55f).fillMaxHeight())
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            headerControls()
            listContent(Modifier.weight(1f))
            footerStats()
        }
    }
        
        if (blockForAction != null) {
            Dialog(onDismissRequest = { blockForAction = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = blockForAction!!.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                blockToRename = blockForAction
                                newBlockName = blockForAction!!.name
                                blockForAction = null
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) { Text("Переименовать") }
                        Button(
                            onClick = {
                                blockToDelete = blockForAction
                                blockForAction = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) { Text("Удалить") }
                        TextButton(
                            onClick = { blockForAction = null },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Отмена", color = Color.Gray) }
                    }
                }
            }
        }

        if (blockToRename != null) {
            AlertDialog(
                onDismissRequest = { blockToRename = null },
                title = { Text("Переименовать колоду") },
                text = {
                    OutlinedTextField(
                        value = newBlockName,
                        onValueChange = { newBlockName = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newBlockName.isNotBlank()) onRenameBlock(blockToRename!!, newBlockName)
                        blockToRename = null
                    }) { Text("Сохранить") }
                },
                dismissButton = {
                    TextButton(onClick = { blockToRename = null }) { Text("Отмена") }
                }
            )
        }

        if (blockToDelete != null) {
            AlertDialog(
                onDismissRequest = { blockToDelete = null },
                title = { Text("Удалить колоду?") },
                text = { Text("Вы действительно хотите удалить колоду «${blockToDelete!!.name}»? Это действие нельзя отменить.") },
                confirmButton = {
                    TextButton(onClick = {
                        onDeleteBlock(blockToDelete!!)
                        blockToDelete = null
                    }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { blockToDelete = null }) { Text("Отмена") }
                }
            )
        }
    }

@Composable
private fun MistakesButton(
    onClick: ((() -> Unit) -> Unit),
    modifier: Modifier = Modifier
) {
    var sent by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.96f
            sent -> 1.04f
            else -> 1f
        },
        label = "mistakes_button_scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                sent = false
                onClick { sent = true }
            },
            interactionSource = interactionSource,
            modifier = modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        ) {
            Text("Отправить ошибки")
        }
        AnimatedVisibility(visible = sent) {
            Text(
                text = "Ответы высланы",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun FlashcardScreen(
    session: StudySession,
    isReverseMode: Boolean,
    onBack: () -> Unit,
    onExportErrors: (List<Flashcard>, String, () -> Unit) -> Unit,
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
    currentXp: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentCards by remember(session) { mutableStateOf(session.cards) }
    var currentIndex by remember(session) { mutableStateOf(session.currentPosition) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onBack) { Text("<- Назад") }
            Text("🌟 $currentXp XP", fontWeight = FontWeight.Bold, color = Color(0xFFFFB300), style = MaterialTheme.typography.titleMedium)
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (currentIndex < currentCards.size) {
                val currentCard = currentCards[currentIndex]
                Text(
                    text = "${session.title}: ${currentIndex + 1} / ${currentCards.size}",
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                FlashcardUI(
                    flashcard = currentCard,
                    isReverseMode = isReverseMode,
                    onSwipeRight = {
                        currentCard.isKnown = true
                        onProgressChanged(true, currentCard)
                        currentIndex++
                        session.currentPosition = currentIndex
                    },
                    onSwipeLeft = {
                        currentCard.isKnown = false
                        onProgressChanged(false, currentCard)
                        currentIndex++
                        session.currentPosition = currentIndex
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "<- Не знаю", color = Color.Gray, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(text = "Знаю ->", color = Color.Gray, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            } else {
                val correctCount = currentCards.count { it.isKnown == true }
                val wrongCount = currentCards.count { it.isKnown == false }

                Text(text = "Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Знаю: $correctCount | Не знаю: $wrongCount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))

                if (wrongCount > 0) {
                    MistakesButton(
                        onClick = { onExportErrors(currentCards.filter { it.isKnown == false }, session.title, it) },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Button(
                        onClick = {
                            currentCards = currentCards.filter { it.isKnown == false }.onEach { it.isKnown = null }.shuffled()
                            session.cards = currentCards
                            session.currentPosition = 0
                            onProgressChanged(null, null)
                            currentIndex = 0
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Доучить неизвестные слова") }
                }

                Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) { Text("Вернуться в меню") }
            }
        }
    }
}

@Composable
fun FlashcardUI(
    flashcard: Flashcard,
    isReverseMode: Boolean,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val cardHeight = if (isLandscape) 180.dp else 300.dp
    val cardPadding = if (isLandscape) 12.dp else 32.dp

    var isFlipped by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var cardWidthPx by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val swipeThreshold = (cardWidthPx * 0.25f).coerceAtLeast(140f)
    val swipeExitDistance = (cardWidthPx * 1.35f).coerceAtLeast(1000f)

    val actualFront = if (isReverseMode) flashcard.back else flashcard.front
    val actualBack = if (isReverseMode) flashcard.front else flashcard.back
    val textToShow = if (isFlipped) actualBack else actualFront

    val scrollState = rememberScrollState()
    LaunchedEffect(textToShow) {
        scrollState.scrollTo(0)
    }

    val defaultColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val targetColor = when {
        offsetX > 0 -> Color(0xFF81C784)
        offsetX < 0 -> Color(0xFFE57373)
        else -> defaultColor
    }

    val dragFraction = min(abs(offsetX) / 300f, 0.8f)
    val backgroundColor = lerp(defaultColor, targetColor, dragFraction)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(cardPadding)
            .height(cardHeight)
            .onSizeChanged { cardWidthPx = it.width }
            .graphicsLayer {
                translationX = offsetX
            }
            .clickable { isFlipped = !isFlipped }
            .pointerInput(isFlipped) {
                if (isFlipped) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX > swipeThreshold) {
                                    animateSwipeOffset(offsetX, swipeExitDistance, 180) { offsetX = it }
                                    onSwipeRight()
                                    isFlipped = false
                                    offsetX = 0f
                                } else if (offsetX < -swipeThreshold) {
                                    animateSwipeOffset(offsetX, -swipeExitDistance, 180) { offsetX = it }
                                    onSwipeLeft()
                                    isFlipped = false
                                    offsetX = 0f
                                } else {
                                    animateSwipeOffset(offsetX, 0f, 160) { offsetX = it }
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount
                        }
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawVerticalScrollbar(scrollState),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textToShow,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Composable
fun TilesScreen(
    session: StudySession,
    isReverseMode: Boolean,
    onBack: () -> Unit,
    onExportErrors: (List<Flashcard>, String, () -> Unit) -> Unit,
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
    currentXp: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var currentCards by remember(session) { mutableStateOf(session.cards) }
    var currentChunkIndex by remember(session) { mutableStateOf(session.currentPosition) }
    var roundId by remember(session) { mutableStateOf(0) }
    val chunks = remember(currentCards) { currentCards.chunked(4) }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (!isLandscape) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onBack) { Text("<- Назад") }
                Text("🌟 $currentXp XP", fontWeight = FontWeight.Bold, color = Color(0xFFFFB300), style = MaterialTheme.typography.titleMedium)
            }
        }

        if (currentChunkIndex < chunks.size) {
            val currentChunk = chunks[currentChunkIndex]
            val tileItems = remember(roundId, currentChunkIndex) { currentChunk.mapIndexed { index, card -> TileItem(index, card) } }
            val leftItems = remember(roundId, currentChunkIndex) { tileItems.shuffled() }
            val rightItems = remember(roundId, currentChunkIndex) { tileItems.shuffled() }

            var selectedLeftId by remember(roundId, currentChunkIndex) { mutableStateOf<Int?>(null) }
            var selectedRightId by remember(roundId, currentChunkIndex) { mutableStateOf<Int?>(null) }
            var matchedIds by remember(roundId, currentChunkIndex) { mutableStateOf(emptySet<Int>()) }
            var errorLeftIds by remember(roundId, currentChunkIndex) { mutableStateOf(emptySet<Int>()) }
            var errorRightIds by remember(roundId, currentChunkIndex) { mutableStateOf(emptySet<Int>()) }
            val coroutineScope = rememberCoroutineScope()

            val checkMatch = {
                if (selectedLeftId != null && selectedRightId != null) {
                    val leftId = selectedLeftId!!
                    val rightId = selectedRightId!!
                    val leftCard = currentChunk[leftId]
                    val rightCard = currentChunk[rightId]

                    if (leftId == rightId) {
                        matchedIds = matchedIds + leftId
                        if (leftCard.isKnown != false) leftCard.isKnown = true
                        onProgressChanged(true, leftCard)
                    } else {
                        leftCard.isKnown = false
                        rightCard.isKnown = false
                        onProgressChanged(false, leftCard)
                        errorLeftIds = errorLeftIds + leftId
                        errorRightIds = errorRightIds + rightId
                    }
                    
                    selectedLeftId = null
                    selectedRightId = null
                    
                    if (matchedIds.size + errorLeftIds.size == currentChunk.size) {
                        coroutineScope.launch {
                            delay(600)
                            currentChunkIndex++
                            session.currentPosition = currentChunkIndex
                        }
                    }
                }
            }

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onBack) { Text("<- Назад") }
                    Text(
                        text = "Найдите пары (${currentChunkIndex + 1}/${chunks.size}):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("🌟 $currentXp XP", fontWeight = FontWeight.Bold, color = Color(0xFFFFB300), style = MaterialTheme.typography.titleMedium)
                }
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Top
            ) {
                if (!isLandscape) {
                    Spacer(modifier = Modifier.weight(0.3f))
                    Text(
                        text = "Найдите пары (${currentChunkIndex + 1}/${chunks.size}):",
                        modifier = Modifier.padding(bottom = 24.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                if (isLandscape) {
                    // В альбомном режиме раскладываем 2х2 слева и 2х2 справа с центральной разделительной полосой
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Левая колонка (Слова): 2 строки по 2 плитки
                        Column(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            leftItems.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowItems.forEach { item ->
                                        val text = if (isReverseMode) item.card.back else item.card.front
                                        val isMatched = matchedIds.contains(item.id)
                                        val isError = errorLeftIds.contains(item.id)
                                        val isSelected = selectedLeftId == item.id
                                        val isDisabled = isMatched || isError

                                        TileButton(
                                            text = text,
                                            isMatched = isMatched,
                                            isSelected = isSelected,
                                            isError = isError,
                                            isDisabled = isDisabled,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            if (!isDisabled) {
                                                if (selectedLeftId != null && selectedRightId != null) return@TileButton
                                                selectedLeftId = if (selectedLeftId == item.id) null else item.id
                                                if (selectedLeftId != null) checkMatch()
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Вертикальная разделительная черта по центру экрана
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.9f)
                                .width(2.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )

                        // Правая колонка (Переводы): 2 строки по 2 плитки
                        Column(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rightItems.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.weight(1f).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowItems.forEach { item ->
                                        val text = if (isReverseMode) item.card.front else item.card.back
                                        val isMatched = matchedIds.contains(item.id)
                                        val isError = errorRightIds.contains(item.id)
                                        val isSelected = selectedRightId == item.id
                                        val isDisabled = isMatched

                                        TileButton(
                                            text = text,
                                            isMatched = isMatched,
                                            isSelected = isSelected,
                                            isError = isError,
                                            isDisabled = isDisabled,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            if (!isDisabled) {
                                                if (selectedLeftId != null && selectedRightId != null) return@TileButton
                                                selectedRightId = if (selectedRightId == item.id) null else item.id
                                                if (selectedRightId != null) checkMatch()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            leftItems.forEach { item ->
                                val text = if (isReverseMode) item.card.back else item.card.front
                                val isMatched = matchedIds.contains(item.id)
                                val isError = errorLeftIds.contains(item.id)
                                val isSelected = selectedLeftId == item.id
                                val isDisabled = isMatched || isError

                                TileButton(text = text, isMatched = isMatched, isSelected = isSelected, isError = isError, isDisabled = isDisabled) {
                                    if (!isDisabled) {
                                        if (selectedLeftId != null && selectedRightId != null) return@TileButton
                                        selectedLeftId = if (selectedLeftId == item.id) null else item.id
                                        if (selectedLeftId != null) checkMatch()
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            rightItems.forEach { item ->
                                val text = if (isReverseMode) item.card.front else item.card.back
                                val isMatched = matchedIds.contains(item.id)
                                val isError = errorRightIds.contains(item.id)
                                val isSelected = selectedRightId == item.id
                                val isDisabled = isMatched

                                TileButton(text = text, isMatched = isMatched, isSelected = isSelected, isError = isError, isDisabled = isDisabled) {
                                    if (!isDisabled) {
                                        if (selectedLeftId != null && selectedRightId != null) return@TileButton
                                        selectedRightId = if (selectedRightId == item.id) null else item.id
                                        if (selectedRightId != null) checkMatch()
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        } else {
            val correctCount = currentCards.count { it.isKnown == true }
            val wrongCount = currentCards.count { it.isKnown == false }

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Знаю: $correctCount | Не знаю: $wrongCount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))
                if (wrongCount > 0) {
                    MistakesButton(
                        onClick = { onExportErrors(currentCards.filter { it.isKnown == false }, session.title, it) },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Button(
                        onClick = {
                            currentCards = currentCards.filter { it.isKnown == false }.onEach { it.isKnown = null }.shuffled()
                            session.cards = currentCards
                            session.currentPosition = 0
                            onProgressChanged(null, null)
                            currentChunkIndex = 0
                            roundId++
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Доучить неизвестные слова") }
                }
                Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) { Text("Вернуться в меню") }
            }
        }
    }
}

@Composable
fun TypingScreen(
    session: StudySession,
    isReverseMode: Boolean,
    onBack: () -> Unit,
    onExportErrors: (List<Flashcard>, String, () -> Unit) -> Unit,
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
    currentXp: Int,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember(session) { mutableStateOf(session.currentPosition) }
    val card = session.cards.getOrNull(currentIndex)
    var answer by remember(currentIndex) { mutableStateOf("") }
    
    // Если мы загрузили карточку, которая УЖЕ была отгадана (или провалена) ранее,
    // сразу помечаем её как submitted, чтобы не давать вводить заново
    var submitted by remember(currentIndex) { mutableStateOf(card?.isKnown != null) }

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onBack) { Text("<- Назад") }
            Text("🌟 $currentXp XP", fontWeight = FontWeight.Bold, color = Color(0xFFFFB300), style = MaterialTheme.typography.titleMedium)
        }
        if (card != null) {
            val prompt = if (isReverseMode) card.back else card.front
            val expected = if (isReverseMode) card.front else card.back
            Text("${session.title}: ${currentIndex + 1} / ${session.cards.size}")
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                
                Text(
                    text = prompt, 
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold, 
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                
                OutlinedTextField(
                    value = answer,
                    onValueChange = { if (!submitted) answer = it },
                    label = { Text("Введите перевод") },
                    singleLine = true,
                    enabled = !submitted,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                )
                if (submitted) {
                    val resultColor by animateColorAsState(
                        if (card.isKnown == true) Color(0xFF2E7D32) else Color(0xFFC62828),
                        label = "answer_result_color"
                    )
                    Text(
                        text = if (card.isKnown == true) "Правильно!" else "Правильный ответ: $expected",
                        color = resultColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
            
            if (!submitted) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            card.isKnown = false
                            submitted = true
                            onProgressChanged(false, card)
                            
                            // Сохраняем, что мы ПРОШЛИ это слово, чтобы при возврате
                            // из меню нас кинуло на следующее слово
                            session.currentPosition = currentIndex + 1
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Не знаю") }
                    
                    Button(
                        onClick = {
                            val correct = answer.trim().equals(expected.trim(), ignoreCase = true)
                            card.isKnown = correct
                            submitted = true
                            onProgressChanged(correct, card)
                            
                            // Сохраняем позицию следующего слова
                            session.currentPosition = currentIndex + 1
                        },
                        enabled = answer.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) { Text("Проверить") }
                }
            } else {
                Button(
                    onClick = {
                        currentIndex++
                        // session.currentPosition = currentIndex // Убрал отсюда, так как мы уже сохранили это при ответе
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp)
                ) { Text("Следующее слово") }
            }
        } else {
            Text("Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
            val wrongCards = session.cards.filter { it.isKnown == false }
            if (wrongCards.isNotEmpty()) {
                MistakesButton(
                    onClick = { onExportErrors(wrongCards, session.title, it) },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) { Text("Вернуться в меню") }
        }
    }
}

private data class TileItem(val id: Int, val card: Flashcard)

fun Modifier.drawVerticalScrollbar(
    scrollState: ScrollState,
    color: Color = Color.Gray.copy(alpha = 0.45f)
): Modifier = drawWithContent {
    drawContent()
    val maxValue = scrollState.maxValue
    if (maxValue > 0) {
        val elementHeight = size.height
        val topPadding = 10.dp.toPx()
        val bottomPadding = 10.dp.toPx()
        val rightPadding = 6.dp.toPx()
        val barWidth = 3.dp.toPx()

        val usableHeight = elementHeight - topPadding - bottomPadding
        if (usableHeight > 0) {
            val totalContentHeight = elementHeight + maxValue
            val visibleRatio = elementHeight / totalContentHeight
            val barHeight = (usableHeight * visibleRatio).coerceAtLeast(12.dp.toPx())
            val scrollValue = scrollState.value.toFloat()
            val barTop = topPadding + (scrollValue / maxValue) * (usableHeight - barHeight)
            val barLeft = size.width - barWidth - rightPadding

            drawRoundRect(
                color = color,
                topLeft = Offset(barLeft, barTop),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}

@Composable
fun TileButton(
    text: String,
    isMatched: Boolean,
    isSelected: Boolean,
    isError: Boolean,
    isDisabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isSplit = isMatched && isError
    
    val backgroundColor = when {
        isSplit -> Color.Transparent // Цвет зададим через градиент внутри
        isMatched -> Color(0xFF81C784)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isError -> Color(0xFFE57373)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    // Состояние скролла для длинного текста
    val scrollState = rememberScrollState()
    
    // При смене текста (новая карточка) сбрасываем скролл на самый верх
    LaunchedEffect(text) {
        scrollState.scrollTo(0)
    }

    val baseCardModifier = if (isLandscape) {
        Modifier.fillMaxWidth().fillMaxHeight().padding(vertical = 3.dp)
    } else {
        Modifier.fillMaxWidth().height(100.dp).padding(vertical = 8.dp)
    }

    Card(
        modifier = modifier.then(baseCardModifier).clickable(enabled = !isDisabled, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val baseModifier = Modifier.fillMaxSize()
        // Жесткий градиент без плавного перехода (50% красного, 50% зеленого)
        val finalModifier = if (isSplit) {
            baseModifier.then(
                Modifier.background(
                    Brush.horizontalGradient(
                        0.0f to Color(0xFFE57373),
                        0.5f to Color(0xFFE57373),
                        0.5f to Color(0xFF81C784),
                        1.0f to Color(0xFF81C784)
                    )
                )
            )
        } else {
            baseModifier
        }

        Box(
            modifier = finalModifier.drawVerticalScrollbar(scrollState), 
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text, 
                textAlign = TextAlign.Center, 
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(scrollState), // Добавили вертикальный скролл
                style = if (isLandscape) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun loadFlashcardsFromCsv(context: Context, filename: String): List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    try {
        val inputStream = context.assets.open(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine()
        var line: String? = reader.readLine()
        while (line != null) {
            val parts = line.split(",")
            if (parts.size >= 2) {
                flashcards.add(Flashcard(front = parts[0].trim(), back = parts[1].trim()))
            }
            line = reader.readLine()
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
        flashcards.add(Flashcard("Error", "Ошибка чтения файла $filename"))
    }
    return flashcards
}

fun loadFlashcardsFromUri(context: Context, uri: Uri): List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine()
            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split(",")
                if (parts.size >= 2) {
                    flashcards.add(Flashcard(front = parts[0].trim(), back = parts[1].trim()))
                }
                line = reader.readLine()
            }
            reader.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return flashcards
}

fun parseFlashcardsFromCsv(bytes: ByteArray): List<Flashcard> {
    return bytes.toString(Charsets.UTF_8).lineSequence().drop(1).mapNotNull { line ->
        val parts = line.split(",")
        if (parts.size >= 2) Flashcard(parts[0].trim(), parts[1].trim()) else null
    }.toList()
}

private fun csvCell(value: String): String =
    "\"" + value.replace("\"", "\"\"") + "\""

fun uploadErrorReport(
    context: Context,
    deckName: String,
    cards: List<Flashcard>,
    onSuccess: () -> Unit = {}
) {
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null)
        ?: return
    if (cards.isEmpty()) return

    val csv = buildString {
        appendLine()
        cards.forEach { card ->
            append(card.front.replace("\r", " ").replace("\n", " "))
            append(',')
            appendLine(card.back.replace("\r", " ").replace("\n", " "))
        }
    }
    val safeDeckName = deckName.replace(Regex("[^A-Za-z0-9А-Яа-яёЁ._-]"), "_")
    val fileName = "${safeDeckName}_mistakes.csv"
    FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
        .reference.child("error_reports/$nickname/$fileName")
        .putBytes(csv.toByteArray(StandardCharsets.UTF_8))
        .addOnSuccessListener { onSuccess() }
}

fun sendDeckToUser(
    context: Context,
    targetUserNickname: String,
    deckName: String,
    cards: List<Flashcard>,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val myNickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, "Друг") ?: "Друг"
        
    val csv = buildString {
        appendLine("front,back")
        cards.forEach { card ->
            append(card.front.replace("\r", " ").replace("\n", " "))
            append(',')
            appendLine(card.back.replace("\r", " ").replace("\n", " "))
        }
    }
    
    val safeDeckName = deckName.replace(Regex("[^A-Za-z0-9А-Яа-яёЁ._-]"), "_")
    val fileName = "${safeDeckName}.csv"
    val bytes = csv.toByteArray(StandardCharsets.UTF_8)
    
    FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
        .reference
        .child("decks/$targetUserNickname/$fileName")
        .putBytes(bytes)
        .addOnSuccessListener {
            FirebaseDatabase.getInstance()
                .getReference("users/$targetUserNickname/available_decks_count")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val count = currentData.getValue(Long::class.java) ?: 0L
                        currentData.value = count + 1L
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) = Unit
                })
                
            val userAvatar = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString("user_avatar", "img") ?: "img"
            val notificationData = mapOf(
                "action" to "NEW_DECK",
                "sender" to myNickname,
                "message" to "Отправил вам колоду «$deckName». Откройте чат, чтобы скачать её.",
                "avatarId" to userAvatar
            )
            FirebaseDatabase.getInstance()
                .getReference("notificationRequests/$targetUserNickname")
                .push()
                .setValue(notificationData)

            // 1. Отправляем карточку колоды в chat_inbox получателя
            val deckMessageText = "DECK:::$deckName:::${cards.size}"
            val newMessage = mapOf(
                "text" to deckMessageText,
                "timestamp" to System.currentTimeMillis()
            )
            FirebaseDatabase.getInstance().getReference("chat_inbox/$targetUserNickname/$myNickname")
                .push().setValue(newMessage)

            // 2. Сохраняем карточку колоды в историю своего чата
            val myHistory = loadChatHistory(context, targetUserNickname).toMutableList()
            myHistory.add(ChatMessage(deckMessageText, isFromMe = true))
            saveChatHistory(context, targetUserNickname, myHistory)
                
            onSuccess()
        }
        .addOnFailureListener { error ->
            onFailure(error.message ?: "Не удалось отправить колоду")
        }
}

fun downloadSpecificSharedDeck(
    context: Context,
    deckName: String,
    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit
) {
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null) ?: return
        
    val safeDeckName = deckName.replace(Regex("[^A-Za-z0-9А-Яа-яёЁ._-]"), "_")
    val fileName = "$safeDeckName.csv"
    val storageRef = FirebaseStorage.getInstance().reference.child("decks/$nickname/$fileName")
    
    storageRef.getBytes(10L * 1024L * 1024L)
        .addOnSuccessListener { bytes ->
            val cards = parseFlashcardsFromCsv(bytes)
            storageRef.delete().addOnCompleteListener {
                decrementAvailableDeckCount(nickname)
                if (cards.isNotEmpty()) {
                    val currentBlocks = (loadBlocksFromStorage(context) ?: emptyList()).toMutableList()
                    val existingIndex = currentBlocks.indexOfFirst { it.name.equals(deckName, ignoreCase = true) }
                    if (existingIndex >= 0) {
                        currentBlocks[existingIndex] = FlashcardBlock(deckName, cards)
                    } else {
                        currentBlocks.add(FlashcardBlock(deckName, cards))
                    }
                    saveBlocksToStorage(context, currentBlocks)
                    onSuccess(deckName)
                } else {
                    onFailure("Файл колоды пуст")
                }
            }
        }
        .addOnFailureListener {
            val currentBlocks = loadBlocksFromStorage(context) ?: emptyList()
            if (currentBlocks.any { it.name.equals(deckName, ignoreCase = true) }) {
                onSuccess("$deckName (уже в ваших колодах)")
            } else {
                onFailure("Колода недоступна для скачивания")
            }
        }
}

fun downloadSharedDecks(
    context: Context,
    blocks: MutableList<FlashcardBlock>,
    onComplete: () -> Unit = {}
) {
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null) ?: return
    FirebaseStorage.getInstance().reference.child("decks/$nickname").listAll()
        .addOnSuccessListener { result ->
            fun finish() {
                saveBlocksToStorage(context, blocks)
                onComplete()
            }

            fun processNext(index: Int) {
                if (index >= result.items.size) {
                    finish()
                    return
                }

                val file = result.items[index]
                file.getBytes(10L * 1024L * 1024L)
                    .addOnSuccessListener { bytes ->
                        val cards = parseFlashcardsFromCsv(bytes)
                        file.delete()
                            .addOnSuccessListener {
                                decrementAvailableDeckCount(nickname)
                                if (cards.isNotEmpty()) {
                                    blocks.add(
                                        FlashcardBlock(
                                            file.name.removeSuffix(".csv"),
                                            cards
                                        )
                                    )
                                }
                                processNext(index + 1)
                            }
                            .addOnFailureListener {
                                processNext(index + 1)
                            }
                    }
                    .addOnFailureListener {
                        processNext(index + 1)
                    }
            }

            processNext(0)
        }
        .addOnFailureListener {
            onComplete()
        }
}

private fun decrementAvailableDeckCount(nickname: String) {
    FirebaseDatabase.getInstance()
        .getReference("users/$nickname/available_decks_count")
        .runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val count = currentData.getValue(Long::class.java) ?: 0L
                currentData.value = (count - 1L).coerceAtLeast(0L)
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) = Unit
        })
}

private suspend fun animateSwipeOffset(
    start: Float,
    target: Float,
    durationMillis: Int,
    onFrame: (Float) -> Unit
) {
    if (start == target) return

    val startTime = withFrameNanos { it }
    val durationNanos = durationMillis * 1_000_000L

    while (true) {
        val frameTime = withFrameNanos { it }
        val progress = ((frameTime - startTime).toFloat() / durationNanos)
            .coerceIn(0f, 1f)
        val easedProgress = 1f - (1f - progress) * (1f - progress)
        onFrame(start + (target - start) * easedProgress)
        if (progress >= 1f) break
    }
}

fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) result = cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) result = result?.substring(cut + 1)
    }
    return result ?: "unknown_file"
}

enum class StudyMode { SWIPE, TILES, INPUT }

data class StudySession(
    val title: String,
    var cards: List<Flashcard>,
    val mode: StudyMode = StudyMode.SWIPE,
    val isErrorsOnly: Boolean = false,
    val sourceBlockName: String = title,
    var currentPosition: Int = 0,
    var deferredErrorCards: List<Flashcard> = emptyList()
) : Serializable {
    fun hasUnfinishedContent(): Boolean = when (mode) {
        StudyMode.SWIPE -> currentPosition < cards.size
        StudyMode.TILES -> currentPosition < cards.chunked(4).size
        StudyMode.INPUT -> currentPosition < cards.size
    }

    fun progressLabel(): String = when (mode) {
        StudyMode.SWIPE -> "карточка ${currentPosition + 1} из ${cards.size}"
        StudyMode.TILES -> "группа ${currentPosition + 1} из ${cards.chunked(4).size}"
        StudyMode.INPUT -> "карточка ${currentPosition + 1} из ${cards.size}"
    }
}

fun saveBlocksToStorage(context: Context, blocks: List<FlashcardBlock>, syncCloud: Boolean = true) {
    try {
        val file = File(context.filesDir, "flashcards_data.dat")
        ObjectOutputStream(file.outputStream()).use { it.writeObject(blocks.toList()) }
        if (syncCloud) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (!uid.isNullOrBlank()) {
                FirebaseDatabase.getInstance().getReference("users/$uid/flashcards").setValue(blocks)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun createBackup(context: Context, outputStream: OutputStream) {
    try {
        ZipOutputStream(outputStream).use { zos ->
            val filesToBackup = context.filesDir.listFiles { _, name -> 
                name == "flashcards_data.dat" || (name.startsWith("chat_") && name.endsWith(".dat")) 
            }
            filesToBackup?.forEach { file ->
                zos.putNextEntry(ZipEntry(file.name))
                file.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun restoreBackup(context: Context, inputStream: InputStream) {
    try {
        ZipInputStream(inputStream).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (entry.name == "flashcards_data.dat" || (entry.name.startsWith("chat_") && entry.name.endsWith(".dat"))) {
                    val file = File(context.filesDir, entry.name)
                    file.outputStream().use { fos ->
                        zis.copyTo(fos)
                    }
                }
                entry = zis.nextEntry
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Suppress("UNCHECKED_CAST")
fun loadBlocksFromStorage(context: Context): List<FlashcardBlock>? {
    return try {
        val file = File(context.filesDir, "flashcards_data.dat")
        if (file.exists()) {
            ObjectInputStream(file.inputStream()).use { it.readObject() as? List<FlashcardBlock> }
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
