package com.example.workoutstatistics.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutstatistics.DaoStatistics
import com.example.workoutstatistics.Exercise
import com.example.workoutstatistics.ExercisesInWorkout
import com.example.workoutstatistics.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TableViewModel(val dao: DaoStatistics) : ViewModel() {

    suspend fun getExWorks(): List<ExercisesInWorkout> {
        val userProfile = runBlocking(Dispatchers.IO) {
            dao.getWorkoutsWithExers()
        }
        return userProfile
    }

    suspend fun getCntWorks(): Int {
        val userProfile = runBlocking(Dispatchers.IO) {
            dao.getCountWorkouts()
        }
        return userProfile
    }
}