package com.example.workoutstatistics.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutstatistics.DaoStatistics

class AddExerViewModelFactory(private val dao: DaoStatistics): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExerciseViewModel::class.java)) {
            return AddExerciseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}