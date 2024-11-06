package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_bases_datos.utils.crearRuta
import com.example.app_bases_datos.utils.obtenerIdUsuario
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.util.Log

class CrearRutaNueva : AppCompatActivity() {
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.crear_ruta_nueva_vacia)

        // Obtener usuario autenticado
        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email ?: ""

            // Obtener el ID del usuario a partir del correo
            obtenerIdUsuario(userEmail) { idUsuario ->
                if (idUsuario != null) {
                    val backBtn = findViewById<ImageButton>(R.id.imageButton2)
                    val editTextText = findViewById<TextInputEditText>(R.id.editTextText)
                    val safeBtn = findViewById<Button>(R.id.safeBtn)

                    // Volver
                    backBtn.setOnClickListener {
                        val intent = Intent(this, GuardarEnCategoria::class.java)
                        startActivity(intent)
                        finish()
                    }

                    safeBtn.setOnClickListener {
                        val nombreRuta = editTextText.text.toString().trim()

                        if (nombreRuta.isNotEmpty()) {
                            crearRuta(idUsuario, nombreRuta) { rutaId ->
                                Log.d("INFO", "Ruta '$nombreRuta' creada con éxito!")

                                val intent = Intent(this, Detalles_de_lugares::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Log.d("INFO", "Por favor, ingresa un nombre para la ruta.")
                        }
                    }
                } else {
                    Log.d("INFO", "Error al obtener el ID del usuario.")
                }
            }
        } else {
            Log.d("INFO", "Por favor, inicia sesión primero.")
        }
    }
}
