package com.example.app_bases_datos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.app_bases_datos.utils.obtenerIdUsuario
import com.example.app_bases_datos.utils.verificarRuta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class Rutas : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root: View? = null
        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email ?: ""
            obtenerIdUsuario(userEmail) { ID_USUARIO ->
                lifecycleScope.launch {
                    val tieneRutas = ID_USUARIO?.let { verificarRuta(it) }
                    Log.d("Firestore", "¿El usuario tiene rutas? $tieneRutas")
                    if (tieneRutas == true) {
                        val fragmentManager = getFragmentManager()
                        val fragmentTransaction = fragmentManager?.beginTransaction()
                        if (fragmentTransaction != null) {
                            fragmentTransaction.replace(R.id.frame_layout, RutasHome())
                            fragmentTransaction.commit()
                        }
                    } else {
                        val fragmentManager = getFragmentManager()
                        val fragmentTransaction = fragmentManager?.beginTransaction()
                        if (fragmentTransaction != null) {
                            fragmentTransaction.replace(R.id.frame_layout, NoRutas())
                            fragmentTransaction.commit()
                        }
                    }
                }
            }
        }
        return root
    }
}
