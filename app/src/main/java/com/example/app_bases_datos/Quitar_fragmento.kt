package com.example.app_bases_datos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class Quitar_fragmento : Fragment() {
    private lateinit var inputField: EditText
    private lateinit var resultTextView: TextView
    private lateinit var processButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quitar_framento, container, false)

        inputField = view.findViewById(R.id.inputField)
        resultTextView = view.findViewById(R.id.resultTextView)
        processButton = view.findViewById(R.id.processButton)

        processButton.setOnClickListener {
            val input = inputField.text.toString()
            resultTextView.text = removeH(input)
        }
        return view
    }

    private fun removeH(input: String): String {
        val firstHIndex = input.indexOf('h')
        val lastHIndex = input.lastIndexOf('h')

        // Validamos que haya al menos dos 'h'
        return if (firstHIndex != -1 && lastHIndex != -1 && firstHIndex != lastHIndex) {
            input.substring(0, firstHIndex) + input.substring(lastHIndex + 1)
        } else {
            "error"
        }
    }

}