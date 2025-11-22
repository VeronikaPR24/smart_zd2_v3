package com.bignerdranch.android.kalinovskayasmartwatch_v3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NormalKcal : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var tvUserName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvGender: TextView
    private lateinit var btnCalculate: Button

    private var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_normal_kcal)
        databaseHelper = DatabaseHelper(this)
        initViews()
        loadUserData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvAge = findViewById(R.id.tvAge)
        tvWeight = findViewById(R.id.tvWeight)
        tvHeight = findViewById(R.id.tvHeight)
        tvGender = findViewById(R.id.tvGender)
        btnCalculate = findViewById(R.id.btnCalculate)

        btnCalculate.setOnClickListener {
            calculateAndShowResults()
        }
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = databaseHelper.getLastUser()
            withContext(Dispatchers.Main) {
                if (user == null) {
                    startActivity(Intent(this@NormalKcal, SignIn::class.java))
                    finish()
                } else {
                    currentUser = user
                    displayUserInfo(user)
                }
            }
        }
    }

    private fun displayUserInfo(user: User) {
        tvUserName.text = user.name
        tvAge.text = "${user.age} лет"
        tvWeight.text = "${user.weight} кг"
        tvHeight.text = "${user.height} см"
        tvGender.text = if (user.gender == "male") "Мужской" else "Женский"
    }

    private fun calculateAndShowResults() {
        val user = currentUser ?: return
        CoroutineScope(Dispatchers.IO).launch {
            val todayCalories = databaseHelper.getTodayCalories(user.id)
            val dailyNorm = databaseHelper.calculateDailyNorm(user)
            withContext(Dispatchers.Main) {
                showResultsDialog(todayCalories, dailyNorm)
            }
        }
    }

    private fun showResultsDialog(todayCalories: Int, dailyNorm: Int) {
        val message = buildString {
            append("Ваша базовая норма калорий: $dailyNorm ккал/день\n")
            append("Вы употребили сегодня: $todayCalories ккал\n\n")
            when {
                todayCalories > dailyNorm -> {
                    val excess = todayCalories - dailyNorm
                    append("Вы превысили норму на $excess ккал")
                }
                todayCalories < dailyNorm -> {
                    val remaining = dailyNorm - todayCalories
                    append("Вам осталось употребить $remaining ккал")
                }
                else -> {
                    append("Вы употребили ровно свою норму!")
                }
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Ваша норма калорий")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    fun Back(view: View) {
        val intent = Intent(this@NormalKcal, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}