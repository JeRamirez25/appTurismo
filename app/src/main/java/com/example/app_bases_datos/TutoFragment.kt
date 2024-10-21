package com.example.app_bases_datos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController


class TutoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_tuto, container, false)

        val buttonNav = root.findViewById<ImageButton>(R.id.imageButton)

        buttonNav.setOnClickListener {
            findNavController().navigate(R.id.action_tutoFragment_to_tutoriFragment)
        }

        return root
    }

}