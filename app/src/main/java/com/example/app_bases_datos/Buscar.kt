package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.saludo
import com.google.firebase.firestore.ktx.firestore

class Buscar : Fragment() {

    private val lugares = mutableListOf<Lugar>()
    private lateinit var adapter: LugarAdapter
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buscar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val tvWelcome = view.findViewById<TextView>(R.id.tvBienvenido)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context)

            val user = auth.currentUser
            if (user != null) {
                val userEmail = user.email ?: ""
                saludo(userEmail, tvWelcome)
            } else {
                tvWelcome.text = "Hola, desconocido"
            }

            adapter = LugarAdapter(lugares)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(object : LugarAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    //Toast.makeText(requireContext(), "Click al item numero. "+ lugares[position], Toast.LENGTH_LONG).show()
                    //val intent = Intent(requireContext(),LugarDetalle::class.java)
                    val lugarSeleccionado = lugares[position]
                    val intent = Intent(requireContext(), Detalles_de_lugares::class.java).apply {
                        putExtra("nombre", lugarSeleccionado.nombre)
                        putExtra("direccion", lugarSeleccionado.direccion)
                        putExtra("descripcion", lugarSeleccionado.descripcion)
                        putExtra("precio", lugarSeleccionado.precio)
                        putExtra("tiempo", lugarSeleccionado.tiempo)
                        putExtra("imagenURL", lugarSeleccionado.imagenURL) // Asegúrate de que esta propiedad exista en tu clase Lugar
                    }
                    // Inicia la actividad
                    startActivity(intent)

                }

            })

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

    @SuppressLint("NotifyDataSetChanged")
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
}