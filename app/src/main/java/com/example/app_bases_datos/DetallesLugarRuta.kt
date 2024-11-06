package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetallesLugarRuta : AppCompatActivity() {

    lateinit var backBtn : ImageButton

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalles_del_lugar_ruta)

        backBtn= findViewById(R.id.boton_cerrar1)

        val nombre = intent.getStringExtra("nombre")
        val direccion = intent.getStringExtra("direccion")
        val descripcion = intent.getStringExtra("descripcion")
        val precio = intent.getIntExtra("precio", 0)
        val tiempo = intent.getIntExtra("tiempo", 0)
        val imagenURL = intent.getStringExtra("imagenURL")

        val nombreDetallesRuta: TextView = findViewById(R.id.detallesNombreLugar)
        val direccionDetallesRuta: TextView = findViewById(R.id.detallesDireccionLugar)
        val descripcionDetallesRuta: TextView = findViewById(R.id.detallesDescripcionLugar)
        val precioDetallesRuta: TextView = findViewById(R.id.detallesPrecioLugar)
        val tiempoDetallesRuta: TextView = findViewById(R.id.detallesTiempoLugar)
        val imagenDetallesRuta: ImageView = findViewById(R.id.detallesRutaFoto)

        if (precio == 0) {
            precioDetallesRuta.text = "Gratis"
        } else {
            precioDetallesRuta.text = "$${precio}"
        }

        nombreDetallesRuta.text = nombre
        direccionDetallesRuta.text = direccion
        descripcionDetallesRuta.text = descripcion
        tiempoDetallesRuta.text = tiempo.toString()
        precioDetallesRuta.text = precio.toString()

        backBtn.setOnClickListener{
            this.onBackPressed()
        }

        Glide.with(this).load(imagenURL).into(imagenDetallesRuta)
    }
}