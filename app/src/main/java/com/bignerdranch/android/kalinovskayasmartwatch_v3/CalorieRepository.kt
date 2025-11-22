package com.bignerdranch.android.kalinovskayasmartwatch_v3

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaloriesRepository(private val userDao: UserDao, private val dailyCaloriesDao: DailyCaloriesDao) {

    suspend fun insertUser(user: User): Long = userDao.insertUser(user)

    suspend fun getLastUser(): User? = userDao.getLastUser()

    suspend fun addCalories(userId: Long, calories: Int) {
        val currentDate = getCurrentDate()
        val existingRecord = dailyCaloriesDao.getCaloriesByDate(userId, currentDate)

        if (existingRecord == null) {
            dailyCaloriesDao.insertCalories(DailyCalories(userId = userId, date = currentDate, calories = calories))
        } else {
            dailyCaloriesDao.addCalories(userId, currentDate, calories)
        }
    }

    suspend fun getTodayCalories(userId: Long): Int {
        val currentDate = getCurrentDate()
        return dailyCaloriesDao.getTotalCaloriesForDate(userId, currentDate) ?: 0
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun calculateDailyNorm(user: User): Int {
        val bmr = if (user.gender.lowercase() == "male") {
            10 * user.weight + 6.25 * user.height - 5 * user.age + 5
        } else {
            10 * user.weight + 6.25 * user.height - 5 * user.age - 161
        }

        return bmr.toInt()
    }
}