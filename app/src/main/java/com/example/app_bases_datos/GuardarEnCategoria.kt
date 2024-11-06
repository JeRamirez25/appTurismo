package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.añadirLugarRuta
import com.example.app_bases_datos.utils.eliminarLugarRuta
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GuardarEnCategoria : AppCompatActivity() {

    private val db = Firebase.firestore
    private val listaRutaFavoritasId = mutableListOf<String>()
    private val listaRutas = mutableListOf<RutaModelo>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnGuardarLugar: Button
    private lateinit var btnCrearRuta: Button
    private val rutasSeleccionadas = mutableMapOf<String, Boolean>()
    private lateinit var adaptadorRutas: RutaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guardar_en_categoria)

        val lugarId = intent.getStringExtra("id") ?: ""
        val usuarioId = intent.getStringExtra("ID_USUARIO") ?: ""

        recyclerView = findViewById(R.id.recyclerViewRutasFavoritasGuardar)
        btnGuardarLugar = findViewById(R.id.btnGuardarDevolverse)
        btnCrearRuta = findViewById(R.id.btnCrearRutaNueva)
        val backBtn = findViewById<ImageButton>(R.id.imageButton5)

        backBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        btnCrearRuta.setOnClickListener {
            val intent = Intent(this, CrearRutaNueva::class.java)
            startActivity(intent)
            finish()
        }

        adaptadorRutas = RutaAdapter(listaRutas, lugarId, usuarioId) { ruta, isChecked ->
            rutasSeleccionadas[ruta.id] = isChecked
        }

        recyclerView.adapter = adaptadorRutas
        recyclerView.layoutManager = LinearLayoutManager(this)

        cargarRutas(usuarioId, lugarId)

        btnGuardarLugar.setOnClickListener {
            rutasSeleccionadas.forEach { (rutaId, isChecked) ->
                if (isChecked) {
                    añadirLugarRuta(rutaId, lugarId)
                    Log.d("GuardarEnCategoria", "Lugar añadido a la ruta: $rutaId")
                } else {
                    eliminarLugarRuta(rutaId, lugarId)
                    Log.d("GuardarEnCategoria", "Lugar eliminado de la ruta: $rutaId")
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun cargarRutas(usuarioId: String, lugarId: String) {
        db.collection("usuarios")
            .document(usuarioId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val rutasFavoritas = document.get("rutasFavoritas") as? List<String>
                    if (rutasFavoritas != null && rutasFavoritas.isNotEmpty()) {
                        listaRutaFavoritasId.addAll(rutasFavoritas)
                        cargarDatosDeRutas()
                    } else {
                        Log.d("Firestore", "El usuario no tiene rutas favoritas.")
                    }
                } else {
                    Log.d("Firestore", "No se encontró el documento del usuario con ID: $usuarioId")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al cargar el documento del usuario", e)
            }
    }

    private fun cargarDatosDeRutas() {
        listaRutaFavoritasId.forEach { rutaId ->
            db.collection("rutas")
                .document(rutaId)
                .get()
                .addOnSuccessListener { rutaDocument ->
                    if (rutaDocument.exists()) {
                        val ruta = rutaDocument.toObject(RutaModelo::class.java)
                        if (ruta != null) {
                            ruta.id = rutaDocument.id
                            listaRutas.add(ruta)
                        }
                    } else {
                        Log.d("Firestore", "Ruta no encontrada: $rutaId")
                    }
                }
                .addOnCompleteListener {
                    adaptadorRutas.notifyDataSetChanged()
                    Log.d("GuardarEnCategoria", "Rutas cargadas: ${listaRutas.size}")
                }
        }
    }
}
