package ru.netology.faceyoga.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: ExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(exercises: List<ExerciseEntity>): List<Long>

    @Query("SELECT id FROM exercises WHERE title = :title LIMIT 1")
    suspend fun getIdByTitle(title: String): Long?

    @Update
    suspend fun update(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises ORDER BY title")
    fun observeAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ExerciseEntity?
}

@Dao
interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(program: ProgramEntity): Long

    @Query("SELECT id FROM programs WHERE title = :title LIMIT 1")
    suspend fun getIdByTitle(title: String): Long?

    @Query("SELECT * FROM programs ORDER BY id DESC")
    fun observeAll(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs WHERE id = :programId LIMIT 1")
    fun observeById(programId: Long): Flow<ProgramEntity?>

    @Query("SELECT * FROM programs WHERE id = :programId LIMIT 1")
    suspend fun getById(programId: Long): ProgramEntity?
}

@Dao
interface ProgramDayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(day: ProgramDayEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(days: List<ProgramDayEntity>): List<Long>

    @Query(
        """
        SELECT id FROM program_days 
        WHERE programId = :programId AND dayNumber = :dayNumber 
        LIMIT 1
        """
    )
    suspend fun getId(programId: Long, dayNumber: Int): Long?

    // ✅ NEW: нужно, чтобы по programDayId получить programId (для сохранения прогресса в конце дня)
    @Query("SELECT programId FROM program_days WHERE id = :programDayId LIMIT 1")
    suspend fun getProgramIdByProgramDayId(programDayId: Long): Long?

    /**
     * Экран "Дни":
     * day + count упражнений + completed (по user_day_progress)
     * + NEW: doneCount (по user_exercise_progress)
     */
    @Query(
        """
        SELECT 
            d.id AS programDayId,
            d.dayNumber AS dayNumber,
            d.title AS title,

            COUNT(DISTINCT de.id) AS exercisesCount,

            -- NEW: сколько упражнений в этом дне выполнено
            COUNT(DISTINCT CASE WHEN uep.isCompleted THEN uep.exerciseId END) AS doneCount,

            CASE WHEN udp.isCompleted IS NULL THEN 0 ELSE udp.isCompleted END AS isCompleted
        FROM program_days d
        LEFT JOIN day_exercises de 
            ON de.programDayId = d.id

        LEFT JOIN user_exercise_progress uep
            ON uep.programId = d.programId AND uep.dayNumber = d.dayNumber

        LEFT JOIN user_day_progress udp 
            ON udp.programId = d.programId AND udp.dayNumber = d.dayNumber

        WHERE d.programId = :programId
        GROUP BY d.id, d.dayNumber, d.title, udp.isCompleted
        ORDER BY d.dayNumber
        """
    )
    fun observeDays(programId: Long): Flow<List<ProgramDayRow>>
}

@Dao
interface DayExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(link: DayExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(links: List<DayExerciseEntity>): List<Long>

    @Query(
        """
        DELETE FROM day_exercises
        WHERE programDayId IN (
            SELECT id FROM program_days WHERE programId = :programId
        )
        """
    )
    suspend fun deleteAllForProgram(programId: Long)

    /**
     * Экран "Упражнения дня": связки + сами упражнения
     */
    @Query(
        """
        SELECT
            de.id AS linkId,
            de.`order` AS `order`,
            de.overrideReps AS overrideReps,
            de.overrideSeconds AS overrideSeconds,
            e.id AS exerciseId,
            e.title AS title,
            e.zone AS zone,
            e.description AS description,
            e.type AS type,
            e.defaultReps AS defaultReps,
            e.defaultSeconds AS defaultSeconds,
            e.level AS level,
            e.videoUri AS videoUri,
            e.previewImageUri AS previewImageUri,
            e.requiresItem AS requiresItem,
            e.requiredItemKey AS requiredItemKey
        FROM day_exercises de
        INNER JOIN exercises e ON e.id = de.exerciseId
        WHERE de.programDayId = :programDayId
        ORDER BY de.`order`
        """
    )
    fun observeDayExercises(programDayId: Long): Flow<List<DayExerciseWithExercise>>
}

@Dao
interface ProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDayProgress(progress: UserDayProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExerciseProgress(progress: UserExerciseProgressEntity)

    @Query(
        """
        SELECT * FROM user_day_progress 
        WHERE programId = :programId AND dayNumber = :dayNumber 
        LIMIT 1
        """
    )
    suspend fun getDayProgress(programId: Long, dayNumber: Int): UserDayProgressEntity?

    @Query(
        """
        SELECT * FROM user_exercise_progress 
        WHERE programId = :programId AND dayNumber = :dayNumber
        """
    )
    suspend fun getExercisesProgress(programId: Long, dayNumber: Int): List<UserExerciseProgressEntity>

    @Query(
        """
        INSERT OR REPLACE INTO user_exercise_progress(programId, dayNumber, exerciseId, isCompleted)
        VALUES(:programId, :dayNumber, :exerciseId, :isCompleted)
        """
    )
    suspend fun insertOrReplaceExerciseProgress(
        programId: Long,
        dayNumber: Int,
        exerciseId: Long,
        isCompleted: Boolean
    )

    // ===== RESET PROGRESS =====

    @Query("DELETE FROM user_day_progress WHERE programId = :programId")
    suspend fun deleteDayProgressForProgram(programId: Long)

    @Query("DELETE FROM user_exercise_progress WHERE programId = :programId")
    suspend fun deleteExerciseProgressForProgram(programId: Long)

    @Transaction
    suspend fun resetProgressForProgram(programId: Long) {
        deleteExerciseProgressForProgram(programId)
        deleteDayProgressForProgram(programId)
    }
}
