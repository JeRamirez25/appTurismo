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

        // Verificar que el usuario esté autenticado y el correo no sea nulo
        if (userEmail != null) {
            // Referencia al usuario en la colección "usuarios" por el campo "id"
            db.collection("usuarios")
                .whereEqualTo("id", userEmail) // Cambiar "id" si el campo es diferente en Firestore
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documento = querySnapshot.documents[0]
                        Log.d("Debug", "Documento de usuario encontrado: ${documento.id}")

                        // Obtener rutas favoritas
                        rutasFavoritas = documento.get("rutasFavoritas") as? MutableList<String> ?: mutableListOf()
                        Log.d("Debug", "Rutas favoritas del usuario: $rutasFavoritas")

                        // Obtener detalles de cada ruta favorita
                        for (id in rutasFavoritas) {
                            db.collection("rutas")
                                .document(id)
                                .get()
                                .addOnSuccessListener { rutaDocument ->
                                    //val rutaGuardada = rutaDocument.toObject(RutaModelo::class.java)
                                    val nombreRuta = documento.getString("nombre")
                                    val lugaresID = documento.get("lugares") as? MutableList<String>
                                    println(lugaresID)
                                    if (nombreRuta != null) {
                                        rutaFavorito.add(nombreRuta)
                                    }
                                    rutaFavorito.add(lugaresID.toString())
                                    println(rutaFavorito)
                                    //Log.d("Debug", "Detalles de la ruta guardada con ID $id: $rutaData")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Error", "Error al obtener los detalles de la ruta con ID $id", e)
                                }
                        }
                    } else {
                        Log.d("Debug", "No se encontró ningún usuario con el correo: $userEmail")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Error", "Error al obtener el usuario por correo", e)
                }
        } else {
            Log.d("Error", "El usuario no está autenticado o el correo es nulo")
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
    fun mostrarRutasFavoritas(rutasFavoritas: List<Map<String, Any>>) {
        for (ruta in rutasFavoritas) {
            val nombre = ruta["nombre"]
            val lugaresIds = ruta["lugaresIds"] as? List<String>

            Log.d("RutaFavorita", "Nombre: $nombre, Lugares: $lugaresIds")
            // Aquí puedes hacer algo con cada ruta, como agregarla a un RecyclerView
        }
    }
}
