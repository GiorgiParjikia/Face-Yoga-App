package ru.netology.faceyoga.data.db

import androidx.room.TypeConverter
import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

class Converters {

    @TypeConverter
    fun fromZone(value: Zone): String = value.name

    @TypeConverter
    fun toZone(value: String): Zone = Zone.valueOf(value)

    @TypeConverter
    fun fromExerciseType(value: ExerciseType): String = value.name

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType = ExerciseType.valueOf(value)
}
