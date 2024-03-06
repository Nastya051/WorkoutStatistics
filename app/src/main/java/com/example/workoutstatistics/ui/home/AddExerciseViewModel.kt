package com.example.workoutstatistics.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.workoutstatistics.DaoStatistics
import com.example.workoutstatistics.Exercise
import com.example.workoutstatistics.MainActivity
import com.example.workoutstatistics.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


class AddExerciseViewModel(val dao: DaoStatistics) : ViewModel() {

    suspend fun addWorkout(date: String){
        viewModelScope.launch {
//            val formatter = SimpleDateFormat("dd.MM.yyyy")
//            val dateRes = formatter.format(date)
            val work = Workout(null, date)
            dao.insertWorkout(work)
        }
    }

    fun addExercise(exer: Exercise){
        viewModelScope.launch {
            dao.insertExercise(exer)
        }
    }

    suspend fun getLast(): Workout{
        val userProfile = runBlocking(Dispatchers.IO) {
            dao.getLastWorkout()
        }
        return userProfile
    }


}