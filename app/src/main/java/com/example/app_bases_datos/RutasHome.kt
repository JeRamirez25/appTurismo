package com.example.app_bases_datos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RutasHome : Fragment() {

    val db = Firebase.firestore
    var rutasFavoritas = mutableListOf<String>()
    val rutaFavorito = mutableListOf<String>()
    val listaRutaFavoritasId = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rutas_home, container, false)

        // Obtener el usuario autenticado
        val auth = FirebaseAuth.getInstance()
        val userActual = auth.currentUser
        val userEmail = userActual?.email

        Log.d("Debug", "Correo del usuario autenticado: $userEmail")

        if (userEmail != null) {
            // Referencia al usuario en la colección "usuarios" por el campo "id"
            db.collection("usuarios").whereEqualTo("id",userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0]
                        // Accede a la lista de IDs de rutas favoritas
                        listaRutaFavoritasId.addAll(documento.get("rutasFavoritas") as List<String>)

                        // Paso 2: Iterar sobre la lista de IDs y obtener detalles de cada ruta
                        listaRutaFavoritasId.forEach { rutaId ->
                            db.collection("rutas").document(rutaId).get()
                                .addOnSuccessListener { rutaDocument ->
                                    if (rutaDocument.exists()) {
                                        // Obtener el nombre de la ruta y los IDs de los lugares
                                        val nombreRuta = rutaDocument.getString("nombre")
                                        val lugaresId = rutaDocument.get("lugares") as? List<String>
                                            ?: emptyList<String>()

                                        // Aquí puedes guardar o mostrar el nombre de la ruta y los lugares
                                        println("Ruta: $nombreRuta")
                                        println("Lugares en la ruta: $lugaresId")
                                    } else {
                                        println("Ruta con ID $rutaId no encontrada.")
                                    }
                                }
                                .addOnFailureListener { e ->
                                    println("Error al obtener la ruta con ID $rutaId: ${e.message}")
                                }
                        }
                    } else {
                        println("Usuario con ID $userEmail no encontrado.")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error al obtener el usuario: ${e.message}")
                }
        }


        // Botón para cambiar de fragmento
        view?.findViewById<Button>(R.id.btn_ruta)?.setOnClickListener {
            val fragment = RutasParametros()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }

        return view
    }

}
