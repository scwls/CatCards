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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import android.provider.OpenableColumns
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.lerp
import kotlin.math.abs
import kotlin.math.min
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.storage.FirebaseStorage
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val STUDY_PREFS = "study_preferences"
private const val LAST_STUDY_DATE = "last_study_date"
private const val NICKNAME_PREFS = "user_preferences"
private const val NICKNAME_KEY = "nickname"
private const val FIREBASE_STORAGE_BUCKET = "gs://card-eng.firebasestorage.app"

data class UserActivity(val learned: Long, val wrong: Long)

fun incrementWrongToday(context: Context) {
    val prefs = context.getSharedPreferences(STUDY_PREFS, Context.MODE_PRIVATE)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val count = if (prefs.getString("wrong_date", "") == today) prefs.getInt("wrong_value", 0) + 1 else 1
    prefs.edit().putString("wrong_date", today).putInt("wrong_value", count).apply()
    context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString(NICKNAME_KEY, null)?.let {
        FirebaseDatabase.getInstance().getReference("users/$it/wrong_today").setValue(count)
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

fun showAdminMessageNotification(context: Context, message: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "admin_msg", 
                "Сообщения от разработчика", 
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val notification = NotificationCompat.Builder(context, "admin_msg")
        .setSmallIcon(R.drawable.ic_dialog_info)
        .setContentTitle("Сообщение от разработчика")
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Запускаем будильники при старте
        scheduleDailyAlarms(this)
        
        // Регистрируем фоновую задачу для проверки уведомлений от разработчика
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            15, TimeUnit.MINUTES // Минимально разрешенный интервал в Android
        ).build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        setContent {
            var pinkTheme by rememberSaveable { mutableStateOf(false) }
            val context = LocalContext.current

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
                            mutableStateOf(!context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
                                .getString(NICKNAME_KEY, null).isNullOrBlank())
                        }
                        if (!hasNickname) {
                            RegistrationScreen(onRegistered = { name ->
                                context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
                                    .edit().putString(NICKNAME_KEY, name.trim()).apply()
                                FirebaseDatabase.getInstance()
                                    .getReference("users/${name.trim()}/learned_today")
                                    .setValue(0L)
                                hasNickname = true
                            })
                        } else {
                            FlashcardApp(modifier = Modifier.fillMaxSize())
                            BottomTools(
                                pinkTheme = pinkTheme,
                                onThemeChange = { pinkTheme = it },
                                modifier = Modifier.align(Alignment.BottomStart)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationScreen(onRegistered: (String) -> Unit) {
    var nickname by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(nickname, { nickname = it }, label = { Text("Имя") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
        Button(onClick = { if (nickname.isNotBlank()) onRegistered(nickname) }, modifier = Modifier.fillMaxWidth()) {
            Text("Войти")
        }
    }
}

private const val DEVELOPER_PASSWORD = "504680"

@Composable
private fun BottomTools(
    pinkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showToolsMenu by remember { mutableStateOf(false) }
    var showDeveloperDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(start = 16.dp, bottom = 16.dp)) {
        FloatingActionButton(
            onClick = { showToolsMenu = true },
            modifier = Modifier.height(48.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) { Text("⚙", fontSize = 22.sp) }
    }

    if (showToolsMenu) {
        AlertDialog(
            onDismissRequest = { showToolsMenu = false },
            title = { Text("Настройки") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Тема", fontWeight = FontWeight.Bold)
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = !pinkTheme,
                            onClick = { onThemeChange(false) },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) { Text("BLUE") }
                        SegmentedButton(
                            selected = pinkTheme,
                            onClick = { onThemeChange(true) },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) { Text("ROSE") }
                    }
                    Button(
                        onClick = {
                            showToolsMenu = false
                            showDeveloperDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Войти в режим разработчика") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showToolsMenu = false }) { Text("Готово") }
            }
        )
    }
    if (showDeveloperDialog) DeveloperDialog(onDismiss = { showDeveloperDialog = false })
}

@Composable
private fun DeveloperDialog(onDismiss: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var authorized by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<Map<String, UserActivity>>(emptyMap()) }
    var selectedUser by remember { mutableStateOf<String?>(null) }
    var userToDelete by remember { mutableStateOf<String?>(null) }
    var userToMessage by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }
    var uploadMessage by remember { mutableStateOf<String?>(null) }
    var usersError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val user = selectedUser ?: return@rememberLauncherForActivityResult
        if (uri != null && getFileName(context, uri).endsWith(".csv", true)) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            if (bytes == null) {
                uploadMessage = "Не удалось прочитать выбранный файл"
                return@rememberLauncherForActivityResult
            }
            FirebaseStorage.getInstance(FIREBASE_STORAGE_BUCKET)
                    .reference
                    .child("decks/$user/${getFileName(context, uri)}")
                    .putBytes(bytes)
                    .addOnSuccessListener {
                        uploadMessage = "Колода отправлена пользователю $user"
                    }
                    .addOnFailureListener { error ->
                        uploadMessage = "Ошибка отправки: ${error.message ?: error.javaClass.simpleName}"
                    }
        }
    }
    val loadUsers = {
        usersError = null
        // Используем addValueEventListener вместо get() для получения обновлений в реальном времени
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users = snapshot.children.associate {
                        it.key.orEmpty() to UserActivity(
                            (it.child("learned_today").value as? Number)?.toLong().orZero(),
                            (it.child("wrong_today").value as? Number)?.toLong().orZero()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    usersError = error.message
                }
            })
    }

    LaunchedEffect(authorized) {
        if (authorized) {
            loadUsers()
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
                    // Удаляем из Realtime Database
                    FirebaseDatabase.getInstance().getReference("users/$user").removeValue()
                        .addOnSuccessListener {
                            userToDelete = null
                        }
                }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) { Text("Отмена") }
            }
        )
    }

    // Диалог для отправки сообщения
    if (userToMessage != null) {
        AlertDialog(
            onDismissRequest = { userToMessage = null },
            title = { Text("Сообщение для $userToMessage") },
            text = {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    label = { Text("Текст сообщения") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (messageText.isNotBlank()) {
                        FirebaseDatabase.getInstance().getReference("users/$userToMessage/notifications")
                            .push().setValue(messageText)
                        userToMessage = null
                        messageText = ""
                    }
                }) { Text("Отправить") }
            },
            dismissButton = {
                TextButton(onClick = { userToMessage = null }) { Text("Отмена") }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (authorized) "Режим разработчика" else "Доступ разработчика") },
        text = {
            if (authorized) Column {
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
                                
                                // Кнопка сообщения
                                Box(
                                    modifier = Modifier
                                        .clickable { userToMessage = user }
                                        .padding(8.dp)
                                ) {
                                    Text("💬", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { picker.launch("*/*") },
                    enabled = selectedUser != null,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    Text("Выбрать CSV и отправить")
                }

                uploadMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
            }
            else OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (authorized) onDismiss() else authorized = password == DEVELOPER_PASSWORD
            }) { Text(if (authorized) "Закрыть" else "Войти") }
        },
        dismissButton = { if (!authorized) TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

private fun Long?.orZero(): Long = this ?: 0L

@Composable
fun FlashcardApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val blocks = remember {
        val savedBlocks = loadBlocksFromStorage(context)
        if (savedBlocks != null) {
            mutableStateListOf(*savedBlocks.toTypedArray())
        } else {
            val initial = listOf(
                FlashcardBlock("Животные (CSV)", loadFlashcardsFromCsv(context, "animals.csv")),
                FlashcardBlock("Еда (CSV)", loadFlashcardsFromCsv(context, "food.csv")),
                FlashcardBlock("Встроенные", listOf(
                    Flashcard("Test", "Тест"),
                    Flashcard("Android", "Андроид")
                ))
            )
            saveBlocksToStorage(context, initial)
            mutableStateListOf(*initial.toTypedArray())
        }
    }

    var activeSession by remember { mutableStateOf<StudySession?>(null) }
    var pausedSession by remember { mutableStateOf<StudySession?>(null) }
    var blockForDialog by remember { mutableStateOf<FlashcardBlock?>(null) }
    var isReverseMode by remember { mutableStateOf(true) }

    // Слушатель уведомлений от разработчика
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE).getString(NICKNAME_KEY, null)
    LaunchedEffect(nickname) {
        if (!nickname.isNullOrBlank()) {
            val ref = FirebaseDatabase.getInstance().getReference("users/$nickname/notifications")
            val listener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val msg = snapshot.getValue(String::class.java) ?: return
                    showAdminMessageNotification(context, msg)
                    snapshot.ref.removeValue() // Удаляем сообщение из БД после того как показали
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }
            ref.addChildEventListener(listener)
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
        BlockSelectionScreen(
            blocks = blocks,
            isReverseMode = isReverseMode,
            onModeChange = { isReverseMode = it },
            onBlockSelected = { block -> blockForDialog = block },
            onImportClick = { filePickerLauncher.launch("*/*") },
            onDownloadClick = { downloadSharedDecks(context, blocks) },
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
            modifier = modifier
        )
    } else {
        if (activeSession!!.mode == StudyMode.SWIPE) {
            FlashcardScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = closeActiveSession,
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                },
                modifier = modifier
            )
        } else if (activeSession!!.mode == StudyMode.TILES) {
            TilesScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = closeActiveSession,
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                },
                modifier = modifier
            )
        } else {
            TypingScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = closeActiveSession,
                onProgressChanged = { correct, card ->
                    saveBlocksToStorage(context, blocks)
                    if (correct == true && card != null) incrementLearnedToday(context, card)
                    if (correct == false) incrementWrongToday(context)
                },
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
}

