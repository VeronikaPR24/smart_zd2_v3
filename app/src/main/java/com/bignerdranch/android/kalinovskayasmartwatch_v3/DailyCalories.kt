package com.bignerdranch.android.kalinovskayasmartwatch_v3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_calories")
data class DailyCalories(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val date: String,
    val calories: Int,
    val timestamp: Long = System.currentTimeMillis()
)