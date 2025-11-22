package com.bignerdranch.android.kalinovskayasmartwatch_v3

import android.content.Context

class DatabaseHelper(private val context: Context) {
    private val database: AppDatabase = AppDatabase.getInstance(context)
    private val repository: CaloriesRepository = CaloriesRepository(database.userDao(), database.dailyCaloriesDao())

    suspend fun insertUser(user: User): Long = repository.insertUser(user)

    suspend fun getLastUser(): User? = repository.getLastUser()

    suspend fun addCalories(userId: Long, calories: Int) = repository.addCalories(userId, calories)

    suspend fun getTodayCalories(userId: Long): Int = repository.getTodayCalories(userId)

    fun calculateDailyNorm(user: User): Int = repository.calculateDailyNorm(user)
}