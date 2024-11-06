package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.app_bases_datos.utils.añadirLugar
import com.example.app_bases_datos.utils.eliminarLugar
import com.example.app_bases_datos.utils.verificarLugarFavorito

class Detalles_de_lugares : AppCompatActivity() {

    lateinit var backBtn: ImageButton
    private lateinit var ID_LUGAR: String
    private lateinit var ID_USUARIO: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_de_lugares)

        backBtn = findViewById(R.id.boton_cerrar)

        val nombre = intent.getStringExtra("nombre")
        val direccion = intent.getStringExtra("direccion")
        val descripcion = intent.getStringExtra("descripcion")
        val imagenUrl = intent.getStringExtra("imagenURL")
        val tiempoVisita = intent.getIntExtra("tiempo", 0)
        val precio = intent.getIntExtra("precio", 0)

        ID_LUGAR = intent.getStringExtra("id").toString()
        ID_USUARIO = intent.getStringExtra("ID_USUARIO") ?: ""

        findViewById<TextView>(R.id.tvNombreLugar).text = nombre
        findViewById<TextView>(R.id.tvDireccionLugar).text = direccion
        findViewById<TextView>(R.id.tvDescripcionLugar).text = descripcion
        val precioLugar = findViewById<TextView>(R.id.tvPrecioLugar)
        val horas = tiempoVisita / 60.0
        val tiempoTexto = String.format("%.1f horas", horas)
        findViewById<TextView>(R.id.tvTiempoLugar).text = tiempoTexto

        precioLugar.text = if (precio == 0) "Gratis" else "$${precio}"

        val likeOff = findViewById<ImageView>(R.id.LikeOff)
        val likeOn = findViewById<ImageView>(R.id.LikeOn)
        val btnGuardar = findViewById<ImageView>(R.id.ivGuardar)

        verificarLugarFavorito(ID_USUARIO, ID_LUGAR) { esFavorito ->
            if (esFavorito) {
                likeOff.visibility = View.INVISIBLE
                likeOn.visibility = View.VISIBLE
            } else {
                likeOff.visibility = View.VISIBLE
                likeOn.visibility = View.INVISIBLE
            }
        }

        btnGuardar.setOnClickListener {
            val intent = Intent(this, GuardarEnCategoria::class.java)
            intent.putExtra("id", ID_LUGAR)
            intent.putExtra("ID_USUARIO", ID_USUARIO)
            startActivityForResult(intent, 1)
        }

        likeOn.setOnClickListener {
            likeOn.visibility = View.INVISIBLE
            likeOff.visibility = View.VISIBLE
            eliminarLugar(ID_USUARIO, ID_LUGAR)
        }

        likeOff.setOnClickListener {
            likeOff.visibility = View.INVISIBLE
            likeOn.visibility = View.VISIBLE
            añadirLugar(ID_USUARIO, ID_LUGAR)
        }

        backBtn.setOnClickListener {
            this.onBackPressed()
        }

        Glide.with(this).load(imagenUrl).into(findViewById(R.id.ivFotoLugar))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            recreate()
        }
    }
}
