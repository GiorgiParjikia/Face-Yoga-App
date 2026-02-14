package ru.netology.faceyoga.data.util

fun Map<String, String>.pick(lang: String): String =
    this[lang] ?: this["en"] ?: this["ru"] ?: values.firstOrNull().orEmpty()
