package com.example.myapplication

import android.content.Context
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
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlashcardApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FlashcardApp(modifier: Modifier = Modifier) {
    // Наши данные: грузятся из сохраненного файла, либо берутся по умолчанию
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

    // Активная сессия (то, что мы сейчас учим)
    var activeSession by remember { mutableStateOf<StudySession?>(null) }
    
    // Блок, для которого открыто диалоговое окно
    var blockForDialog by remember { mutableStateOf<FlashcardBlock?>(null) }
    
    // Состояние: направление перевода (false = Англ->Рус, true = Рус->Англ)
    var isReverseMode by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = getFileName(context, uri)
            // Проверяем, что файл действительно имеет расширение .csv
            if (name.endsWith(".csv", ignoreCase = true)) {
                val cards = loadFlashcardsFromUri(context, uri)
                if (cards.isNotEmpty()) {
                    // Убираем расширение для красивого названия
                    val displayName = name.replace(".csv", "", ignoreCase = true)
                    blocks.add(FlashcardBlock(displayName, cards))
                    saveBlocksToStorage(context, blocks)
                }
            } else {
                // Если файл не .csv, можно было бы показать ошибку, но пока просто игнорируем
                // (В реальном приложении здесь обычно показывают Toast сообщение)
            }
        }
    }

    if (activeSession == null) {
        // Главный экран: выбор блока
        BlockSelectionScreen(
            blocks = blocks,
            isReverseMode = isReverseMode,
            onModeChange = { isReverseMode = it },
            onBlockSelected = { block -> blockForDialog = block }, // Открываем диалог вместо прямого перехода
            onImportClick = { filePickerLauncher.launch("*/*") },
            onDeleteBlock = { block ->
                blocks.remove(block)
                saveBlocksToStorage(context, blocks)
            },
            onRenameBlock = { block, newName ->
                val index = blocks.indexOf(block)
                if (index != -1) {
                    // Используем .copy(), так как это data class
                    blocks[index] = block.copy(name = newName)
                    saveBlocksToStorage(context, blocks)
                }
            },
            modifier = modifier
        )
    } else {
        if (activeSession!!.mode == StudyMode.SWIPE) {
            // Экран карточек
            FlashcardScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = { activeSession = null },
                onProgressChanged = { saveBlocksToStorage(context, blocks) },
                modifier = modifier
            )
        } else {
            // Экран плиточек
            TilesScreen(
                session = activeSession!!,
                isReverseMode = isReverseMode,
                onBack = { activeSession = null },
                onProgressChanged = { saveBlocksToStorage(context, blocks) },
                modifier = modifier
            )
        }
    }

        // Диалоговое окно выбора режима обучения
        if (blockForDialog != null) {
            // Находим САМУЮ СВЕЖУЮ версию колоды в списке по её изначальному объекту
            // Если мы её переименовали, старый blockForDialog будет содержать старое имя.
            // Поэтому мы ищем её по индексу или совпадению карточек.
            val latestBlock = blocks.find { it.cards == blockForDialog!!.cards } ?: blockForDialog!!
            val mistakesCount = latestBlock.cards.count { it.isKnown == false }
            
            // Локальное состояние для переключателя режимов (Свайпы или Плиточки)
            var selectedMode by remember { mutableStateOf(StudyMode.SWIPE) }
            
            Dialog(onDismissRequest = { blockForDialog = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        
                        // Переключатель типа игры (Сегментированные кнопки)
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                        ) {
                            val options = listOf("Свайпы", "Плиточки")
                            options.forEachIndexed { index, label ->
                                val mode = if (index == 0) StudyMode.SWIPE else StudyMode.TILES
                                SegmentedButton(
                                    selected = selectedMode == mode,
                                    onClick = { selectedMode = mode },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                                ) {
                                    Text(text = label)
                                }
                            }
                        }
                        
                        Button(
                            onClick = {
                                latestBlock.cards.forEach { it.isKnown = null }
                                saveBlocksToStorage(context, blocks)
                                activeSession = StudySession(latestBlock.name, latestBlock.cards, selectedMode)
                                blockForDialog = null
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Text("Всю колоду с начала")
                        }
                        
                        if (mistakesCount > 0) {
                            Button(
                                onClick = {
                                    val mistakes = latestBlock.cards.filter { it.isKnown == false }
                                    mistakes.forEach { it.isKnown = null }
                                    saveBlocksToStorage(context, blocks)
                                    activeSession = StudySession("${latestBlock.name} (Ошибки)", mistakes, selectedMode)
                                    blockForDialog = null
                                },
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                Text("Только ошибки ($mistakesCount)")
                            }
                        }
                        
                        TextButton(
                            onClick = { blockForDialog = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Отмена", color = Color.Gray)
                        }
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
    onDeleteBlock: (FlashcardBlock) -> Unit,
    onRenameBlock: (FlashcardBlock, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
        ) {
            Text("Импортировать колоду (.csv)")
        }
        
        // Переключатель режима изучения
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "Англ -> Рус", 
                color = if (!isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = if (!isReverseMode) FontWeight.Bold else FontWeight.Normal
            )
            Switch(
                checked = isReverseMode,
                onCheckedChange = onModeChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Рус -> Англ", 
                color = if (isReverseMode) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = if (isReverseMode) FontWeight.Bold else FontWeight.Normal
            )
        }

        // LazyColumn - это специальный компонент для списков. 
        // Он рисует только те элементы, которые сейчас видны на экране (экономит память).
        
        var blockForAction by remember { mutableStateOf<FlashcardBlock?>(null) }
        var blockToDelete by remember { mutableStateOf<FlashcardBlock?>(null) }
        var blockToRename by remember { mutableStateOf<FlashcardBlock?>(null) }
        var newBlockName by remember { mutableStateOf("") }
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(blocks) { block ->
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
                    Text(
                        text = "${block.name} (${block.cards.size} слов)",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(24.dp)
                    )
                }
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
                        ) {
                            Text("Переименовать")
                        }
                        
                        Button(
                            onClick = {
                                blockToDelete = blockForAction
                                blockForAction = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Text("Удалить")
                        }
                        
                        TextButton(
                            onClick = { blockForAction = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Отмена", color = Color.Gray)
                        }
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
                        if (newBlockName.isNotBlank()) {
                            onRenameBlock(blockToRename!!, newBlockName)
                        }
                        blockToRename = null
                    }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { blockToRename = null }) {
                        Text("Отмена")
                    }
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
                    }) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { blockToDelete = null }) {
                        Text("Отмена")
                    }
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
    onProgressChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Используем локальное состояние, чтобы можно было перезапускать список внутри сессии
    var currentCards by remember(session) { mutableStateOf(session.cards) }
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя панель с кнопкой "Назад"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = onBack) {
                Text("<- Назад")
            }
        }

        // Основная часть с карточками
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Занимает всё оставшееся место
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
                        onProgressChanged()
                        currentIndex++
                    },
                    onSwipeLeft = {
                        currentCard.isKnown = false
                        onProgressChanged()
                        currentIndex++
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "<- Не знаю",
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Знаю ->",
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            } else {
                val correctCount = currentCards.count { it.isKnown == true }
                val wrongCount = currentCards.count { it.isKnown == false }
                
                Text(
                    text = "Тренировка завершена!",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Text(
                    text = "Знаю: $correctCount | Не знаю: $wrongCount",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (wrongCount > 0) {
                    Button(
                        onClick = {
                            // Оставляем только те, что не знали, и сбрасываем их статус
                            currentCards = currentCards.filter { it.isKnown == false }.onEach { it.isKnown = null }
                            onProgressChanged()
                            currentIndex = 0
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Доучить неизвестные слова")
                    }
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Вернуться в меню")
                }
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
    // Состояние: показываем ли мы обратную сторону (перевод)
    var isFlipped by remember { mutableStateOf(false) }

    // Анимация: смещение карточки по оси X
    val offsetX = remember { Animatable(0f) }
    // Область корутин для запуска анимаций
    val coroutineScope = rememberCoroutineScope()

    // Базовый цвет (меняется при клике/перевороте)
    val defaultColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    
    // Определяем целевой цвет в зависимости от направления свайпа
    val targetColor = when {
        offsetX.value > 0 -> Color(0xFF81C784) // Зеленый для "Знаю" (немного приглушенный)
        offsetX.value < 0 -> Color(0xFFE57373) // Красный для "Не знаю"
        else -> defaultColor
    }
    
    // Считаем прогресс свайпа (от 0.0 до 1.0)
    // Чем дальше тянем (до 300 пикселей), тем сильнее проявляется цвет
    val dragFraction = min(abs(offsetX.value) / 300f, 0.8f) 
    
    // Плавно смешиваем базовый цвет и цвет свайпа
    val backgroundColor = lerp(defaultColor, targetColor, dragFraction)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .height(300.dp)
            // Применяем смещение к карточке
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            // Обработка клика: переворачиваем карточку
            .clickable { isFlipped = !isFlipped }
            // Обработка свайпов (жестов)
            // Мы передаем isFlipped как ключ (key). Если он меняется, Compose перезапускает этот блок.
            .pointerInput(isFlipped) {
                // Разрешаем жесты свайпа ТОЛЬКО если карточка перевернута
                if (isFlipped) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // Когда отпускаем палец, решаем: улетаем за край или возвращаемся в центр
                            coroutineScope.launch {
                                val threshold = 200f // Порог срабатывания свайпа (чуть больше, чтобы было удобнее)
                                if (offsetX.value > threshold) {
                                    // Улетаем вправо
                                    offsetX.animateTo(targetValue = 1000f, animationSpec = tween(300))
                                    onSwipeRight()
                                    isFlipped = false
                                    offsetX.snapTo(0f) // Мгновенно возвращаем невидимую новую карточку в центр
                                } else if (offsetX.value < -threshold) {
                                    // Улетаем влево
                                    offsetX.animateTo(targetValue = -1000f, animationSpec = tween(300))
                                    onSwipeLeft()
                                    isFlipped = false
                                    offsetX.snapTo(0f)
                                } else {
                                    // Возвращаемся в центр, если свайп был слишком слабым
                                    offsetX.animateTo(targetValue = 0f, animationSpec = tween(300))
                                }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            // Пока тянем палец, двигаем карточку
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        }
                    )
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        // Устанавливаем динамический цвет, который мы рассчитали выше
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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
    onProgressChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentCards by remember(session) { mutableStateOf(session.cards) }
    var currentChunkIndex by remember { mutableStateOf(0) }

    // Разбиваем карточки на группы по 4 штуки
    val chunks = remember(currentCards) { currentCards.chunked(4) }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Верхняя панель с кнопкой "Назад"
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Start) {
            Button(onClick = onBack) { Text("<- Назад") }
        }

        if (currentChunkIndex < chunks.size) {
            val currentChunk = chunks[currentChunkIndex]
            
            // Перемешиваем левый и правый столбцы независимо друг от друга
            val leftItems = remember(currentChunk) { currentChunk.shuffled() }
            val rightItems = remember(currentChunk) { currentChunk.shuffled() }

            // Состояния выделения и ошибок
            var selectedLeft by remember { mutableStateOf<Flashcard?>(null) }
            var selectedRight by remember { mutableStateOf<Flashcard?>(null) }
            var matched by remember { mutableStateOf(setOf<Flashcard>()) }
            var errorPairs by remember { mutableStateOf(setOf<Flashcard>()) }
            val coroutineScope = rememberCoroutineScope()

            // Логика проверки совпадения
            val checkMatch = {
                if (selectedLeft != null && selectedRight != null) {
                    val leftCard = selectedLeft!!
                    val rightCard = selectedRight!!
                    
                    if (leftCard == rightCard) {
                        // Правильно! 
                        matched = matched + leftCard
                        if (leftCard.isKnown != false) {
                            leftCard.isKnown = true
                        }
                        onProgressChanged()
                        
                        val currentMatchedSize = matched.size
                        val totalPairsResolved = currentMatchedSize + (errorPairs.size / 2)
                        
                        selectedLeft = null
                        selectedRight = null
                        
                        // Если ВСЕ 4 пары отгаданы (или провалены), переходим дальше
                        if (totalPairsResolved == currentChunk.size) {
                            coroutineScope.launch {
                                delay(600) // Даем посмотреть на последнюю пару
                                currentChunkIndex++
                                matched = emptySet()
                                errorPairs = emptySet()
                            }
                        }
                    } else {
                        // Ошибка!
                        leftCard.isKnown = false
                        rightCard.isKnown = false
                        onProgressChanged()
                        
                        errorPairs = errorPairs + leftCard + rightCard
                        
                        selectedLeft = null
                        selectedRight = null
                        
                        val totalPairsResolved = matched.size + (errorPairs.size / 2)
                        if (totalPairsResolved == currentChunk.size) {
                             coroutineScope.launch {
                                delay(1000) 
                                currentChunkIndex++
                                matched = emptySet()
                                errorPairs = emptySet()
                            }
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
                // Левая колонка
                Column(modifier = Modifier.weight(1f)) {
                    leftItems.forEach { card ->
                        val text = if (isReverseMode) card.back else card.front
                        val isMatched = matched.contains(card)
                        // Карточка красная ТОЛЬКО если она есть в списке ошибок
                        val isError = errorPairs.contains(card)
                        val isSelected = selectedLeft == card
                        
                        val isDisabled = isMatched || isError

                        TileButton(text = text, isMatched = isMatched, isSelected = isSelected, isError = isError, isDisabled = isDisabled) {
                            if (!isDisabled) {
                                // Если в момент клика уже идет проверка (оба выбраны), игнорируем клик
                                if (selectedLeft != null && selectedRight != null) return@TileButton
                                
                                // Если мы кликнули на новую карточку, сразу назначаем её выбранной
                                selectedLeft = if (selectedLeft == card) null else card
                                
                                // И только если мы ВЫБРАЛИ карточку (а не сняли выделение)
                                // мы запускаем проверку
                                if (selectedLeft != null) {
                                    checkMatch()
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Правая колонка
                Column(modifier = Modifier.weight(1f)) {
                    rightItems.forEach { card ->
                        val text = if (isReverseMode) card.front else card.back
                        val isMatched = matched.contains(card)
                        val isError = errorPairs.contains(card)
                        val isSelected = selectedRight == card
                        
                        val isDisabled = isMatched || isError

                        TileButton(text = text, isMatched = isMatched, isSelected = isSelected, isError = isError, isDisabled = isDisabled) {
                            if (!isDisabled) {
                                // Если в момент клика уже идет проверка (оба выбраны), игнорируем клик
                                if (selectedLeft != null && selectedRight != null) return@TileButton
                                
                                selectedRight = if (selectedRight == card) null else card
                                
                                if (selectedRight != null) {
                                    checkMatch()
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Результаты (такие же, как в свайпах)
            val correctCount = currentCards.count { it.isKnown == true }
            val wrongCount = currentCards.count { it.isKnown == false }
            
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Тренировка завершена!", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Знаю: $correctCount | Не знаю: $wrongCount", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))
                if (wrongCount > 0) {
                    Button(
                        onClick = {
                            currentCards = currentCards.filter { it.isKnown == false }.onEach { it.isKnown = null }
                            onProgressChanged()
                            currentChunkIndex = 0
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
fun TileButton(text: String, isMatched: Boolean, isSelected: Boolean, isError: Boolean, isDisabled: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isError -> Color(0xFFE57373)   // Красный для ошибок
        isMatched -> Color(0xFF81C784) // Зеленый для правильных пар
        isSelected -> MaterialTheme.colorScheme.primaryContainer // Выделено (синий)
        else -> MaterialTheme.colorScheme.surfaceVariant // Обычный цвет
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(80.dp)
            .clickable(enabled = !isDisabled, onClick = onClick), // Отключаем клик, если isDisabled = true
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// Функция для парсинга CSV файла из папки assets
fun loadFlashcardsFromCsv(context: Context, filename: String): List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    try {
        // Открываем файл из папки assets
        val inputStream = context.assets.open(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))
        
        // Читаем первую строку (заголовки) и пропускаем её
        reader.readLine()
        
        // Читаем остальные строки
        var line: String? = reader.readLine()
        while (line != null) {
            // Разбиваем строку по запятой
            val parts = line.split(",")
            // Убеждаемся, что у нас есть и слово, и перевод
            if (parts.size >= 2) {
                val front = parts[0].trim()
                val back = parts[1].trim()
                flashcards.add(Flashcard(front = front, back = back))
            }
            line = reader.readLine()
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
        // В случае ошибки вернем карточку с сообщением об ошибке
        flashcards.add(Flashcard("Error", "Ошибка чтения файла $filename"))
    }
    return flashcards
}

fun loadFlashcardsFromUri(context: Context, uri: Uri): List<Flashcard> {
    val flashcards = mutableListOf<Flashcard>()
    try {
        // ContentResolver нужен для чтения файлов по Uri в Android
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine() // пропускаем заголовки
            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split(",")
                if (parts.size >= 2) {
                    val front = parts[0].trim()
                    val back = parts[1].trim()
                    flashcards.add(Flashcard(front = front, back = back))
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

// Функция для получения реального имени файла из Uri
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
    // Возвращаем реальное имя файла с расширением (если оно есть)
    return result ?: "unknown_file"
}

// Режимы обучения
enum class StudyMode { SWIPE, TILES }

// Данные для текущей сессии изучения
data class StudySession(
    val title: String,
    val cards: List<Flashcard>,
    val mode: StudyMode = StudyMode.SWIPE
)

// --- ФУНКЦИИ ДЛЯ ЛОКАЛЬНОГО СОХРАНЕНИЯ ПРОГРЕССА ---

fun saveBlocksToStorage(context: Context, blocks: List<FlashcardBlock>) {
    try {
        // Создаем файл во внутренней памяти приложения
        val file = File(context.filesDir, "flashcards_data.dat")
        // Записываем список как объект
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
            // Читаем объект из файла
            ObjectInputStream(file.inputStream()).use { it.readObject() as? List<FlashcardBlock> }
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}