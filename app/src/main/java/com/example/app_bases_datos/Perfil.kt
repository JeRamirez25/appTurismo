package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.app_bases_datos.utils.nombreUsuario
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

class Perfil : Fragment() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    var lugaresFavoritos = mutableListOf<String>()
    val lugaresMuseos = mutableListOf<FavoritoLugar>()
    val lugaresParques = mutableListOf<FavoritoLugar>()
    val lugaresRestaurantes = mutableListOf<FavoritoLugar>()
    val lugaresHistoricos = mutableListOf<FavoritoLugar>()
    val lugaresRecreativos = mutableListOf<FavoritoLugar>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_perfil, container, false)
        val logoutBtn = root.findViewById<Button>(R.id.logout)
        val info = root.findViewById<TextView>(R.id.texto)

        val recyclerViewMuseos = root.findViewById<RecyclerView>(R.id.recyclerViewMuseos)
        val recyclerViewParques = root.findViewById<RecyclerView>(R.id.recyclerViewParques)
        val recyclerViewRestaurantes = root.findViewById<RecyclerView>(R.id.recyclerViewRestaurantes)
        val recyclerViewHistoricos = root.findViewById<RecyclerView>(R.id.recyclerViewHistoricos)
        val recyclerViewRecreativos = root.findViewById<RecyclerView>(R.id.recyclerViewRecreativos)

        // Establecer layout manager para los RecyclerView
        recyclerViewMuseos.layoutManager = LinearLayoutManager(context)
        recyclerViewParques.layoutManager = LinearLayoutManager(context)
        recyclerViewRestaurantes.layoutManager = LinearLayoutManager(context)
        recyclerViewHistoricos.layoutManager = LinearLayoutManager(context)
        recyclerViewRecreativos.layoutManager = LinearLayoutManager(context)

        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email ?: ""
            nombreUsuario(userEmail, info)
        } else {
            info.text = "Usuario no autenticado"
        }

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
        }

        val auth = FirebaseAuth.getInstance()
        val userActual = auth.currentUser
        val userEmail = userActual?.email
        val userRef = db.collection("usuarios").whereEqualTo("id", userEmail)
        userRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Obtener el primer documento que coincida
                    val documento = querySnapshot.documents[0]
                    lugaresFavoritos = documento.get("lugaresFavoritos") as MutableList<String>
                    Log.d("hola", "$lugaresFavoritos")
                    for (id in lugaresFavoritos) {
                        db.collection("lugares").document(id)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    val lugar = document.toObject(FavoritoLugar::class.java)
                                    if (lugar != null) {
                                        if (lugar.categoria == "Museos") {
                                            lugaresMuseos.add(lugar)
                                            Log.d("hola", "Museos: $lugaresMuseos")
                                        }
                                        if (lugar.categoria == "Parques") {
                                            lugaresParques.add(lugar)
                                            Log.d("hola", "Parq: ${lugaresParques}s")
                                        }
                                        if (lugar.categoria == "Restaurantes") {
                                            lugaresRestaurantes.add(lugar)
                                            Log.d("hola", "Resta: $lugaresRestaurantes")
                                        }
                                        if (lugar.categoria == "Históricos") {
                                            lugaresHistoricos.add(lugar)
                                            Log.d("hola", "Histo: $lugaresHistoricos")
                                        }
                                        if (lugar.categoria == "Recreativos") {
                                            lugaresRecreativos.add(lugar)
                                            Log.d("hola", "Recre: $lugaresRecreativos")
                                        }
                                    }
                                } else {
                                    Log.d("Firestore", "No such document")
                                }

                                recyclerViewMuseos.adapter = LugarFavoritoAdapter(lugaresMuseos)
                                recyclerViewParques.adapter = LugarFavoritoAdapter(lugaresParques)
                                recyclerViewRestaurantes.adapter = LugarFavoritoAdapter(lugaresRestaurantes)
                                recyclerViewHistoricos.adapter = LugarFavoritoAdapter(lugaresHistoricos)
                                recyclerViewRecreativos.adapter = LugarFavoritoAdapter(lugaresRecreativos)

                            }
                            .addOnFailureListener { exception ->
                                Log.w("Firestore", "Get failed with ", exception)
                            }
                    }
                } else {
                    Log.d("PerfilFragment", "El documento del usuario no existe")
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores de obtención
                Log.e("PerfilFragment", "Error al obtener el documento: ", exception)
            }
        return root
    }
}


