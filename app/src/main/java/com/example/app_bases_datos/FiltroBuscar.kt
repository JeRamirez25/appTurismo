package com.example.app_bases_datos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList


class FiltroBuscar : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_filtro_buscar, container, false)

        val db = FirebaseFirestore.getInstance()

        val parametrosSeleccionados = mutableListOf<String>()
        val rutaEstablecida = mutableListOf<String>()

        val checkMuseos = view.findViewById<CheckBox>(R.id.check_museos)
        val checkParques = view.findViewById<CheckBox>(R.id.check_parques)
        val checkRecreativos = view.findViewById<CheckBox>(R.id.check_recreativos)
        val checkHistoricos = view.findViewById<CheckBox>(R.id.check_historicos)
        val checkGastronomia = view.findViewById<CheckBox>(R.id.check_gastronomia)

        val btnAplicar = view.findViewById<Button>(R.id.btn_aplicar)


        btnAplicar.setOnClickListener {

            val museosChecked = checkMuseos.isChecked
            val parquesChecked = checkParques.isChecked
            val recreativosChecked = checkRecreativos.isChecked
            val historicosChecked = checkHistoricos.isChecked
            val gastronomiaChecked = checkGastronomia.isChecked

            if (museosChecked) parametrosSeleccionados.add("Museos")
            if (parquesChecked) parametrosSeleccionados.add("Parques")
            if (recreativosChecked) parametrosSeleccionados.add("Recreativos")
            if (historicosChecked) parametrosSeleccionados.add("Historicos")
            if (gastronomiaChecked) parametrosSeleccionados.add("Restaurantes")

            val bundle = Bundle()
            bundle.putStringArrayList("parametrosFiltro", ArrayList(parametrosSeleccionados))
            val fragment = Buscar()
            fragment.arguments = bundle

            val fragmentManager = getFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
            }
        }


        return view
    }

}