package com.bignerdranch.android.kalinovskayasmartwatch_v3

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import kotlin.jvm.Volatile

@Database(
    entities = [User::class, DailyCalories::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyCaloriesDao(): DailyCaloriesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calorie_tracker.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}