package com.bignerdranch.android.kalinovskayasmartwatch_v3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignIn : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        databaseHelper = DatabaseHelper(this)
        initViews()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun initViews() {
        nameEditText = findViewById(R.id.name)
        ageEditText = findViewById(R.id.password)
        weightEditText = findViewById(R.id.ves)
        heightEditText = findViewById(R.id.height)
        genderRadioGroup = findViewById(R.id.rgGender)
        nameEditText.requestFocus()
    }

    fun Registr(view: View) {
        val name = nameEditText.text.toString().trim()
        val ageText = ageEditText.text.toString().trim()
        val weightText = weightEditText.text.toString().trim()
        val heightText = heightEditText.text.toString().trim()
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId

        if (name.isEmpty() || ageText.isEmpty() || weightText.isEmpty() || heightText.isEmpty() || selectedGenderId == -1) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }
        val age = ageText.toIntOrNull()
        val weight = weightText.toDoubleOrNull()
        val height = heightText.toDoubleOrNull()
        if (age == null || age <= 0 || age > 150) {
            Toast.makeText(this, "Введите корректный возраст", Toast.LENGTH_SHORT).show()
            return
        }

        if (weight == null || weight <= 0 || weight > 500) {
            Toast.makeText(this, "Введите корректный вес", Toast.LENGTH_SHORT).show()
            return
        }

        if (height == null || height <= 0 || height > 300) {
            Toast.makeText(this, "Введите корректный рост", Toast.LENGTH_SHORT).show()
            return
        }
        val gender = when (selectedGenderId) {
            R.id.rbMale -> "male"
            R.id.rbFemale -> "female"
            else -> "male"
        }
        val user = User(
            name = name,
            gender = gender,
            age = age,
            weight = weight,
            height = height
        )
        CoroutineScope(Dispatchers.IO).launch {
            databaseHelper.insertUser(user)
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@SignIn, MainActivity::class.java))
                finish()
            }
        }
    }
}