@Composable
fun BlockSelectionScreen(
    blocks: List<FlashcardBlock>,
    isReverseMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    onBlockSelected: (FlashcardBlock) -> Unit,
    onImportClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onDeleteBlock: (FlashcardBlock) -> Unit,
    onRenameBlock: (FlashcardBlock, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val learnedToday = getLearnedToday(context)

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выберите колоду",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onImportClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) { Text("Импортировать колоду (.csv)") }

        Button(
            onClick = onDownloadClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) { Text("Загрузить присланные колоды") }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "Рус -> Англ",
                color = if (isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = if (isReverseMode) FontWeight.Bold else FontWeight.Normal
            )
            Switch(
                checked = !isReverseMode,
                onCheckedChange = { onModeChange(!it) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Англ -> Рус",
                color = if (!isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = if (!isReverseMode) FontWeight.Bold else FontWeight.Normal
            )
        }

        var blockForAction by remember { mutableStateOf<FlashcardBlock?>(null) }
        var blockToDelete by remember { mutableStateOf<FlashcardBlock?>(null) }
        var blockToRename by remember { mutableStateOf<FlashcardBlock?>(null) }
        var newBlockName by remember { mutableStateOf("") }

        LazyColumn(
            modifier = Modifier.weight(1f),
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

        Text(
            text = "Выучено за сегодня: $learnedToday",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp), // Добавили отступ снизу
            textAlign = TextAlign.Center, // Выровняли текст строго по центру
            style = MaterialTheme.typography.titleMedium
        )
        
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
}

@Composable
fun FlashcardScreen(
    session: StudySession,
    isReverseMode: Boolean,
    onBack: () -> Unit,
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
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
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = onBack) { Text("<- Назад") }
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
    var isFlipped by remember { mutableStateOf(false) }
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val defaultColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val targetColor = when {
        offsetX.value > 0 -> Color(0xFF81C784)
        offsetX.value < 0 -> Color(0xFFE57373)
        else -> defaultColor
    }

    val dragFraction = min(abs(offsetX.value) / 300f, 0.8f)
    val backgroundColor = lerp(defaultColor, targetColor, dragFraction)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .height(300.dp)
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .clickable { isFlipped = !isFlipped }
            .pointerInput(isFlipped) {
                if (isFlipped) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                val threshold = 200f
                                if (offsetX.value > threshold) {
                                    offsetX.animateTo(targetValue = 1000f, animationSpec = tween(300))
                                    onSwipeRight()
                                    isFlipped = false
                                    offsetX.snapTo(0f)
                                } else if (offsetX.value < -threshold) {
                                    offsetX.animateTo(targetValue = -1000f, animationSpec = tween(300))
                                    onSwipeLeft()
                                    isFlipped = false
                                    offsetX.snapTo(0f)
                                } else {
                                    offsetX.animateTo(targetValue = 0f, animationSpec = tween(300))
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                        }
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val actualFront = if (isReverseMode) flashcard.back else flashcard.front
            val actualBack = if (isReverseMode) flashcard.front else flashcard.back

            Text(
                text = if (isFlipped) actualBack else actualFront,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TilesScreen(
    session: StudySession,
    isReverseMode: Boolean,
    onBack: () -> Unit,
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentCards by remember(session) { mutableStateOf(session.cards) }
    var currentChunkIndex by remember(session) { mutableStateOf(session.currentPosition) }
    var roundId by remember(session) { mutableStateOf(0) }
    val chunks = remember(currentCards) { currentCards.chunked(4) }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Start) {
            Button(onClick = onBack) { Text("<- Назад") }
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
                    
                    // Переходим на следующий экран, когда ВСЕ левые карточки "израсходованы"
                    // (либо угаданы правильно, либо были потрачены в ошибке)
                    if (matchedIds.size + errorLeftIds.size == currentChunk.size) {
                        coroutineScope.launch {
                            delay(600)
                            currentChunkIndex++
                            session.currentPosition = currentChunkIndex
                        }
                    }
                }
            }

            Text(
                text = "Найдите пары (${currentChunkIndex + 1}/${chunks.size}):",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    leftItems.forEach { item ->
                        val text = if (isReverseMode) item.card.back else item.card.front
                        val isMatched = matchedIds.contains(item.id)
                        val isError = errorLeftIds.contains(item.id)
                        val isSelected = selectedLeftId == item.id
                        // Новое: блокируем левую карточку, если она угадана ИЛИ если была ошибка
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
        } else {
            val correctCount = currentCards.count { it.isKnown == true }
            val wrongCount = currentCards.count { it.isKnown == false }

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Знаю: $correctCount | Не знаю: $wrongCount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))
                if (wrongCount > 0) {
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
    onProgressChanged: (Boolean?, Flashcard?) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember(session) { mutableStateOf(session.currentPosition) }
    val card = session.cards.getOrNull(currentIndex)
    var answer by remember(currentIndex) { mutableStateOf("") }
    var submitted by remember(currentIndex) { mutableStateOf(false) }

    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth().padding(16.dp)) {
            Button(onClick = onBack) { Text("<- Назад") }
        }
        if (card != null) {
            val prompt = if (isReverseMode) card.back else card.front
            val expected = if (isReverseMode) card.front else card.back
            Text("${session.title}: ${currentIndex + 1} / ${session.cards.size}")
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                
                Text(prompt, style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp))
                
                OutlinedTextField(
                    value = answer,
                    onValueChange = { if (!submitted) answer = it },
                    label = { Text("Введите перевод") },
                    singleLine = true,
                    enabled = !submitted,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )
                if (submitted) {
                    val resultColor by animateColorAsState(
                        if (card.isKnown == true) Color(0xFF2E7D32) else Color(0xFFC62828),
                        label = "answer_result_color"
                    )
                    Text(
                        if (card.isKnown == true) "Правильно" else "Неправильно. Ответ: $expected",
                        color = resultColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
            Button(
                onClick = {
                    if (!submitted) {
                        val correct = answer.trim().equals(expected.trim(), ignoreCase = true)
                        card.isKnown = correct
                        submitted = true
                        onProgressChanged(correct, card)
                    } else {
                        currentIndex++
                        session.currentPosition = currentIndex
                    }
                },
                enabled = submitted || answer.isNotBlank(),
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            ) { Text(if (submitted) "Следующее слово" else "Проверить") }
        } else {
            Text("Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) { Text("Вернуться в меню") }
        }
    }
}

private data class TileItem(val id: Int, val card: Flashcard)

@Composable
fun TileButton(text: String, isMatched: Boolean, isSelected: Boolean, isError: Boolean, isDisabled: Boolean, onClick: () -> Unit) {
    // Если карточка участвовала в ошибке, но теперь отгадана верно (актуально для правой колонки)
    val isSplit = isMatched && isError
    
    val backgroundColor = when {
        isSplit -> Color.Transparent // Цвет зададим через градиент внутри
        isMatched -> Color(0xFF81C784)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isError -> Color(0xFFE57373)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(80.dp).clickable(enabled = !isDisabled, onClick = onClick),
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

        Box(modifier = finalModifier, contentAlignment = Alignment.Center) {
            Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
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

fun downloadSharedDecks(context: Context, blocks: MutableList<FlashcardBlock>) {
    val nickname = context.getSharedPreferences(NICKNAME_PREFS, Context.MODE_PRIVATE)
        .getString(NICKNAME_KEY, null) ?: return
    FirebaseStorage.getInstance().reference.child("decks/$nickname").listAll()
        .addOnSuccessListener { result ->
            if (result.items.isEmpty()) return@addOnSuccessListener
            var remaining = result.items.size
            result.items.forEach { file ->
                file.getBytes(10L * 1024L * 1024L).addOnSuccessListener { bytes ->
                    val cards = parseFlashcardsFromCsv(bytes)
                    if (cards.isNotEmpty()) blocks.add(FlashcardBlock(file.name.removeSuffix(".csv"), cards))
                    file.delete().addOnCompleteListener {
                        remaining--
                        if (remaining == 0) saveBlocksToStorage(context, blocks)
                    }
                }.addOnFailureListener {
                    remaining--
                    if (remaining == 0) saveBlocksToStorage(context, blocks)
                }
            }
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
) {
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

fun saveBlocksToStorage(context: Context, blocks: List<FlashcardBlock>) {
    try {
        val file = File(context.filesDir, "flashcards_data.dat")
        ObjectOutputStream(file.outputStream()).use { it.writeObject(blocks.toList()) }
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
