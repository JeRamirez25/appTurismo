package com.example.app_bases_datos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_bases_datos.utils.saludo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import kotlin.math.*
import java.util.Random

class RutasParametros : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rutas_parametros, container, false)

        val db = FirebaseFirestore.getInstance()

        data class Geopoint(val latitud: Double, val longitud: Double)

        val rutaEstablecida = mutableListOf<String>()
        val parametrosSeleccionados = mutableListOf<String>()
        var numeroLugares: Int = 0
        var tiempoRecorrido: Int = 0
        var tiempoParametro: Int = 0
        val listaId = mutableListOf<String>()
        val categorias = mutableListOf<String>()
        val lugares = mutableListOf<Geopoint>()
        val nombres = mutableListOf<String>()
        val descripciones = mutableListOf<String>()
        val direcciones = mutableListOf<String>()
        val imagenes = mutableListOf<String>()
        val precios = mutableListOf<Int>()
        val tiempos = mutableListOf<Int>()

        val nombreRuta = view.findViewById<EditText>(R.id.input_nombre_ruta)

        val checkMuseos = view.findViewById<CheckBox>(R.id.check_museos)
        val checkParques = view.findViewById<CheckBox>(R.id.check_parques)
        val checkRecreativos = view.findViewById<CheckBox>(R.id.check_recreativos)
        val checkHistoricos = view.findViewById<CheckBox>(R.id.check_historicos)
        val checkGastronomia = view.findViewById<CheckBox>(R.id.check_gastronomia)

        val listaDesplegable = view.findViewById<Spinner>(R.id.tiempo_disponible)
        val opcionesLista = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesLista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        listaDesplegable.adapter = adapter

        val btnGenerarRuta = view.findViewById<Button>(R.id.btn_generar_ruta)

        listaDesplegable.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                tiempoParametro = opcionesLista[position] * 60
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(context, "Por favor, seleccione el tiempo", Toast.LENGTH_SHORT).show()
            }
        }

        btnGenerarRuta.setOnClickListener {
            val texto = nombreRuta.text.toString()
            val museosChecked = checkMuseos.isChecked
            val parquesChecked = checkParques.isChecked
            val recreativosChecked = checkRecreativos.isChecked
            val historicosChecked = checkHistoricos.isChecked
            val gastronomiaChecked = checkGastronomia.isChecked

            if (texto.isBlank()) {
                nombreRuta.error = "Este campo es obligatorio"
                Toast.makeText(context, "Por favor, ingrese el nombre de la ruta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!museosChecked && !parquesChecked && !recreativosChecked && !historicosChecked && !gastronomiaChecked) {
                Toast.makeText(context, "Debes seleccionar al menos un lugar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (museosChecked) parametrosSeleccionados.add("Museos")
            if (parquesChecked) parametrosSeleccionados.add("Parques")
            if (recreativosChecked) parametrosSeleccionados.add("Recreativos")
            if (historicosChecked) parametrosSeleccionados.add("Historicos")
            if (gastronomiaChecked) parametrosSeleccionados.add("Restaurantes")

            db.collection("lugares").get().addOnSuccessListener { result ->
                for (document in result) {
                    val categoria = document.getString("categoria")
                    if (categoria in parametrosSeleccionados) {
                        listaId.add(document.id)
                        categorias.add(categoria ?: "")
                        nombres.add(document.getString("nombre") ?: "")
                        descripciones.add(document.getString("descripcion") ?: "")
                        direcciones.add(document.getString("direccion") ?: "")
                        imagenes.add(document.getString("imagenURL") ?: "")
                        precios.add(document.getLong("precio")?.toInt() ?: 0)
                        tiempos.add(document.getLong("tiempo")?.toInt() ?: 0)

                        document.getGeoPoint("geopoint")?.let {
                            lugares.add(Geopoint(it.latitude, it.longitude))
                        }
                        numeroLugares++
                    }
                }

                val r = Random()
                val lugarAleatorio = r.nextInt(numeroLugares)
                val puntoReferencia = lugares[lugarAleatorio]

                fun distancia(point1: Geopoint, point2: Geopoint): Double {
                    val R = 6371.0
                    val latDistancia = Math.toRadians(point2.latitud - point1.latitud)
                    val lonDistancia = Math.toRadians(point2.longitud - point1.longitud)
                    val a = sin(latDistancia / 2).pow(2.0) + cos(Math.toRadians(point1.latitud)) * cos(Math.toRadians(point2.latitud)) * sin(lonDistancia / 2).pow(2.0)
                    return R * 2 * atan2(sqrt(a), sqrt(1 - a))
                }

                val puntosCercanos = lugares.sortedBy { distancia(puntoReferencia, it) }.take(numeroLugares)

                puntosCercanos.forEachIndexed { index, _ ->
                    if (index >= tiempos.size || tiempoRecorrido + tiempos[index] > tiempoParametro) return@forEachIndexed

                    rutaEstablecida.add(listaId[index])
                    tiempoRecorrido += tiempos[index]
                }
                println("El nombre de la ruta es \"$texto\", tiene como paradas $rutaEstablecida")

                val bundle = Bundle()
                bundle.putStringArrayList("rutaEstablecida", ArrayList(rutaEstablecida))
                bundle.putString("nombreRuta", texto)
                val fragment = RutasEscogidas()
                fragment.arguments = bundle

                val fragmentManager = getFragmentManager()
                val fragmentTransaction = fragmentManager?.beginTransaction()
                if (fragmentTransaction != null) {
                    fragmentTransaction.replace(R.id.frame_layout, fragment)
                    fragmentTransaction.commit()
                }
            }
        }
        return view
    }
}