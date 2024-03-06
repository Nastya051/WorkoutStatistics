package com.example.workoutstatistics.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutstatistics.DaoStatistics

class TableViewModelFactory(private val dao: DaoStatistics): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TableViewModel::class.java)) {
            return TableViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}