package com.example.workoutstatistics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    var idWorkout: Int?,
    @ColumnInfo(name = "Date")
    var date: String
) {
}
