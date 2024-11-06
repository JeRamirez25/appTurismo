package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CrearRutaNueva : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.crear_ruta_nueva_vacia)

        val backBtn = findViewById<ImageButton>(R.id.imageButton2)

        backBtn.setOnClickListener{
            val intent = Intent(this, GuardarEnCategoria::class.java)
            startActivity(intent)
            finish()
        }
    }
}