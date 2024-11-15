package com.example.app_bases_datos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
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
    private var listafiltrada = mutableListOf<Lugar>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buscar, container, false)
        val btnfiltro = view.findViewById<ImageButton>(R.id.botonFiltro)

        btnfiltro.setOnClickListener{
            val fragment = FiltroBuscar()
            val fragmentManager = getFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val tvWelcome = view.findViewById<TextView>(R.id.tvBienvenido)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            val searchView = view.findViewById<SearchView>(R.id.searchView)

            recyclerView.layoutManager = LinearLayoutManager(context)

            val user = auth.currentUser


            if (user != null) {

                val userEmail = user.email ?: ""
                saludo(userEmail, tvWelcome)


                obtenerIdUsuario(userEmail) { userId ->
                    if (userId != null) {

                        adapter.setOnItemClickListener(object : LugarAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                var lugarSeleccionado:Lugar
                                if (listafiltrada.size==0){
                                    lugarSeleccionado = lugares[position]
                                }
                                else {
                                    lugarSeleccionado = listafiltrada[position]
                                }
                                Log.d("listafiltrada", "$listafiltrada")
                                Log.d("listaLugares", "$lugarSeleccionado")
                                val intent = Intent(requireContext(), Detalles_de_lugares::class.java).apply {
                                    putExtra("id", lugarSeleccionado.id)
                                    putExtra("nombre", lugarSeleccionado.nombre)
                                    putExtra("direccion", lugarSeleccionado.direccion)
                                    putExtra("descripcion", lugarSeleccionado.descripcion)
                                    putExtra("precio", lugarSeleccionado.precio)
                                    putExtra("tiempo", lugarSeleccionado.tiempo)
                                    putExtra("imagenURL", lugarSeleccionado.imagenURL)
                                    putExtra("ID_USUARIO", userId) // Usamos el ID obtenido de Firestore
                                    Log.d("prueba_usuario", userId)
                                }
                                startActivity(intent)
                            }
                        })
                    } else {
                        Log.e("BuscarFragment", "No se pudo obtener el ID del usuario.")
                    }
                }
            } else {
                tvWelcome.text = "Hola, desconocido"
            }

            adapter = LugarAdapter(lugares)
            recyclerView.adapter = adapter

            cargarLugares()



            /*
            lugares.forEach{
                val listafiltradaC = ArrayList<Lugar>()
                if (listaParametros != null) {
                    listafiltrada.clear()
                    if (it.categoria in listaParametros) {
                        listafiltradaC.add(it)
                        listafiltrada.add(it)
                    }
                }
            }

            adapter.filtrarAD(listafiltradaC)
            */

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    searchView.clearFocus()

                    filtrar(p0.toString())

                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    filtrar(p0.toString())
                    return false
                }



            })

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
                    lugar.id = document.id
                    lugares.add(lugar)
                }
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.e("InicioFragment", "Error al cargar lugares: ${exception.message}")
            }

    }
    private fun filtrar(nombre: String){

        val listafiltradaA = ArrayList<Lugar>()
        listafiltrada.clear()
        val args = this.arguments
        val listaParametros = args?.getStringArrayList("parametrosFiltro")
        Log.d("Cantidad lugares", "$lugares")
        lugares.forEach{

            if (it.nombre.toLowerCase().contains(nombre.toLowerCase())){

                if (listaParametros != null) {
                    if (listaParametros.size != 0) {
                        if (it.categoria in listaParametros) {
                            listafiltradaA.add(it)
                            listafiltrada.add(it)
                        }
                    }
                    else {
                        listafiltradaA.add(it)
                        listafiltrada.add(it)
                    }
                }
                else {
                    listafiltradaA.add(it)
                    listafiltrada.add(it)
                }

            }



        }
        adapter.filtrarAD(listafiltradaA)

    }

    // Función para obtener el ID del usuario a partir de su correo
    private fun obtenerIdUsuario(correo: String, onComplete: (String?) -> Unit) {
        val db = Firebase.firestore
        Log.d("FirestoreDebug", "Buscando ID de usuario con correo: $correo")

        // Normalizar el correo a minúsculas para evitar problemas con mayúsculas/minúsculas
        val correoNormalizado = correo.lowercase()

        // Consulta a la colección "usuarios" por el correo normalizado
        db.collection("usuarios")
            .whereEqualTo("id", correoNormalizado) // Asegúrate de que el campo se llame "correo"
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val usuarioId = documents.documents[0].id // El ID del documento es el ID de usuario
                    Log.d("FirestoreDebug", "ID del usuario encontrado: $usuarioId")
                    onComplete(usuarioId) // Pasar el ID del usuario a la función callback
                } else {
                    Log.d("FirestoreDebug", "No se encontraron documentos con el correo: $correo")
                    onComplete(null) // Si no se encuentra el usuario
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error al buscar el usuario por correo", e)
                onComplete(null) // Si hay un error, devolver null
            }
    }
}
