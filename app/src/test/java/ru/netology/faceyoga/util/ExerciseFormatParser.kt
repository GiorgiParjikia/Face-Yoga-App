package ru.netology.faceyoga.util

/**
 * Парсит строку формата:
 *  - "x10" / "10x" / "x 10" / "10 x" -> Reps(10)
 *  - "5 min" / "5 mins" / "5 minutes" / "5 минут" -> Minutes(5)
 *  - "90 sec" / "90 seconds" / "90 секунд" -> Seconds(90)
 *
 * Возвращает null, если формат не распознан.
 */
object ExerciseFormatParser {

    sealed class Parsed {
        data class Reps(val count: Int) : Parsed()
        data class Minutes(val minutes: Int) : Parsed()
        data class Seconds(val seconds: Int) : Parsed()
    }

    fun parse(input: String?): Parsed? {
        val s = input?.trim()?.lowercase() ?: return null
        if (s.isBlank()) return null

        // x10 / 10x (повторы)
        val reps = Regex("""^\s*(?:x\s*(\d+)|(\d+)\s*x)\s*$""").find(s)
        if (reps != null) {
            val n = (reps.groupValues[1].ifBlank { reps.groupValues[2] }).toIntOrNull()
            if (n != null && n > 0) return Parsed.Reps(n)
        }

        // minutes
        val mins = Regex("""^\s*(\d+)\s*(min|mins|minute|minutes|м|мин|минута|минут|минуты)\s*$""").find(s)
        if (mins != null) {
            val n = mins.groupValues[1].toIntOrNull()
            if (n != null && n > 0) return Parsed.Minutes(n)
        }

        // seconds
        val secs = Regex("""^\s*(\d+)\s*(sec|secs|second|seconds|с|сек|секунда|секунд|секунды)\s*$""").find(s)
        if (secs != null) {
            val n = secs.groupValues[1].toIntOrNull()
            if (n != null && n > 0) return Parsed.Seconds(n)
        }

        return null
    }
}