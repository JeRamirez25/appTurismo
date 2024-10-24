package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private lateinit var emailInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailInput = findViewById(R.id.Usuario)
        loginBtn = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            Log.d("LoginActivity", "Correo a pasar: $email") //
            Intent(this, MainActivity::class.java).also { intent ->
                intent.putExtra("EMAIL", email)
                startActivity(intent)
                finish()
            }
        }
    }
}