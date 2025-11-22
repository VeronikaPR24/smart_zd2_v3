package com.bignerdranch.android.kalinovskayasmartwatch_v3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyCaloriesDao {
    @Insert
    suspend fun insertCalories(dailyCalories: DailyCalories)

    @Query("SELECT * FROM daily_calories WHERE userId = :userId AND date = :date")
    suspend fun getCaloriesByDate(userId: Long, date: String): DailyCalories?

    @Query("UPDATE daily_calories SET calories = calories + :additionalCalories WHERE userId = :userId AND date = :date")
    suspend fun addCalories(userId: Long, date: String, additionalCalories: Int)

    @Query("SELECT SUM(calories) FROM daily_calories WHERE userId = :userId AND date = :date")
    suspend fun getTotalCaloriesForDate(userId: Long, date: String): Int?
}