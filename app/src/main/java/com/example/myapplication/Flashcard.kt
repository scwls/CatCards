package com.example.myapplication

import java.io.Serializable

data class Flashcard(
    val front: String,
    val back: String,
    var isKnown: Boolean? = null
) : Serializable

// Новый класс для хранения "колоды" или "блока" карточек
data class FlashcardBlock(
    val name: String, // Название блока (например, "Животные")
    val cards: List<Flashcard> // Список карточек в этом блоке
) : Serializable
