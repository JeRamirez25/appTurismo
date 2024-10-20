package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginBtn = findViewById(R.id.loginBtn)
        registerBtn = findViewById(R.id.registrarBtn)

        loginBtn.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progress)
            val email = findViewById<TextInputEditText>(R.id.emailInput).text.toString().trim()
            val password =
                findViewById<TextInputEditText>(R.id.passwordInput).text.toString().trim()

            progressBar.visibility = ProgressBar.VISIBLE

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Por favor, ingresa un correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Por favor, ingresa una contraseña.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = ProgressBar.GONE
                    if (task.isSuccessful) {
                        try {
                            Toast.makeText(this, "Inicio de sesión correcto.", Toast.LENGTH_SHORT)
                                .show()
                            Intent(this, MainActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Error al cambiar de actividad: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(this, "ERROR: Falló inicio de sesión", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
        registerBtn.setOnClickListener {
            try {
                Intent(this, Registro::class.java).also {
                    startActivity(it)
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error al cambiar de actividad: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}