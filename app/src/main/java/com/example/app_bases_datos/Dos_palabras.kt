package com.example.app_bases_datos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
class Dos_palabras : Fragment() {
    private lateinit var boton: Button
    private lateinit var inputCadena: EditText
    private lateinit var resultado: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dos_numeros, container, false)

        inputCadena = view.findViewById(R.id.inputCadena)
        resultado = view.findViewById(R.id.resultado)
        boton = view.findViewById(R.id.boton)

        boton.setOnClickListener {
            val input = inputCadena.text.toString()
            resultado.text = cambiar_palabras(input)
        }

        return view
    }
    private fun cambiar_palabras(input: String): String {
        val words = input.split(" ")
        return if (words.size == 2) {
            "${words[1]} ${words[0]}"
        } else {
            "error"
        }
    }

}