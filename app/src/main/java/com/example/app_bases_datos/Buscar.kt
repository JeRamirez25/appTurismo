package com.example.app_bases_datos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.example.app_bases_datos.utils.updateLugaresFavoritos
import com.example.app_bases_datos.utils.obtenerIdUsuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Buscar : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth
    private val lugarId = "4X2Mhk25Rh01ScrM0SLI"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buscar, container, false)

        val lugarDescripcion = view.findViewById<TextView>(R.id.lugarDescripcion)
        val agregarFavoritoBtn = view.findViewById<Button>(R.id.agregarFavoritoBtn)

        lugarDescripcion.text = "Este es un lugar de prueba con ID: $lugarId"

        agregarFavoritoBtn.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {

                // Obtener el ID del usuario por correo
                obtenerIdUsuario(user.email ?: "") { usuarioId ->
                    if (usuarioId != null) {
                        // Llamar a la función para actualizar los lugares favoritos
                        updateLugaresFavoritos(usuarioId, lugarId)
                        Toast.makeText(context, "Lugar añadido a favoritos.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "No se encontró el usuario.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Debes iniciar sesión para añadir a favoritos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }
}