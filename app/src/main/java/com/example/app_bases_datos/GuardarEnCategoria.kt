package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.añadirLugarRuta
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class GuardarEnCategoria : AppCompatActivity() {

    val db = Firebase.firestore
    val listaRutaFavoritasId = mutableListOf<String>()
    val listaRutas = mutableListOf<RutaModelo>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnGuardarLugar: Button
    private val rutasSeleccionadas = mutableMapOf<String, Boolean>()
    private lateinit var adaptadorRutas: RutaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guardar_en_categoria)

        // Aquí traigo el id del lugar y el id del usuario actual
        val lugarId = intent.getStringExtra("id") ?: ""
        val usuarioId = intent.getStringExtra("ID_USUARIO") ?: ""

        recyclerView = findViewById(R.id.recyclerViewRutasFavoritasGuardar)
        btnGuardarLugar = findViewById(R.id.btnGuardarDevolverse)

        adaptadorRutas = RutaAdapter(listaRutas, lugarId, usuarioId) { ruta, isChecked ->
            rutasSeleccionadas[ruta.id] = isChecked
        }
        recyclerView.adapter = adaptadorRutas
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            db.collection("usuarios").whereEqualTo("id", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0]
                        listaRutaFavoritasId.addAll(documento.get("rutasFavoritas") as List<String>)

                        listaRutaFavoritasId.forEach { rutaId ->
                            db.collection("rutas")
                                .document(rutaId)
                                .get()
                                .addOnSuccessListener { rutaDocument ->
                                    if (rutaDocument.exists()) {
                                        val ruta = rutaDocument.toObject(RutaModelo::class.java)
                                        if (ruta != null) {
                                            ruta.id = rutaDocument.id
                                            Log.d("PRUEBA", "${ruta.id}")
                                            listaRutas.add(ruta)
                                            Log.d("Hola", "$listaRutas")
                                            Log.d("HolaPrueba", "$ruta.id")
                                            adaptadorRutas.notifyDataSetChanged()
                                            Log.d(
                                                "GuardarEnCategoria",
                                                "Ruta añadida: ${ruta.nombre}"
                                            )
                                            Log.d(
                                                "GuardarEnCategoria",
                                                "Lista de rutas actual: $listaRutas"
                                            ) // Verifica la lista completa
                                            btnGuardarLugar.setOnClickListener {
                                                añadirLugarRuta(ruta.id, lugarId)
                                                val intent =
                                                    Intent(this, Detalles_de_lugares::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
        }
    }
}