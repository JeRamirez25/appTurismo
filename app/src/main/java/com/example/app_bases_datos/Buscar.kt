package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.saludo
import com.google.firebase.firestore.ktx.firestore
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

class Buscar : Fragment() {

    private val lugares = mutableListOf<Lugar>()
    private lateinit var adapter: LugarAdapter
    private val auth: FirebaseAuth = Firebase.auth

    //private val auth: FirebaseAuth = Firebase.auth
    //private val lugarId = "4X2Mhk25Rh01ScrM0SLI"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buscar, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            // Referencias de las vistas
            val tvWelcome = view.findViewById<TextView>(R.id.tvBienvenido)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            //val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

            val user = auth.currentUser
            if (user != null) {
                val userEmail = user.email ?: ""
                saludo(userEmail, tvWelcome)
            } else {
                tvWelcome.text = "Hola, desconocido"
            }

            adapter = LugarAdapter(lugares){ lugar ->
                abrirDetalleLugar(lugar)
            }
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            cargarLugares()

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    tvWelcome.visibility = if (dy > 0) View.GONE else View.VISIBLE
                }
            })
        } catch (e: Exception) {
            Log.e("BuscarFragment", "Error en onViewCreated: ${e.message}")
        }
    }

    private fun cargarLugares() {
        val db = Firebase.firestore
        db.collection("lugares")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val lugar = document.toObject(Lugar::class.java)
                    lugares.add(lugar)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("InicioFragment", "Error al cargar lugares: ${exception.message}")
            }
    }

    @SuppressLint("CommitTransaction")
    private fun abrirDetalleLugar(lugar: Lugar) {
        val fragment = LugarDetalle().apply {
            arguments = Bundle().apply {
                putString("nombre", "Ejemplo de Lugar")
                putString("precio", "$100")
                putString("descripcion", "Un lugar muy interesante para visitar.")
                putString("direccion", "Dirección Ejemplo 123")
                putString("tiempo", "2 horas")
                putString("imagenURL", "https://example.com/imagen.jpg")
            }
        }

        // Agregar el fragmento al contenedor
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()

    }
}