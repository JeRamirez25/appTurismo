package com.example.app_bases_datos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class Rutas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_rutas,container, false)

        val buttonNav = root.findViewById<Button>(R.id.StartBtn)

        buttonNav.setOnClickListener {
            //findNavController().navigate(R.id.action_rutas2_to_rutasParametros)

            val fragmentManager = getFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.frame_layout, RutasParametros())
                fragmentTransaction.commit()
            }

        }
        return root
    }

}