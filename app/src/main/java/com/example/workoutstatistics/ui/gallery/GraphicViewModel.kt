package com.example.workoutstatistics.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutstatistics.DaoStatistics
import com.example.workoutstatistics.Exercise
import com.example.workoutstatistics.ExercisesInWorkout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class GraphicViewModel(val dao: DaoStatistics) : ViewModel() {

    suspend fun getExWorks(): List<ExercisesInWorkout> {
        val userProfile = runBlocking(Dispatchers.IO) {
            dao.getWorkoutsWithExers()
        }
        return userProfile
    }

    suspend fun getExByName(name: String/*, num: Int*/): List<Exercise>{
        val listEx = runBlocking(Dispatchers.IO){
            dao.getExercisesByName(name/*, num*/)
        }
        return listEx
    }
}