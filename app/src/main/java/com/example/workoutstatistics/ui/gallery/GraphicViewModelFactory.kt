package com.example.workoutstatistics.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutstatistics.DaoStatistics

class GraphicViewModelFactory(private val dao: DaoStatistics): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GraphicViewModel::class.java)) {
            return GraphicViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}