package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RutasEscogidas : Fragment() {

    private val lugares = mutableListOf<Lugar>()
    private val auth: FirebaseAuth = Firebase.auth
    private val lugaresRutasGeneradas = mutableListOf<Lugar>()
    private lateinit var adaptadorEscogidosRuta: AdaptadorEscogidos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rutas_escogidas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = view.findViewById(R.id.textView)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEscogidos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializa el adaptador y asígnalo al RecyclerView
        adaptadorEscogidosRuta = AdaptadorEscogidos(lugaresRutasGeneradas)
        recyclerView.adapter = adaptadorEscogidosRuta

        val args = this.arguments
        val listaLugares = args?.getStringArrayList("rutaEstablecida")
        val nombreRuta = args?.getString("nombreRuta")

        textView.text = nombreRuta ?: "Nombre de ruta no encontrado"
        if (listaLugares != null) {
            cargarLugaresEscogidos(listaLugares)
        } else {
            Log.d("RutasEscogidasFragment", "No hay datos en la lista de rutaEstablecida")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarLugaresEscogidos(lista: ArrayList<String>) {
        val db = Firebase.firestore
        for (id in lista) {
            db.collection("lugares")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val lugarRutaGenerada = document.toObject(Lugar::class.java)
                        if (lugarRutaGenerada != null) {
                            lugaresRutasGeneradas.add(lugarRutaGenerada)
                            adaptadorEscogidosRuta.notifyDataSetChanged() // Notifica cambios al adaptador
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("RutasEscogidasFragment", "Error al cargar lugar: ${exception.message}")
                }
        }
    }
}
