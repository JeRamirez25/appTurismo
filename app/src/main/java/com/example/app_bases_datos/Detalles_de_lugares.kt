package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class Detalles_de_lugares : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_de_lugares)

            val lugarId = intent.getStringExtra("id")
            Log.d("ID_Lugar_Detalle", "ID recibido en Detalles_de_lugares: $lugarId")

            val nombre = intent.getStringExtra("nombre")
            val direccion = intent.getStringExtra("direccion")
            val descripcion = intent.getStringExtra("descripcion")
            val imagenUrl = intent.getStringExtra("imagenURL")
            val tiempoVisita = intent.getIntExtra("tiempo", 0)
            val precio = intent.getIntExtra("precio", 0)

            findViewById<TextView>(R.id.tvNombreLugar).text = nombre
            findViewById<TextView>(R.id.tvDireccionLugar).text = direccion
            findViewById<TextView>(R.id.tvDescripcionLugar).text = descripcion

            val precioLugar = findViewById<TextView>(R.id.tvPrecioLugar)

            val horas = tiempoVisita / 60.0
            val tiempoTexto = String.format("%.1f horas", horas)

            findViewById<TextView>(R.id.tvTiempoLugar).text = tiempoTexto

            if (precio == 0) {
                precioLugar.text = "Gratis"
            } else {
                precioLugar.text = "$${precio}" // Muestra el precio con el símbolo de moneda
            }

            // Cargar la imagen usando Glide u otra librería
            Glide.with(this).load(imagenUrl).into(findViewById(R.id.ivFotoLugar))
        }
    }
