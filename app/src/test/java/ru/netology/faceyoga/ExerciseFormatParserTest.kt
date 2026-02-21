package ru.netology.faceyoga

import org.junit.Assert.*
import org.junit.Test
import ru.netology.faceyoga.util.ExerciseFormatParser
import ru.netology.faceyoga.util.ExerciseFormatParser.Parsed

class ExerciseFormatParserTest {

    @Test
    fun parse_reps_formats() {
        assertEquals(Parsed.Reps(10), ExerciseFormatParser.parse("x10"))
        assertEquals(Parsed.Reps(10), ExerciseFormatParser.parse("x 10"))
        assertEquals(Parsed.Reps(10), ExerciseFormatParser.parse("10x"))
        assertEquals(Parsed.Reps(10), ExerciseFormatParser.parse("10 x"))
    }

    @Test
    fun parse_minutes_formats() {
        assertEquals(Parsed.Minutes(5), ExerciseFormatParser.parse("5 min"))
        assertEquals(Parsed.Minutes(5), ExerciseFormatParser.parse("5 minutes"))
        assertEquals(Parsed.Minutes(5), ExerciseFormatParser.parse("5 минут"))
        assertEquals(Parsed.Minutes(5), ExerciseFormatParser.parse("5 мин"))
    }

    @Test
    fun parse_seconds_formats() {
        assertEquals(Parsed.Seconds(90), ExerciseFormatParser.parse("90 sec"))
        assertEquals(Parsed.Seconds(90), ExerciseFormatParser.parse("90 seconds"))
        assertEquals(Parsed.Seconds(90), ExerciseFormatParser.parse("90 секунд"))
        assertEquals(Parsed.Seconds(90), ExerciseFormatParser.parse("90 сек"))
    }

    @Test
    fun parse_invalid_returns_null() {
        assertNull(ExerciseFormatParser.parse(null))
        assertNull(ExerciseFormatParser.parse(""))
        assertNull(ExerciseFormatParser.parse("abc"))
        assertNull(ExerciseFormatParser.parse("x0"))
        assertNull(ExerciseFormatParser.parse("-5 min"))
    }
}