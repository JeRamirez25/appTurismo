package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.añadirLugarRuta
import com.example.app_bases_datos.utils.crearRuta
import com.example.app_bases_datos.utils.obtenerIdUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RutasEscogidas : Fragment() {

    private val lugares = mutableListOf<Lugar>()
    private val auth: FirebaseAuth = Firebase.auth
    private val lugaresRutasGeneradas = mutableListOf<Lugar>()
    private lateinit var adaptadorEscogidosRuta: AdaptadorEscogidos
    private lateinit var rutaId: String

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
        val guardarBtn = view.findViewById<ImageButton>(R.id.guardarBTN)

        adaptadorEscogidosRuta = AdaptadorEscogidos(lugaresRutasGeneradas)
        recyclerView.adapter = adaptadorEscogidosRuta

        val args = this.arguments
        val listaLugares = args?.getStringArrayList("rutaEstablecida")
        val nombreRuta = args?.getString("nombreRuta")

        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email ?: ""
            obtenerIdUsuario(userEmail) { ID_USUARIO ->
                if (ID_USUARIO != null) {

                    textView.text = nombreRuta ?: "Nombre de ruta no encontrado"
                    if (listaLugares != null) {
                        cargarLugaresEscogidos(listaLugares)
                    } else {
                        Log.d(
                            "RutasEscogidasFragment",
                            "No hay datos en la lista de rutaEstablecida"
                        )
                    }
                    guardarBtn.setOnClickListener {
                        if (nombreRuta != null) {
                            crearRuta(ID_USUARIO, nombreRuta) { id ->
                                rutaId = id
                                if (listaLugares != null) {
                                    for (lugar in listaLugares) {
                                        añadirLugarRuta(rutaId, lugar)
                                    }
                                }
                            }
                            val fragmentManager = getFragmentManager()
                            val fragmentTransaction = fragmentManager?.beginTransaction()
                            if (fragmentTransaction != null) {
                                fragmentTransaction.replace(R.id.frame_layout, Rutas())
                                fragmentTransaction.commit()
                            }
                        }
                    }

                } else {
                    Log.d("MainActivity", "No se encontró el usuario con ese correo.")
                }
            }
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