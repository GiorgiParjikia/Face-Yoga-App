package ru.netology.faceyoga.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.faceyoga.data.db.AppDatabase
import ru.netology.faceyoga.data.db.DayExerciseDao
import ru.netology.faceyoga.data.db.ExerciseDao
import ru.netology.faceyoga.data.db.ProgramDao
import ru.netology.faceyoga.data.db.ProgramDayDao
import ru.netology.faceyoga.data.db.ProgressDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "faceyoga.db")
            // на диплом нормально, миграции добавим позже
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()
    @Provides fun provideProgramDao(db: AppDatabase): ProgramDao = db.programDao()
    @Provides fun provideProgramDayDao(db: AppDatabase): ProgramDayDao = db.programDayDao()
    @Provides fun provideDayExerciseDao(db: AppDatabase): DayExerciseDao = db.dayExerciseDao()
    @Provides fun provideProgressDao(db: AppDatabase): ProgressDao = db.progressDao()
}