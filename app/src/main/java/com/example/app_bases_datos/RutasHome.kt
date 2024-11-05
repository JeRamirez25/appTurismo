package com.example.app_bases_datos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RutasHome : Fragment() {

    val db = Firebase.firestore
    val listaRutaFavoritasId = mutableListOf<String>()
    val listaRutas = mutableListOf<RutaModelo>()

    private lateinit var adaptadorRutasEscogidas: AdaptadorRutas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rutas_home, container, false)

        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        if (userEmail != null) {
            db.collection("usuarios").whereEqualTo("id", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0]
                        listaRutaFavoritasId.addAll(documento.get("rutasFavoritas") as List<String>)

                        listaRutaFavoritasId.forEach { rutaId ->
                            db.collection("rutas").document(rutaId).get()
                                .addOnSuccessListener { rutaDocument ->
                                    if (rutaDocument.exists()) {
                                        val modeloPrueba = rutaDocument.toObject(RutaModelo::class.java)
                                        modeloPrueba?.let {
                                            listaRutas.add(it)
                                            adaptadorRutasEscogidas.notifyDataSetChanged()
                                        }
                                    }
                                }
                        }
                    }
                }
        }

        view?.findViewById<Button>(R.id.btn_ruta)?.setOnClickListener {
            val fragment = RutasParametros()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewHome)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adaptadorRutasEscogidas = AdaptadorRutas(listaRutas)
        recyclerView.adapter = adaptadorRutasEscogidas

        adaptadorRutasEscogidas.notifyDataSetChanged()
    }
}
