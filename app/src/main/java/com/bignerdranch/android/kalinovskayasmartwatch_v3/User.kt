package com.bignerdranch.android.kalinovskayasmartwatch_v3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val createdAt: Long = System.currentTimeMillis()
)