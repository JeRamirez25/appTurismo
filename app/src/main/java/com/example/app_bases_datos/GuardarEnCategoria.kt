package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.añadirLugarRuta
import com.example.app_bases_datos.utils.eliminarLugarRuta
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class GuardarEnCategoria : AppCompatActivity() {

    val db = Firebase.firestore
    val listaRutaFavoritasId = mutableListOf<String>()
    val listaRutas = mutableListOf<RutaModelo>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnGuardarLugar: Button
    private lateinit var btnCrearRuta: Button
    private val rutasSeleccionadas = mutableMapOf<String, Boolean>()
    private lateinit var adaptadorRutas: RutaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guardar_en_categoria)

        // Obtener los IDs
        val lugarId = intent.getStringExtra("id") ?: ""
        val usuarioId = intent.getStringExtra("ID_USUARIO") ?: ""

        recyclerView = findViewById(R.id.recyclerViewRutasFavoritasGuardar)
        btnGuardarLugar = findViewById(R.id.btnGuardarDevolverse)
        btnCrearRuta = findViewById(R.id.btnCrearRutaNueva)
        val backBtn = findViewById<ImageButton>(R.id.imageButton5)

        backBtn.setOnClickListener {
            val intent = Intent(this, Detalles_de_lugares::class.java)
            startActivity(intent)
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

        // Mostrar rutas
        cargarRutas(usuarioId, lugarId)

        btnGuardarLugar.setOnClickListener {
            rutasSeleccionadas.forEach { (rutaId, isChecked) ->
                if (isChecked) {
                    // Si está seleccionado, añadir el lugar a la ruta
                    añadirLugarRuta(rutaId, lugarId)
                    Log.d("GuardarEnCategoria", "Lugar añadido a la ruta: $rutaId")
                } else {
                    // Si no está seleccionado, eliminar el lugar de la ruta
                    eliminarLugarRuta(rutaId, lugarId)
                    Log.d("GuardarEnCategoria", "Lugar eliminado de la ruta: $rutaId")
                }
            }
            Intent(this, Detalles_de_lugares::class.java).also {
                startActivity(it)
                finish()
            }
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