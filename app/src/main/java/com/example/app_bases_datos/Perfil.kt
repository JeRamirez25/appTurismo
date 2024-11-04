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
    /*private fun configurarRecyclerViews() {
        // RecyclerView para Museos
        val recyclerMuseos = view?.findViewById<RecyclerView>(R.id.categoriaMuseos)
        if (recyclerMuseos != null) {
            recyclerMuseos.layoutManager = LinearLayoutManager(context)
        }
        Log.d("LugaresAdapter", "Número de lugares en museos: ${lugaresMuseos.size}")
        recyclerMuseos?.adapter = LugarFavoritoAdapter(lugaresMuseos)

        // RecyclerView para Parques
        val recyclerParques = view?.findViewById<RecyclerView>(R.id.categoriaParques)
        if (recyclerParques != null) {
            recyclerParques.layoutManager = LinearLayoutManager(context)
        }
        Log.d("LugaresAdapter", "Número de lugares en museos: ${lugaresParques.size}")
        recyclerParques?.adapter = LugarFavoritoAdapter(lugaresParques)

        // RecyclerView para Restaurantes
        val recyclerRestaurantes = view?.findViewById<RecyclerView>(R.id.categoriaRestaurantes)
        if (recyclerRestaurantes != null) {
            recyclerRestaurantes.layoutManager = LinearLayoutManager(context)
        }
        Log.d("LugaresAdapter", "Número de lugares en museos: ${lugaresRestaurantes.size}")
        recyclerRestaurantes?.adapter = LugarFavoritoAdapter(lugaresRestaurantes)

        // RecyclerView para Históricos
        val recyclerHistoricos = view?.findViewById<RecyclerView>(R.id.categoriaHistoricos)
        if (recyclerHistoricos != null) {
            recyclerHistoricos.layoutManager = LinearLayoutManager(context)
        }
        Log.d("LugaresAdapter", "Número de lugares en museos: ${lugaresHistoricos.size}")
        recyclerHistoricos?.adapter = LugarFavoritoAdapter(lugaresHistoricos)

        // RecyclerView para Recreativos
        val recyclerRecreativos = view?.findViewById<RecyclerView>(R.id.categoriaRecreativos)
        if (recyclerRecreativos != null) {
            recyclerRecreativos.layoutManager = LinearLayoutManager(context)
        }
        Log.d("LugaresAdapter", "Número de lugares en museos: ${lugaresRecreativos.size}")
        recyclerRecreativos?.adapter = LugarFavoritoAdapter(lugaresRecreativos)
    }*/



    /*private fun obtenerLugaresFavoritos() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userEmail = user?.email

        val userRef = db.collection("usuarios").whereEqualTo("id", userEmail)
        userRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Obtener el primer documento que coincida
                    val documento = querySnapshot.documents[0]
                    val lugaresFavoritos = documento.get("lugaresFavoritos") as? List<String>
                    if (lugaresFavoritos != null) {
                        organizarPorCategoria(lugaresFavoritos)
                        Log.d("hola","$lugaresFavoritos")
                        //obtenerDetallesLugaresFavoritos(favoritos){
                            //Log.d("PerfilFragment", "Datos cargados correctamente")
                            //onDataLoaded()
                        //}
                    } else {
                        Log.d("PerfilFragment", "No hay lugares favoritos en la lista")
                    }
                } else {
                    Log.d("PerfilFragment", "El documento del usuario no existe")
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores de obtención
                Log.e("PerfilFragment", "Error al obtener el documento: ", exception)
            }
    }*/


   /* fun organizarPorCategoria(lugaresFavoritos: List<String>) {
        val lugaresMuseos = mutableListOf<FavoritoLugar>()
        val lugaresParques = mutableListOf<FavoritoLugar>()
        val lugaresRestaurantes = mutableListOf<FavoritoLugar>()
        val lugaresHistoricos = mutableListOf<FavoritoLugar>()
        val lugaresRecreativos = mutableListOf<FavoritoLugar>()

        val lugaresRef = FirebaseFirestore.getInstance().collection("lugares")

        // Iterar sobre los lugares favoritos
        for (lugarID in lugaresFavoritos) {
            lugaresRef.document(lugarID).get().addOnSuccessListener { lugar ->
                if (lugar.exists()) {
                    val categoria = lugar.getString("categoria")
                    val favoritoLugar = FavoritoLugar(
                        nombre = lugar.getString("nombre") ?: "",
                        direccion = lugar.getString("direccion") ?: "",
                        tiempo = lugar.getLong("tiempo") ?: 0,
                        imagenURL = lugar.getString("imagenURL") ?: "",
                        precio = lugar.getDouble("precio") ?: 0.0,
                        descripcion = lugar.getString("descripcion") ?: ""
                    )

                    // Clasificar según la categoría
                    when (categoria) {
                        "Museos" -> lugaresMuseos.add(favoritoLugar)
                        "Parques" -> lugaresParques.add(favoritoLugar)
                        "Restaurantes" -> lugaresRestaurantes.add(favoritoLugar)
                        "Históricos" -> lugaresHistoricos.add(favoritoLugar)
                        "Recreativos" -> lugaresRecreativos.add(favoritoLugar)
                    }
                }
            }
        }

        // Una vez que todos los lugares han sido clasificados, puedes actualizar la UI
        mostrarLugaresPorCategoria(lugaresMuseos, lugaresParques, lugaresRestaurantes, lugaresHistoricos, lugaresRecreativos)
    }*/



    /*private fun obtenerDetallesLugaresFavoritos(favoritos: List<String>,onDataLoaded: () -> Unit) {
        val lugaresRef = db.collection("lugares")
        var lugaresContados = 0 // Contador de lugares que hemos procesado

        // Para cada ID en la lista de favoritos, obtenemos el documento correspondiente
        for (id in favoritos) {
            lugaresRef.document(id).get()
                .addOnSuccessListener { lugarSnapshot ->
                    if (lugarSnapshot.exists()) {
                        val categoria = lugarSnapshot.getString("categoria")
                        val lugar = lugarSnapshot.toObject(Lugar::class.java) // Asegúrate de que Lugar tiene un constructor vacío
                        if (lugar != null) {
                            // Clasificar según la categoría
                            when (categoria) {
                                "Restaurantes" -> {
                                    lugaresRestaurantes.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Restaurantes: ${lugar.nombre}")
                                }
                                "Museos" -> {
                                    lugaresMuseos.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Museos: ${lugar.nombre}")
                                }
                                "Parques" -> {
                                    lugaresParques.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Parques: ${lugar.nombre}")
                                }
                                "Históricos" -> {
                                    lugaresHistoricos.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Históricos: ${lugar.nombre}")
                                }

                                "Historicos" -> {
                                    lugaresHistoricos.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Históricos: ${lugar.nombre}")
                                }

                                "Recreativos" -> {
                                    lugaresRecreativos.add(lugar)
                                    Log.d("PerfilFragment", "Agregado a Recreativos: ${lugar.nombre}")
                                }
                                else -> {
                                    Log.d("PerfilFragment", "Categoría desconocida para el lugar: ${lugar.nombre}")
                                }
                            }
                        }
                    } else {
                        Log.d("PerfilFragment", "Lugar no encontrado para el ID: $id")
                    }
                    lugaresContados++ // Incrementar el contador
                    Log.d("PerfilFragment", "Lugares contados: $lugaresContados de ${favoritos.size}")
                    // Cuando todos los lugares hayan sido procesados, imprime las listas
                    if (lugaresContados == favoritos.size) {
                        imprimirLugaresFavoritos()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("PerfilFragment", "Error al obtener el lugar: ", exception)
                    lugaresContados++ // Incrementar también en caso de error
                    if (lugaresContados == favoritos.size) {
                        imprimirLugaresFavoritos()
                    }
                }
        }
    }*/

    /*private fun imprimirLugaresFavoritos() {
        Log.d("PerfilFragment", "Lugares Favoritos - Museos: $lugaresMuseos")
        Log.d("PerfilFragment", "Lugares Favoritos - Parques: $lugaresParques")
        Log.d("PerfilFragment", "Lugares Favoritos - Restaurantes: $lugaresRestaurantes")
        Log.d("PerfilFragment", "Lugares Favoritos - Históricos: $lugaresHistoricos")
        Log.d("PerfilFragment", "Lugares Favoritos - Recreativos: $lugaresRecreativos")
    }
}*/
