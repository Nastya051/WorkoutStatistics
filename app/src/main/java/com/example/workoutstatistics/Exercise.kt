package com.example.workoutstatistics

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "Exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "Name")
    var name: String,
    @ColumnInfo(name = "Kg")
    var weigth: Double,
    @ColumnInfo(name = "Repeat")
    var repeat: Int,
    @ColumnInfo(name = "Negative")
    var negative: Int,
    @ColumnInfo(name = "NumWorkout")
    var num: Int
)


