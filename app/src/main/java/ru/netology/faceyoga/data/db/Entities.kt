package ru.netology.faceyoga.data.db

import androidx.room.*
import ru.netology.faceyoga.data.model.ExerciseType
import ru.netology.faceyoga.data.model.Zone

@Entity(
    tableName = "exercises",
    indices = [
        Index(value = ["zone"]),
        Index(value = ["title"], unique = true)
    ]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val zone: Zone,
    val description: String,
    val type: ExerciseType,
    val defaultReps: Int? = null,
    val defaultSeconds: Int? = null,
    val level: Int? = null,
    val videoUri: String? = null,
    val previewImageUri: String? = null,
)

@Entity(
    tableName = "programs",
    indices = [Index(value = ["title"], unique = true)]
)
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val durationDays: Int,
    val level: Int? = null,
    val coverImageUri: String? = null,
)

@Entity(
    tableName = "program_days",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["programId"]),
        Index(value = ["programId", "dayNumber"], unique = true)
    ]
)
data class ProgramDayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programId: Long,
    val dayNumber: Int, // 1..30
    val title: String? = null,
)

@Entity(
    tableName = "day_exercises",
    foreignKeys = [
        ForeignKey(
            entity = ProgramDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["programDayId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["programDayId"]),
        Index(value = ["exerciseId"]),
        Index(value = ["programDayId", "order"], unique = true)
    ]
)
data class DayExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programDayId: Long,
    val exerciseId: Long,
    val order: Int, // 1..N
    val overrideReps: Int? = null,
    val overrideSeconds: Int? = null,
)

@Entity(
    tableName = "user_day_progress",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["programId"]),
        Index(value = ["programId", "dayNumber"], unique = true)
    ]
)
data class UserDayProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programId: Long,
    val dayNumber: Int,
    val isCompleted: Boolean,
    val completedAt: Long? = null
)

@Entity(
    tableName = "user_exercise_progress",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["programId", "dayNumber"]),
        Index(value = ["exerciseId"]),
        Index(value = ["programId", "dayNumber", "exerciseId"], unique = true)
    ]
)
data class UserExerciseProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val programId: Long,
    val dayNumber: Int,
    val exerciseId: Long,
    val isCompleted: Boolean
)