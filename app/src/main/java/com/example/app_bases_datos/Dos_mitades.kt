package com.example.app_bases_datos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class Dos_mitades : Fragment() {

    private lateinit var boton: Button
    private lateinit var inputCadena: EditText
    private lateinit var resultado: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dos_mitades, container, false)

        boton = view.findViewById(R.id.btn)
        inputCadena = view.findViewById(R.id.editText)
        resultado = view.findViewById(R.id.textView7)

        boton.setOnClickListener {
            val cadena = inputCadena.text.toString()

            if (cadena.isNotBlank()) {
                val resultadoTexto = intercambiarMitades(cadena)
                resultado.text = resultadoTexto
            } else {
                resultado.text = "Ups! Algo salió mal. Revisa tu cadena"
            }
        }
        return view
    }

    private fun intercambiarMitades(cadena: String): String {
        val longitud = cadena.length
        val puntoCorte = (longitud + 1) / 2 // +1 para el caso impar

        val primeraMitad = cadena.substring(0, puntoCorte)
        val segundaMitad = cadena.substring(puntoCorte)

        return segundaMitad + primeraMitad
    }
}

