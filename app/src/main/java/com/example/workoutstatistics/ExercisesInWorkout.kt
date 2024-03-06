package com.example.workoutstatistics

import androidx.room.Embedded
import androidx.room.Relation

data class ExercisesInWorkout(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "idWorkout",
        entityColumn = "NumWorkout"
    )
    val exercises: List<Exercise>
)
