package com.example.workoutstatistics

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow


@Dao
interface DaoStatistics {

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Delete
     fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM Exercises")
     fun clearExercises()

    @Query("DELETE FROM Workout")
     fun clearWorkouts()

    @Query("SELECT * FROM Exercises")
     fun getAllExercises(): LiveData<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM Workout")
    suspend fun getWorkoutsWithExers():  List<ExercisesInWorkout>

    @Query("SELECT * FROM Exercises WHERE NumWorkout = :num")
    suspend fun getExercisesForWorkout(num: Int):  List<Exercise>

    @Query("SELECT * FROM Exercises WHERE Name = :name")// AND NumWorkout = :num")
    suspend fun getExercisesByName(name: String/*, num: Int*/):  List<Exercise>

     @Query("SELECT * FROM Workout WHERE idWorkout = (SELECT MAX(idWorkout) FROM Workout)")
      suspend fun getLastWorkout(): Workout

    @Query("SELECT COUNT(idWorkout) FROM Workout")
    suspend fun getCountWorkouts(): Int
}