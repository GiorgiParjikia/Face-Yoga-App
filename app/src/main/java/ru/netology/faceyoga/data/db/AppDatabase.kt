package ru.netology.faceyoga.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        ProgramDayEntity::class,
        DayExerciseEntity::class,
        UserDayProgressEntity::class,
        UserExerciseProgressEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programDao(): ProgramDao
    abstract fun programDayDao(): ProgramDayDao
    abstract fun dayExerciseDao(): DayExerciseDao
    abstract fun progressDao(): ProgressDao
}
