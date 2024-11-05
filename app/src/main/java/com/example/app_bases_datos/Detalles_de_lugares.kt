package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app_bases_datos.utils.añadirLugar
import com.example.app_bases_datos.utils.eliminarLugar
import com.bumptech.glide.Glide
import com.example.app_bases_datos.utils.obtenerIdUsuario
import com.example.app_bases_datos.utils.verificarLugarFavorito

class Detalles_de_lugares : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_de_lugares)

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

            // Likes
            val likeOff = findViewById<ImageView>(R.id.LikeOff)
            val likeOn = findViewById<ImageView>(R.id.LikeOn)
            val ID_LUGAR = intent.getStringExtra("id").toString()
            var ID_USUARIO = intent.getStringExtra("ID_USUARIO") ?: ""

            verificarLugarFavorito(ID_USUARIO, ID_LUGAR) { esFavorito ->
                if (esFavorito) {
                    Log.d("Verificación", "El lugar es favorito del usuario.")
                    likeOff.visibility = View.INVISIBLE
                    likeOn.visibility = View.VISIBLE
                } else {
                    Log.d("Verificación", "El lugar NO es favorito del usuario.")
                    likeOff.visibility = View.VISIBLE
                    likeOn.visibility = View.INVISIBLE
                }
            }

            likeOn.setOnClickListener {
                likeOn.visibility = View.INVISIBLE
                likeOff.visibility = View.VISIBLE
                eliminarLugar(ID_USUARIO,ID_LUGAR)
            }

            likeOff.setOnClickListener {
                likeOff.visibility = View.INVISIBLE
                likeOn.visibility = View.VISIBLE
                añadirLugar(ID_USUARIO,ID_LUGAR)
            }

            // Cargar la imagen usando Glide u otra librería
            Glide.with(this).load(imagenUrl).into(findViewById(R.id.ivFotoLugar))
        }
    }
