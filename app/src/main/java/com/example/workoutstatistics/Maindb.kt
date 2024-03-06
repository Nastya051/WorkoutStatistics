package com.example.workoutstatistics

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = arrayOf(Exercise::class, Workout::class)/*[Exercise::class], [Workout::class]*/, version = 2, exportSchema = true)
abstract class Maindb : RoomDatabase() {
    abstract fun getDao(): DaoStatistics //получаем интерфейс

    companion object{
        fun getdb(context: Context): Maindb{
            return Room.databaseBuilder(context.applicationContext, Maindb::class.java, "statistic.db").build()
        }
    }
}