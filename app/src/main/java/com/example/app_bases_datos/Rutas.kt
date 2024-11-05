package com.example.app_bases_datos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.app_bases_datos.utils.verificarRuta
import kotlinx.coroutines.launch

class Rutas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ID_USUARIO = "qDKCX43GUHbA0pyB8yRl"
        var root: View? = null

        lifecycleScope.launch {
            val tieneRutas = verificarRuta(ID_USUARIO)
            Log.d("Firestore", "¿El usuario tiene rutas? $tieneRutas")
            if (tieneRutas) {
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
        return root
    }
}
