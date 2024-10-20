package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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

class Registro : AppCompatActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        registerBtn = findViewById(R.id.RegisterBtn)

        registerBtn.setOnClickListener {
            val nombre = findViewById<TextInputEditText>(R.id.Nombre).text.toString().trim()
            val email = findViewById<TextInputEditText>(R.id.emailInput).text.toString().trim()
            val password =
                findViewById<TextInputEditText>(R.id.passwordInput).text.toString().trim()
            val repeatPassword =
                findViewById<TextInputEditText>(R.id.repeatPassword).text.toString().trim()
            val progressBar = findViewById<ProgressBar>(R.id.progress)

            progressBar.visibility = ProgressBar.VISIBLE

            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(this, "Por favor, ingresa un nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Por favor, ingresa un correo.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Por favor, ingresa una contraseña.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(repeatPassword)) {
                Toast.makeText(this, "Por favor, repite la contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != repeatPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("Registro", "Nombre: $nombre, Email: $email, Password: $password")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = ProgressBar.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuario creado!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "ERROR: autentificación fallida", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}