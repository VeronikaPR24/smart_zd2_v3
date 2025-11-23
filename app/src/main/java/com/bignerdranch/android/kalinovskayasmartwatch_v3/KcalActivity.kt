package com.bignerdranch.android.kalinovskayasmartwatch_v3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KcalActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var tvTotalCalories: TextView
    private lateinit var inputKcal: EditText
    private lateinit var btnPlus: Button
    private var currentUser: User? = null
    private var todayCalories: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kcal)
        databaseHelper = DatabaseHelper(this)
        initViews()
        loadUserAndCalories()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initViews() {
        tvTotalCalories = findViewById(R.id.tvTotalCalories)
        inputKcal = findViewById(R.id.InputKcal)
        btnPlus = findViewById(R.id.btnPlus)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnPlus.setOnClickListener {
            addCalories()
        }
        inputKcal.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addCalories()
                true
            } else {
                false
            }
        }
    }

    private fun loadUserAndCalories() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = databaseHelper.getLastUser()
            withContext(Dispatchers.Main) {
                if (user == null) {
                    startActivity(Intent(this@KcalActivity, SignIn::class.java))
                    finish()
                } else {
                    currentUser = user
                    loadTodayCalories(user.id)
                }
            }
        }
    }

    private fun loadTodayCalories(userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            todayCalories = databaseHelper.getTodayCalories(userId)
            withContext(Dispatchers.Main) {
                updateCaloriesDisplay()
            }
        }
    }

    private fun addCalories() {
        val caloriesText = inputKcal.text.toString().trim()
        if (caloriesText.isNotEmpty()) {
            val calories = caloriesText.toIntOrNull()
            if (calories != null && calories > 0) {
                val user = currentUser
                if (user != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        databaseHelper.addCalories(user.id, calories)
                        withContext(Dispatchers.Main) {
                            todayCalories += calories
                            updateCaloriesDisplay()
                            inputKcal.text.clear()
                        }
                    }
                }
            }
        }
    }

    private fun updateCaloriesDisplay() {
        tvTotalCalories.text = todayCalories.toString()
    }

    fun Back(view: View) {
        val intent = Intent(this@KcalActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
